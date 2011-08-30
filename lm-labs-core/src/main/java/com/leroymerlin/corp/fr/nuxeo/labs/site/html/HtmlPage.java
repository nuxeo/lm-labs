package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface HtmlPage {

	void setTitle(String string) throws ClientException;

	void setDescription(String string) throws ClientException;

	String getTitle()  throws ClientException;

	String getDescription() throws ClientException;

	DocumentModel getDocument();

	List<HtmlSection> getSections() throws ClientException;

	HtmlSection addSection() throws ClientException;

}
