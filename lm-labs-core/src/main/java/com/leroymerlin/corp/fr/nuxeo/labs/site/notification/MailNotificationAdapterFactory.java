package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class MailNotificationAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> klass) {
        if (Docs.PAGELIST.type().equals(doc.getType())) {
            return new PageListMailNotificationAdapter(doc);
        } else if (Docs.PAGECLASSEUR.type().equals(doc.getType())) {
            return new PageClasseurMailNotificationAdapter(doc);
        } else if (Docs.PAGENEWS.type().equals(doc.getType())) {
            return new PageNewsMailNotificationAdapter(doc);
        } else {
            return new MailNotificationAdapter(doc);
        }
    }

}
