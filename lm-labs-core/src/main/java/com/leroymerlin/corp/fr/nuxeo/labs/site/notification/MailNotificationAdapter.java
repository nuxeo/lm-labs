package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.api.Framework;

// TODO unit tests
public class MailNotificationAdapter implements MailNotification {

    protected final DocumentModel doc;
    
    private static final Log LOG = LogFactory.getLog(MailNotificationAdapter.class);

    public MailNotificationAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public void setAsToBeNotified() throws Exception {
        PageNotificationService notificationService = Framework.getService(PageNotificationService.class);
        notificationService.markForNotification(doc);
    }

}
