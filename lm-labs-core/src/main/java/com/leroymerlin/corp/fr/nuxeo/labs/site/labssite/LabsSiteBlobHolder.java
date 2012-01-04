package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.DocumentBlobHolder;

public class LabsSiteBlobHolder extends DocumentBlobHolder {

	public LabsSiteBlobHolder(DocumentModel doc, String xPath) {
		super(doc, xPath);
	}

}
