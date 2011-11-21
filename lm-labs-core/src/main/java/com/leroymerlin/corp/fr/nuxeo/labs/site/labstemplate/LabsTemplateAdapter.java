package com.leroymerlin.corp.fr.nuxeo.labs.site.labstemplate;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;

public class LabsTemplateAdapter implements LabsTemplate {

    private final DocumentModel doc;

    public LabsTemplateAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    public DocumentModel getDocument() {
        return doc;
    }

    @Override
    public String getTemplateName() throws ClientException {
        return (String) doc.getPropertyValue(Schemas.LABSTEMPLATE.prefix() + ":name");
    }

    @Override
    public void setTemplateName(String name) throws ClientException {
        doc.setPropertyValue(Schemas.LABSTEMPLATE.prefix() + ":name", name);       
    }

}
