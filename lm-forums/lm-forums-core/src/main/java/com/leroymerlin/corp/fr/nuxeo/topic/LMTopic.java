package com.leroymerlin.corp.fr.nuxeo.topic;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;

public interface LMTopic extends Page {

	public void addComment(CoreSession session, String comment) throws ClientException;

	public List<DocumentModel> getComments() throws ClientException;
	
	public Integer getNbComments() throws ClientException;
}
