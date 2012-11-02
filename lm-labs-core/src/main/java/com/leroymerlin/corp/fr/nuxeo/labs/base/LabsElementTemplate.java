package com.leroymerlin.corp.fr.nuxeo.labs.base;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;

public interface LabsElementTemplate {
	
	void setElementTemplate (boolean isTemplate) throws ClientException;
	
	boolean isElementTemplate () throws ClientException;

    void setElementPreview(Blob blob) throws ClientException;

    Blob getElementPreview() throws ClientException;
    
    boolean hasElementPreview() throws ClientException;

}
