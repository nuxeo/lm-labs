package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;

public interface Page {

    public void setTitle(String title) throws PropertyException, ClientException, IllegalArgumentException;

    public String getTitle() throws PropertyException, ClientException;

    public void setDescription(String description) throws PropertyException, ClientException;

    public String getDescription() throws PropertyException, ClientException;

    public void setCommentaire(String description) throws PropertyException, ClientException;

    public String getCommentaire() throws PropertyException, ClientException;

    public DocumentModel getDocument();

    public String getPath() throws ClientException;

    public String[] getAllowedSubtypes() throws ClientException;

    void publish() throws ClientException;
    
    void draft() throws ClientException;
    
    boolean isVisible() throws ClientException;
    
    void delete() throws ClientException;
    
    void undelete() throws ClientException;
}
