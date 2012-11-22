package com.leroymerlin.corp.fr.nuxeo.labs.site.nav;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;

public interface PageNav extends Page {

	List<String> getTags () throws ClientException;
	
	void setTags(List<String> tags) throws ClientException;

	List<Page> getTaggetPages() throws ClientException;
}
