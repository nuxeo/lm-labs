package com.leroymerlin.corp.fr.nuxeo.labs.site.labstemplate;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.common.core.adapter.SessionAdapter;

public interface LabsTemplate extends SessionAdapter {

    String getTemplateName() throws ClientException;
    
    void setTemplateName(String name) throws ClientException;

    DocumentModel getDocument();

    String getDocumentTemplateName() throws ClientException;
}
