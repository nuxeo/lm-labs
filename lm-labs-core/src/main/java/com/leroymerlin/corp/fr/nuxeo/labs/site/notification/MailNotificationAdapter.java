package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.base.LabsAdapterImpl;

// TODO unit tests
public class MailNotificationAdapter extends LabsAdapterImpl implements MailNotification {

//    private static final Log LOG = LogFactory.getLog(MailNotificationAdapter.class);

    public MailNotificationAdapter(DocumentModel doc) {
        super(doc);
    }

    @Override
    public void setAsToBeNotified() throws Exception {
        PageNotificationService notificationService = Framework.getService(PageNotificationService.class);
        notificationService.markForNotification(doc);
    }

    @Override
    public Calendar getLastNotified() throws Exception {
        PageNotificationService notificationService = Framework.getService(PageNotificationService.class);
        return notificationService.getLastNotified(doc, getSession());
    }

}
