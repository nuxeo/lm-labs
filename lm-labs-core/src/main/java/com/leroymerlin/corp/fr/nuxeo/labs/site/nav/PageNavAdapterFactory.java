package com.leroymerlin.corp.fr.nuxeo.labs.site.nav;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

public class PageNavAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> klass) {
        if (klass.equals(PageNav.class)) {
            return new PageNavAdapter(doc);
        }
        return null;
    }

}
