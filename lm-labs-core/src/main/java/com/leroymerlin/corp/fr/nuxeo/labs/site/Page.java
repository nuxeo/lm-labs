package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;

public interface Page extends LabsBase, LabsCommentable {

    public String getPath() throws ClientException;
    
    List<String> getNotDisplayableParameters() throws ClientException;
    
    void setNotDisplayableParameters(List<String> fields) throws ClientException;
    
    boolean isDisplayable(String fieldName) throws ClientException;
    
    Calendar getLastNotified() throws ClientException;

	public int getElementsPerPage() throws ClientException;
	
	void setElementsPerPage(int elementsPerPage) throws ClientException;
	
	/* A GARDER
	void setCollapseType(final String collapseType) throws ClientException;

	public void setCollapseType(final CollapseTypes collapseType) throws ClientException;
	
	public String getCollapseType() throws ClientException;
	*/
}
