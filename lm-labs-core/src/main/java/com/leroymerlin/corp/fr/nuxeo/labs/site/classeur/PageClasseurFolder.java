package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SubDocument;

public interface PageClasseurFolder extends SubDocument {
    DocumentModel getDocument();

    String getTitle() throws ClientException;
    
    boolean setAsDeleted() throws ClientException;
    
    boolean hide(DocumentModel file) throws ClientException;
    
    boolean show(DocumentModel file) throws ClientException;
}
