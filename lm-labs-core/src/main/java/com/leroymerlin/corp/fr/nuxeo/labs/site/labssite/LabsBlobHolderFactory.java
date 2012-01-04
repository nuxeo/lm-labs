package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.BlobHolderFactory;

public class LabsBlobHolderFactory implements BlobHolderFactory {

	@Override
	public BlobHolder getBlobHolder(DocumentModel doc) {
		return new LabsSiteBlobHolder(doc, "labssite:siteTemplatePreview");
	}

}
