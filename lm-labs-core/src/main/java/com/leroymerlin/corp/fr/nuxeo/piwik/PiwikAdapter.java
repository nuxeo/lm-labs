package com.leroymerlin.corp.fr.nuxeo.piwik;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

public class PiwikAdapter implements Piwik {
	
	public static final String SCHEMA_NAME = "piwik";
	protected static final String PROPERTY_NAME_ID = "piwikId";

    private DocumentModel doc;

	public PiwikAdapter(DocumentModel doc) {
        this.doc = doc;
	}

	@Override
	public void setId(String id) throws ClientException {
		doc.setPropertyValue(getFullName(PROPERTY_NAME_ID), id);
	}

	@Override
	public String getId() throws ClientException {
		return (String) doc.getPropertyValue(getFullName(PROPERTY_NAME_ID));
	}

	private String getFullName(String propertyNameId) {
		return StringUtils.join(new String[]{SCHEMA_NAME, propertyNameId}, ":");
	}
	
}
