package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class LabsThemeAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        if(Docs.LABSTHEME.type().equals(doc.getType())) {
            return new LabsThemeAdapter(doc);
        }
        return null;
    }

}
