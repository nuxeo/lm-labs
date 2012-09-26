package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.FacetNames;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;

public class LabsPageCustomViewAdapter implements LabsPageCustomView {

    protected DocumentModel doc;
    
    private static final String CONTENTVIEW_PROPERTY_NAME = Schemas.LABSCONTENTVIEW.prefix() + ":contentView";

    public LabsPageCustomViewAdapter(DocumentModel document) {
        this.doc = document;
    }

    @Override
    public String getContentView() throws ClientException {
        return (String) doc.getPropertyValue(CONTENTVIEW_PROPERTY_NAME);
    }

    @Override
    public boolean setCustomView(String view) throws ClientException {
        if (PAGE_DEFAULT_VIEW.equals(view)) {
            removeFacet();
        } else {
            ensureFacet();
            // TODO validation
            doc.setPropertyValue(CONTENTVIEW_PROPERTY_NAME, view);
        }
        return true;
    }

//    @Override
//    public void setCustomView(LabsCustomView view) throws ClientException {
//        if (PAGE_DEFAULT_VIEW.equals(view.getContentView())) {
//            removeFacet();
//        } else {
//            ensureFacet();
//            doc.setPropertyValue(CONTENTVIEW_PROPERTY_NAME, view.getContentView());
//        }
//    }
    
    private void ensureFacet() {
        if (!doc.getFacets().contains(FacetNames.LABSPAGECUSTOMVIEW)) {
            doc.addFacet(FacetNames.LABSPAGECUSTOMVIEW);
        }
    }
    
    private void removeFacet() {
        if (doc.getFacets().contains(FacetNames.LABSPAGECUSTOMVIEW)) {
            doc.removeFacet(FacetNames.LABSPAGECUSTOMVIEW);
        }
    }

}
