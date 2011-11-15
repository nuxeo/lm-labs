package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageSubscriptionAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> Klass) {
        if (Docs.PAGENEWS.type().equals(doc.getType())) {
            return new PageNewsSubscriptionAdapter(doc);
        } else if (Docs.notifiableDocs().contains(Docs.fromString(doc.getType()))) {
            return new PageSubscriptionAdapter(doc);
        }
        return null;
    }

}
