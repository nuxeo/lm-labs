package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

public class MailNotificationAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> klass) {
        return new MailNotificationAdapter(doc);
    }

}
