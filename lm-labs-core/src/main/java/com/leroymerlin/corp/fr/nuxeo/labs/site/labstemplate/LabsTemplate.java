package com.leroymerlin.corp.fr.nuxeo.labs.site.labstemplate;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface LabsTemplate {

    String getTemplateName() throws ClientException;
    
    void setTemplateName(String name) throws ClientException;

    DocumentModel getDocument();

    String getDocumentTemplateName() throws ClientException;
}
