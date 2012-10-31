package com.leroymerlin.corp.fr.nuxeo.labs.site.customview;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.FacetNames;

public class LabsPageCustomViewAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> klass) {
        if (doc.hasFacet(FacetNames.LABSPAGECUSTOMVIEW)) {
            return new LabsPageCustomViewAdapter(doc);
        }
        return new LabsPageDefaultContentView(doc);
    }

}
