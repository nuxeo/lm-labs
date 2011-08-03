package com.leroymerlin.corp.fr.nuxeo.labs.site.page;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class PageAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        if (LabsSiteConstants.Docs.fromString(doc.getType()) != null) {
            return new PageAdapter(doc);
        }
        return null;
    }

}
