package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

public interface Page extends LabsBase {

    public String getPath(CoreSession session) throws ClientException;
    
    boolean isCommentable() throws ClientException;
    
    void setCommentable(boolean isCommentable) throws ClientException;
    
    List<String> getNotDisplayableParameters() throws ClientException;
    
    void setNotDisplayableParameters(List<String> fields) throws ClientException;
    
    boolean isDisplayable(String fieldName) throws ClientException;
    
    Calendar getLastNotified(CoreSession session) throws ClientException;

	public int getElementsPerPage() throws ClientException;
	
	void setElementsPerPage(int elementsPerPage) throws ClientException;
}
