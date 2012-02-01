package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.ec.notification.NotificationConstants;
import org.nuxeo.ecm.platform.notification.api.NotificationManager;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.NotifNames;

public class PageSubscriptionAdapter implements PageSubscription {

    private final DocumentModel doc;

    public PageSubscriptionAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public List<String> getNotificationNames() {
        return new ArrayList<String>(
                Arrays.asList(
                        NotifNames.PAGE_MODIFIED
                        // TODO
//                        ,NotifNames.PAGE_REMOVED
                        ));
    }

    @Override
    public void subscribe(String principal) throws Exception {
        if (!isSubscribed(principal)) {
            NotificationManager notificationService = Framework.getService(NotificationManager.class);
            UserManager userManager = Framework.getService(UserManager.class);
            for (String name : getNotificationNames()) {
                notificationService.addSubscription(NotificationConstants.USER_PREFIX + principal, name, doc, false, userManager.getPrincipal(principal), name);
            }
        }
    }

    @Override
    public void unsubscribe(String principal) throws Exception {
        NotificationManager notificationService = Framework.getService(NotificationManager.class);
        for (String name : getNotificationNames()) {
            notificationService.removeSubscription(NotificationConstants.USER_PREFIX + principal, name, doc.getId());
        }
    }

    @Override
    public boolean isSubscribed(String principal) throws Exception {
        NotificationManager notificationService = Framework.getService(NotificationManager.class);
        List<String> subscriptions = notificationService.getSubscriptionsForUserOnDocument(NotificationConstants.USER_PREFIX + principal, doc.getId());
        if (subscriptions.containsAll(getNotificationNames())) {
            return true;
        }
        return false;
    }

}
