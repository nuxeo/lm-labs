package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labstemplate.LabsTemplate;

public interface LabsBase {

    public void setTitle(String title) throws PropertyException, ClientException, IllegalArgumentException;

    public String getTitle() throws PropertyException, ClientException;

    public void setDescription(String description) throws PropertyException, ClientException;

    public String getDescription() throws PropertyException, ClientException;

    public DocumentModel getDocument();

    public String[] getAllowedSubtypes() throws ClientException;
    
    boolean isAuthorizedToDisplay() throws ClientException;
    
    boolean isDeleted() throws ClientException;
    
    boolean isVisible() throws ClientException;

    LabsTemplate getTemplate() throws ClientException;

    void addFacetTemplate();
    
    boolean isDraft() throws ClientException;
    
    boolean isAdministrator(String userName) throws ClientException;
    
    boolean isContributor(String userName) throws ClientException;
}
