package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class LabsNewsAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        if(LabsSiteConstants.Docs.LABSNEWS.type().equals(doc.getType())) {
            return new LabsNewsAdapter(doc);
        }
        return null;
    }

}
