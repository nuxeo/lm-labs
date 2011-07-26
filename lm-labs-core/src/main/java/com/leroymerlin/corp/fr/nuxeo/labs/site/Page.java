package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.model.PropertyException;

public interface Page {

    public void setTitle(String title) throws PropertyException, ClientException, IllegalArgumentException;
    
    public String getTitle() throws PropertyException, ClientException;
    
    public void SetDescription(String description) throws PropertyException, ClientException;
    
    public String getDescription() throws PropertyException, ClientException;
    
}
