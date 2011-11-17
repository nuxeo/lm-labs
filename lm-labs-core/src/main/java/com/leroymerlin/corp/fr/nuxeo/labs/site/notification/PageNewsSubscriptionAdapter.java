package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.NotifNames;

public class PageNewsSubscriptionAdapter extends PageSubscriptionAdapter {

    public PageNewsSubscriptionAdapter(DocumentModel doc) {
        super(doc);
    }

    @Override
    public List<String> getNotificationNames() {
        return new ArrayList<String>(
                Arrays.asList(
                        NotifNames.NEWS_PUBLISHED
                        ));
    }

}
