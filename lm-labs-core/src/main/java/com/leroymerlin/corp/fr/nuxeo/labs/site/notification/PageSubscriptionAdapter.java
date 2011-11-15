package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ec.notification.NotificationConstants;
import org.nuxeo.ecm.platform.notification.api.NotificationManager;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

public class PageSubscriptionAdapter implements PageSubscription {

    private final DocumentModel doc;

    public PageSubscriptionAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public String getNotificationName() {
        return "Page modified";
    }

    @Override
    public void subscribe(String principal) throws Exception {
        if (!isSubscribed(principal)) {
            NotificationManager notificationService = Framework.getService(NotificationManager.class);
            UserManager userManager = Framework.getService(UserManager.class);
            notificationService.addSubscription(NotificationConstants.USER_PREFIX + principal, getNotificationName(), doc, true, userManager.getPrincipal(principal), getNotificationName());
        }
    }

    @Override
    public void unsubscribe(String principal) throws Exception {
        NotificationManager notificationService = Framework.getService(NotificationManager.class);
        notificationService.removeSubscription(NotificationConstants.USER_PREFIX + principal, getNotificationName(), doc.getId());
    }

    @Override
    public boolean isSubscribed(String principal) throws Exception {
        NotificationManager notificationService = Framework.getService(NotificationManager.class);
        List<String> subscriptions = notificationService.getSubscriptionsForUserOnDocument(NotificationConstants.USER_PREFIX + principal, doc.getId());
        if (subscriptions.contains(getNotificationName())) {
            return true;
        }
        return false;
    }

}
