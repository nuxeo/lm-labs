package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import org.nuxeo.ecm.core.api.DocumentModel;

public class PageNewsSubscriptionAdapter extends PageSubscriptionAdapter {

    public PageNewsSubscriptionAdapter(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getNotificationName() {
        return "News published";
    }

}
