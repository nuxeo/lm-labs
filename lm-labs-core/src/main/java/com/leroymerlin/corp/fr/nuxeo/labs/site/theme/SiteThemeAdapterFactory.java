package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class SiteThemeAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        if(Docs.SITETHEME.type().equals(doc.getType())) {
            return new SiteThemeAdapter(doc);
        }
        return null;
    }

}
