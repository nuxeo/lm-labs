package com.leroymerlin.corp.fr.nuxeo.labs.site.like;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

public class LabsLikeAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        return new LabsLikeAdapter(doc);
    }

}
