package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.EventNames;

public class SubDocumentsMailNotificationAdapter extends MailNotificationAdapter {

    private static final Log LOG = LogFactory.getLog(SubDocumentsMailNotificationAdapter.class);

    public SubDocumentsMailNotificationAdapter(DocumentModel doc) {
        super(doc);
    }

    @Override
    public void fireNotificationEvent() throws Exception {
        DocumentEventContext ctx = getEventContext();
        List<DocumentModel> subDocuments = getSubDocumentsToNotify();
        if (subDocuments.isEmpty()) {
            LOG.warn("No sub documents to notify for " + doc.getType() + " " + doc.getPathAsString());
            return;
        }
        for (DocumentModel subDocument : subDocuments) {
            subDocument.getAdapter(MailNotification.class).setAsNotified();
        }
        LOG.debug("firing event " + EventNames.PAGE_MODIFIED + " for " + doc.getPathAsString());
        EventProducer evtProducer = Framework.getService(EventProducer.class);
        evtProducer.fireEvent(ctx.newEvent(EventNames.PAGE_MODIFIED));
        for (DocumentModel subDocument : subDocuments) {
            subDocument = doc.getCoreSession().saveDocument(subDocument);
        }
    }

}
