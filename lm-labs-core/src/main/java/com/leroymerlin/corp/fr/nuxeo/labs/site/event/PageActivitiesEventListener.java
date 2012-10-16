package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.notification.MailNotification;
import com.leroymerlin.corp.fr.nuxeo.labs.site.notification.PageNotificationService;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.EventNames;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.State;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public class PageActivitiesEventListener implements EventListener {

    private static final Log LOG = LogFactory.getLog(PageActivitiesEventListener.class);

    static final List<String> markDocs = new ArrayList<String>(
            Arrays.asList(
                    Docs.PAGEBLOCS.type(),
                    Docs.DASHBOARD.type(),
                    Docs.PAGE.type(),
                    Docs.PAGENEWS.type(), 
                    Docs.PAGELIST.type(),
                    Docs.PAGECLASSEUR.type(),
                    Docs.HTMLPAGE.type(),
                    Docs.PAGEFORUM.type(),
                    Docs.LABSNEWS.type()
                    ));
    static final List<String> markParentDocs = new ArrayList<String>(
            Arrays.asList(
                    Docs.PAGECLASSEURFOLDER.type(),
                    "File",
                    "Picture"
                    ));
    static final List<String> markParentPageDocs = new ArrayList<String>(
            Arrays.asList(
                    Docs.LABSNEWS.type(), // on creation
                    Docs.PAGELIST_LINE.type()
                    ));

    private PageNotificationService notificationService = null;

    @Override
    public void handleEvent(Event evt) throws ClientException {
        if (!(evt.getContext() instanceof DocumentEventContext)) {
            return;
        }
        String eventName = evt.getName();
        if (!DocumentEventTypes.DOCUMENT_UPDATED.equals(eventName)
                && !DocumentEventTypes.DOCUMENT_CREATED.equals(eventName)
                && !DocumentEventTypes.ABOUT_TO_REMOVE.equals(eventName)
                ) {
            return;
        }
        DocumentEventContext ctx = (DocumentEventContext) evt.getContext();
        DocumentModel doc = ctx.getSourceDocument();
        CoreSession session = ctx.getCoreSession();
        if (doc != null) {
            List<String> allTypes = new ArrayList<String>();
            allTypes.addAll(markDocs);
            allTypes.addAll(markParentDocs);
            allTypes.addAll(markParentPageDocs);
            if (!allTypes.contains(doc.getType())) {
            	return;
            }
            LOG.debug("event: " + eventName);
            boolean processed = false;
            try {
                getNotificationService();
            } catch (Exception e) {
                LOG.error("Unable to retrieve notification service. Skip processing event. So it won't be notified to users.", e);
                return;
            }
            if (DocumentEventTypes.DOCUMENT_UPDATED.equals(eventName)) {
                if (markDocs.contains(doc.getType())) {
                    processed = Mark(doc, session);
                } else if (markParentPageDocs.contains(doc.getType())
                        || markParentDocs.contains(doc.getType())
                        ) {
                    processed = Mark(doc, session);
                }
            } else if (DocumentEventTypes.DOCUMENT_CREATED.equals(eventName)) {
                Page parentPage = Tools.getAdapter(SiteDocument.class, doc, session).getParentPage();
                if (parentPage == null) {
                    return;
                }
                if (markParentPageDocs.contains(doc.getType())
                        || markParentDocs.contains(doc.getType())
                        ) {
                    processed = Mark(doc, session);
                }
            } else if (DocumentEventTypes.ABOUT_TO_REMOVE.equals(eventName)) {
                try {
                    Page parentPage = notificationService.getRelatedPage(doc, session);
                    if (parentPage == null) {
                        return;
                    }
                } catch (Exception e) {
                    LOG.error(e, e);
                    return;
                }
                if (Docs.pageDocs().contains(Docs.fromString(doc.getType()))) {
                    processed = notifyPage(doc, EventNames.PAGE_REMOVED, session);
                } else {
                    processed = Mark(doc, session);

                    // TODO
//                  processed = notifyPage(doc, EventNames.PAGE_REMOVED);
                }
            } else {
                // should not happen
            }
            if (!processed) {
                LOG.debug("event " + eventName + " on " + doc.getPathAsString() + " not processed.");
            } else {
                LOG.debug("event " + eventName + " on " + doc.getPathAsString() + " processed.");
            }
        }
    }

    private boolean notifyPage(DocumentModel doc, String eventName, CoreSession session) throws ClientException {
        try {
            if (!notificationService.canBeMarked(doc, session)) {
                return false;
            }
            Page page = getPage(doc, session);
            notificationService.notifyPageEvent(page, eventName, session);
            return true;
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return false;
    }

    private boolean Mark(DocumentModel doc, CoreSession session) throws ClientException {
        try {
            if (!notificationService.canBeMarked(doc, session)) {
                return false;
            }
            Page page = getPage(doc, session);
            return setAstoBeNotified(page.getDocument(), session);
        } catch (Exception e) {
            return false;
        }
    }

    private Page getPage(DocumentModel doc, CoreSession session) throws Exception {
        return notificationService.getRelatedPage(doc, session);
    }
    
    private boolean setAstoBeNotified(DocumentModel doc, CoreSession session) throws ClientException {
        try {
            Tools.getAdapter(MailNotification.class, doc, session).setAsToBeNotified();
            return true;
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return false;
//        if Tools.getAdapter(MailNotification.class, doc, session).isToBeNotified()) {
//            return false;
//        }
//        Tools.getAdapter(MailNotification.class, doc, session).setAsToBeNotified();
//        session.saveDocument(doc);
//        return true;
    }
    
    private PageNotificationService getNotificationService() throws Exception {
        if (notificationService == null) {
            notificationService = Framework.getService(PageNotificationService.class);
        }
        return notificationService;
    }

    @SuppressWarnings("unused")
	@Deprecated
    private boolean MarkParent(DocumentModel doc, CoreSession session) throws Exception {
        Page page = getPage(doc, session);
        if (page == null || !State.PUBLISH.getState().equals(page.getDocument().getCurrentLifeCycleState())) {
            return false;
        }
        DocumentModel parentDocument = session.getParentDocument(doc.getRef());
        setAstoBeNotified(parentDocument, session);
        return true;
    }

    @SuppressWarnings("unused")
	@Deprecated
    private boolean MarkParentPage(DocumentModel doc, CoreSession session) throws Exception {
        Page page = getPage(doc, session);
        if (page == null || !State.PUBLISH.getState().equals(page.getDocument().getCurrentLifeCycleState())) {
            return false;
        }
        Page parentPage = Tools.getAdapter(SiteDocument.class, doc, session).getParentPage();
        if (parentPage != null) {
            setAstoBeNotified(parentPage.getDocument(), session);
            return true;
        }
        return false;
        
    }

}
