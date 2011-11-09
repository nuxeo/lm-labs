package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

// TODO unit tests
public class MailNotificationAdapter implements MailNotification {

    private final DocumentModel doc;

    public MailNotificationAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public void setAsNotified() throws ClientException {
        ensureFacet();
        doc.setPropertyValue(MailNotification.PROPERTY_NOTIFIED, Calendar.getInstance());
        doc.setPropertyValue(MailNotification.PROPERTY_ISNOTIFIED, Boolean.TRUE);
    }

    @Override
    public boolean isNotified() throws ClientException {
        if (!hasFacet()) {
            return false; 
        }
        return (Boolean) doc.getPropertyValue(MailNotification.PROPERTY_ISNOTIFIED);
    }

    @Override
    public Calendar getNotificationDate() throws ClientException {
        if (!hasFacet()) {
            return null; 
        }
        return (Calendar) doc.getPropertyValue(MailNotification.PROPERTY_NOTIFIED);
    }

    @Override
    public void reset() throws ClientException {
        if (!hasFacet()) {
            return;
        }
        ensureFacet();
        doc.setPropertyValue(MailNotification.PROPERTY_NOTIFIED, null);
        doc.setPropertyValue(MailNotification.PROPERTY_ISNOTIFIED, Boolean.FALSE);
    }
    
    @Override
    public boolean hasFacet() {
        return doc.hasFacet(FACET_NAME);
    }
    
    protected boolean ensureFacet() {
        boolean facetAdded = false;
        if (!hasFacet()) {
            doc.addFacet(FACET_NAME);
            facetAdded = true; 
        }
        return facetAdded;
    }

}
