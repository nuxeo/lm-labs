package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;

import com.leroymerlin.corp.fr.nuxeo.labs.base.LabsBase;
import com.leroymerlin.corp.fr.nuxeo.labs.base.LabsCommentable;
import com.leroymerlin.corp.fr.nuxeo.labs.base.LabsLike;
import com.leroymerlin.corp.fr.nuxeo.labs.base.LabsTags;

public interface Page extends LabsBase, LabsCommentable, LabsTags, LabsLike {

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
	
	void hideInNavigation() throws ClientException;
	
	void showInNavigation() throws ClientException;
	
	boolean isHiddenInNavigation() throws ClientException;
}
