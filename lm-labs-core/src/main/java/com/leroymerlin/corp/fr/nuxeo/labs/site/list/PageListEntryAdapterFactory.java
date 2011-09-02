package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class PageListEntryAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        if(LabsSiteConstants.Docs.PAGELISTENTRY.type().equals(doc.getType())) {
            return new PageListEntryAdapter(doc);
        }
        return null;
    }
}
