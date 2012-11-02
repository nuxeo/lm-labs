package com.leroymerlin.corp.fr.nuxeo.labs.site.customview;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

public class LabsPageDefaultContentView extends LabsPageCustomViewAdapter implements LabsPageCustomView {

    public LabsPageDefaultContentView(DocumentModel document) {
        super(document);
    }

    @Override
    public String getContentView() throws ClientException {
        return LabsCustomView.PAGE_DEFAULT_VIEW;
    }

}
