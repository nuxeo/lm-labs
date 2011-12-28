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

public class PageActivitiesEventListener extends PageNotifier implements EventListener {

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
                    processed = Mark(doc);
                } else if (markParentPageDocs.contains(doc.getType())
                        || markParentDocs.contains(doc.getType())
                        ) {
                    processed = Mark(doc);
                }
            } else if (DocumentEventTypes.DOCUMENT_CREATED.equals(eventName)) {
                Page parentPage = doc.getAdapter(SiteDocument.class).getParentPage();
                if (parentPage == null) {
                    return;
                }
                if (markParentPageDocs.contains(doc.getType())
                        || markParentDocs.contains(doc.getType())
                        ) {
                    processed = Mark(doc);
                }
            } else if (DocumentEventTypes.ABOUT_TO_REMOVE.equals(eventName)) {
                try {
                    Page parentPage = notificationService.getRelatedPage(doc);
                    if (parentPage == null) {
                        return;
                    }
                } catch (Exception e) {
                    LOG.error(e, e);
                    return;
                }
                if (Docs.pageDocs().contains(Docs.fromString(doc.getType()))) {
                    processed = notifyPage(doc, EventNames.PAGE_REMOVED);
                } else {
                    processed = Mark(doc);

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

    private boolean notifyPage(DocumentModel doc, String eventName) throws ClientException {
        try {
            if (!notificationService.canBeMarked(doc)) {
                return false;
            }
            Page page = getPage(doc);
            notificationService.notifyPageEvent(page, eventName);
            return true;
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return false;
    }

    private boolean Mark(DocumentModel doc) throws ClientException {
        try {
            if (!notificationService.canBeMarked(doc)) {
                return false;
            }
            Page page = getPage(doc);
            return setAstoBeNotified(page.getDocument());
        } catch (Exception e) {
            return false;
        }
    }

    private Page getPage(DocumentModel doc) throws Exception {
        return notificationService.getRelatedPage(doc);
    }
    
    private boolean setAstoBeNotified(DocumentModel doc) throws ClientException {
        try {
            doc.getAdapter(MailNotification.class).setAsToBeNotified();
            return true;
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return false;
//        if (doc.getAdapter(MailNotification.class).isToBeNotified()) {
//            return false;
//        }
//        doc.getAdapter(MailNotification.class).setAsToBeNotified();
//        doc.getCoreSession().saveDocument(doc);
//        return true;
    }
    
    private PageNotificationService getNotificationService() throws Exception {
        if (notificationService == null) {
            notificationService = Framework.getService(PageNotificationService.class);
        }
        return notificationService;
    }

    @Deprecated
    private boolean MarkParent(DocumentModel doc) throws Exception {
        Page page = getPage(doc);
        if (page == null || !State.PUBLISH.getState().equals(page.getDocument().getCurrentLifeCycleState())) {
            return false;
        }
        CoreSession session = doc.getCoreSession();
        DocumentModel parentDocument = session.getParentDocument(doc.getRef());
        setAstoBeNotified(parentDocument);
        return true;
    }

    @Deprecated
    private boolean MarkParentPage(DocumentModel doc) throws Exception {
        Page page = getPage(doc);
        if (page == null || !State.PUBLISH.getState().equals(page.getDocument().getCurrentLifeCycleState())) {
            return false;
        }
        Page parentPage = doc.getAdapter(SiteDocument.class).getParentPage();
        if (parentPage != null) {
            setAstoBeNotified(parentPage.getDocument());
            return true;
        }
        return false;
        
    }

}
