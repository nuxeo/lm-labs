package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.DefaultComponent;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.EventNames;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.State;

public class PageNotificationServiceImpl extends DefaultComponent implements PageNotificationService {

    private static final Log LOG = LogFactory.getLog(PageNotificationServiceImpl.class);

    @Override
    public boolean markForNotification(DocumentModel doc) throws ClientException {
        LoginContext loginContext = null;
        CoreSession session = null;
        Page page = getPage(doc);
        if (page == null || !State.PUBLISH.getState().equals(page.getDocument().getCurrentLifeCycleState())) {
            return false;
        }
        try {
            loginContext = Framework.login();
            session = openSession(doc.getRepositoryName());
            DocumentModel notif;
            PathRef ref = new PathRef(page.getDocument().getPathAsString() + "/" + Docs.NOTIFACTIVITIES.docName());
            if (!session.exists(ref)) {
                notif = session.createDocumentModel(page.getDocument().getPathAsString(), Docs.NOTIFACTIVITIES.docName(), Docs.NOTIFACTIVITIES.type());
                notif.setPropertyValue(PROPERTY_TONOTIFY, Boolean.TRUE);
                notif = session.createDocument(notif);
            } else {
                notif = session.getDocument(ref);
                notif.setPropertyValue(PROPERTY_TONOTIFY, Boolean.TRUE);
                notif = session.saveDocument(notif);
            }
            session.save();
        } catch (LoginException e) {
            LOG.error(e, e);
        } finally {
            closeCoreSession(loginContext, session);
        }
        return true;
    }

    @Override
    public void notifyPagesOfSite(DocumentModel site) throws ClientException {
        LoginContext loginContext = null;
        CoreSession session = null;
        try {
            loginContext = Framework.login();
            session = openSession(site.getRepositoryName());
            List<DocumentModel> pages = getMarkedPagesOfSite(site, session);
            for (DocumentModel page : pages) {
                if (State.PUBLISH.getState().equals(page.getCurrentLifeCycleState())) {
                    notifyPage(page.getAdapter(Page.class));
                }
                unmarkForNotification(page);
            }
            session.save();
        } catch (LoginException e) {
            LOG.error(e, e);
        } finally {
            closeCoreSession(loginContext, session);
        }
    }

    @Override
    public boolean notifyPage(Page page) throws ClientException {
        return notifyPageEvent(page, EventNames.PAGE_MODIFIED);
    }

    @Override
    public boolean notifyPageEvent(Page page, String eventName) throws ClientException {
        try {
            fireEvent(page, eventName);
            return true;
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return false;
    }

    private void unmarkForNotification(DocumentModel page) throws ClientException {
        Page adapter = page.getAdapter(Page.class);
        if (adapter == null) {
            return;
        }
        CoreSession session = page.getCoreSession();
        PathRef ref = new PathRef(page.getPathAsString() + "/" + Docs.NOTIFACTIVITIES.docName());
        if (session.exists(ref)) {
//            session.removeDocument(ref);
            DocumentModel notif = session.getDocument(ref);
            notif.setPropertyValue(PROPERTY_NOTIFIED, Calendar.getInstance());
            notif.setPropertyValue(PROPERTY_TONOTIFY, Boolean.FALSE);
            notif = session.saveDocument(notif);
        }
    }

    public List<DocumentModel> getMarkedPagesOfSite(DocumentModel site, CoreSession session) throws ClientException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(Docs.NOTIFACTIVITIES.type())
            .append(" WHERE ")
            .append(NXQL.ECM_PATH).append(" STARTSWITH '").append(site.getPathAsString()).append("'")
            .append(" AND ")
            .append(PROPERTY_TONOTIFY).append(" = 1")
        ;
        DocumentModelList notifs = session.query(query.toString());
        List<DocumentModel> pages = new ArrayList<DocumentModel>();
        for (DocumentModel notif : notifs) {
            pages.add(session.getDocument(notif.getParentRef()));
        }
        return pages;
    }
    
    private Page getPage(DocumentModel doc) throws ClientException {
        Page page = doc.getAdapter(Page.class);
        if (page == null) {
            page = doc.getAdapter(SiteDocument.class).getParentPage();
        }
        return page;
    }
    
    public void fireEvent(Page page, String eventName) throws Exception {
        DocumentModel doc = page.getDocument();
        DocumentEventContext ctx = new DocumentEventContext(doc.getCoreSession(), doc.getCoreSession().getPrincipal(), doc);
        ctx.setProperty("PageId", doc.getId());
        String loopbackurl = Framework.getProperty("nuxeo.loopback.url");
        ctx.setProperty("baseUrl", loopbackurl);
        ctx.setProperty("siteUrl", (Serializable) doc.getAdapter(SiteDocument.class).getSite().getURL());
        ctx.setProperty("siteTitle", (Serializable) doc.getAdapter(SiteDocument.class).getSite().getTitle());
        ctx.setProperty("pageUrl", doc.getAdapter(Page.class).getPath());
        LOG.debug("firing event " + eventName + " for " + doc.getPathAsString());
        EventProducer evtProducer = Framework.getService(EventProducer.class);
        evtProducer.fireEvent(ctx.newEvent(eventName));
    }

    protected CoreSession openSession(String repositoryName) throws ClientException {
        try {
            RepositoryManager m = Framework.getService(RepositoryManager.class);
            if (repositoryName == null) {
                return m.getDefaultRepository().open();
            } else {
                return m.getRepository(repositoryName).open();
            }
        } catch (Exception e) {
            throw new ClientException("Unable to get session", e);
        }
    }
    
    protected void closeCoreSession(LoginContext loginContext,
            CoreSession session) throws ClientException {
        if (loginContext != null) {
            try {
                loginContext.logout();
            } catch (LoginException e) {
                throw new ClientException(e);
            }
        }
        if (session != null) {
            CoreInstance.getInstance().close(session);
        }
    }

}
