package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.EventNames;

// TODO unit tests
public class MailNotificationAdapter implements MailNotification {

    protected final DocumentModel doc;
    protected List<DocumentModel> subDocuments = null;
    
    private static final Log LOG = LogFactory.getLog(MailNotificationAdapter.class);

    public MailNotificationAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public void setAsNotified() throws ClientException {
        ensureFacet();
        doc.setPropertyValue(MailNotification.PROPERTY_NOTIFIED, Calendar.getInstance());
        doc.setPropertyValue(MailNotification.PROPERTY_TONOTIFY, Boolean.FALSE);
    }

    @Override
    public Calendar getNotificationDate() throws ClientException {
        if (hasFacet()) {
            return (Calendar) doc.getPropertyValue(MailNotification.PROPERTY_NOTIFIED);
        }
        return null; 
    }

    @Override
    public void reset() throws ClientException {
        if (!hasFacet()) {
            return;
        }
        ensureFacet();
        doc.setPropertyValue(MailNotification.PROPERTY_NOTIFIED, null);
        doc.setPropertyValue(MailNotification.PROPERTY_TONOTIFY, Boolean.FALSE);
    }
    
    @Override
    public boolean hasFacet() {
        return doc.hasFacet(FACET_NAME);
    }
    
    protected boolean ensureFacet() {
        boolean facetAdded = false;
        if (!hasFacet()) {
            // TODO maybe check also if a user is susbscribed to this document 
            // before adding the facet, is it worth it ?
            doc.addFacet(FACET_NAME);
            facetAdded = true; 
        }
        return facetAdded;
    }

    @Override
    public void setAsToBeNotified() throws ClientException {
        ensureFacet();
        doc.setPropertyValue(MailNotification.PROPERTY_TONOTIFY, Boolean.TRUE);
    }

    @Override
    public boolean isToBeNotified() throws ClientException {
        if (hasFacet()) {
            return (Boolean) doc.getPropertyValue(MailNotification.PROPERTY_TONOTIFY);
        }
        return false;
    }

    @Override
    public List<DocumentModel> getSubDocumentsToNotify() throws ClientException {
        return subDocuments;
    }
    
    @Override
    public DocumentEventContext getEventContext() throws ClientException {
        DocumentEventContext ctx = new DocumentEventContext(doc.getCoreSession(), doc.getCoreSession().getPrincipal(), doc);
        ctx.setProperty("PageId", doc.getId());
        String loopbackurl = Framework.getProperty("nuxeo.loopback.url");
        ctx.setProperty("baseUrl", loopbackurl);
        ctx.setProperty("siteUrl", (Serializable) doc.getAdapter(SiteDocument.class).getSite().getURL());
        ctx.setProperty("siteTitle", (Serializable) doc.getAdapter(SiteDocument.class).getSite().getTitle());
        ctx.setProperty("pageUrl", doc.getAdapter(Page.class).getPath());
        return ctx;
    }

    @Override
    public void fireNotificationEvent() throws Exception {
        if (!isToBeNotified()) {
            LOG.debug(doc.getType() + " " + doc.getPathAsString() + " not to be notified.");
            return;
        }
        DocumentEventContext ctx = getEventContext();
        LOG.debug("firing event " + EventNames.PAGE_MODIFIED + " for " + doc.getPathAsString());
        EventProducer evtProducer = Framework.getService(EventProducer.class);
        evtProducer.fireEvent(ctx.newEvent(EventNames.PAGE_MODIFIED));
        doc.getAdapter(MailNotification.class).setAsNotified();
        doc.getCoreSession().saveDocument(doc);

    }

}
