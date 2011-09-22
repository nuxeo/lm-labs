package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class PageListLineAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        if(LabsSiteConstants.Docs.PAGELIST_LINE.type().equals(doc.getType())) {
            return new PageListLineAdapter(doc);
        }
        return null;
    }
}
