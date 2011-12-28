package com.leroymerlin.corp.fr.nuxeo.piwik;

import org.nuxeo.ecm.core.api.ClientException;

public interface Piwik {

	void setId(String id) throws ClientException;
	
	String getId() throws ClientException;
	
}
