package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;

public interface LabsTags {

	public void setLabsTags(List<String> tags) throws ClientException;

	public List<String> getLabsTags() throws ClientException;

}
