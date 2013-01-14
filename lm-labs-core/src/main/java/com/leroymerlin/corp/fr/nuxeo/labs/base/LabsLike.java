package com.leroymerlin.corp.fr.nuxeo.labs.base;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface LabsLike {

    boolean isLiked(final String username) throws ClientException;
    
    LabsLike like(final String username) throws ClientException;
    
    List<String> getUsersLiked() throws ClientException;
    
    long getLikesCount() throws ClientException;
    
    DocumentModel getDocument() throws ClientException;
}
