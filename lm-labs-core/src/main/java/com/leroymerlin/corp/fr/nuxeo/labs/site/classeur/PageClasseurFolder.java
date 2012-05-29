package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SubDocument;

public interface PageClasseurFolder extends SubDocument {
    DocumentModel getDocument();

    String getTitle() throws ClientException;
    
    boolean setAsDeleted(CoreSession session) throws ClientException;
    
    boolean hide(DocumentModel file, CoreSession session) throws ClientException;
    
    boolean show(DocumentModel file, CoreSession session) throws ClientException;
}
