package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import org.nuxeo.ecm.core.api.ClientException;

public interface MovableElement {
	
	void moveUp(int index) throws ClientException;
	
	void moveDown(int index) throws ClientException;

}
