package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;

public interface ConfigDisplayPropPage extends LabsBase {

    public static final String DC_TITLE = "dc:title";

    public static final String DC_DESCRIPTION = "dc:description";

    List<String> getNotDisplayableParameters() throws ClientException;
    
    void setNotDisplayableParameters(List<String> fields) throws ClientException;
    
    boolean isDisplayable(String fieldName) throws ClientException;
}
