package com.leroymerlin.corp.fr.nuxeo.labs.base;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.DocumentBlobHolder;

public class LabsElementTemplateBlobHolder extends DocumentBlobHolder {

	public LabsElementTemplateBlobHolder(DocumentModel doc, String xPath) {
		super(doc, xPath);
	}

}
