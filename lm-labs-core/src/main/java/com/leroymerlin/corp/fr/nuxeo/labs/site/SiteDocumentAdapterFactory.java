package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

public class SiteDocumentAdapterFactory implements DocumentAdapterFactory{

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> klass) {
        if(doc != null) {
            return new SiteDocumentAdapter(doc);
        }
        return null;
    }

}
