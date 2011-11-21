package com.leroymerlin.corp.fr.nuxeo.labs.site.labstemplate;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class LabsTemplateAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        if(doc.hasSchema("page") || Docs.SITE.type().equals(doc.getType())) {
            return new LabsTemplateAdapter(doc);
        }
        return null;
    }

}
