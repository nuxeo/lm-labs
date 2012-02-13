package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.BlobHolderFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSiteBlobHolder;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNewsBlobHolder;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class LabsBlobHolderFactory implements BlobHolderFactory {

	@Override
	public BlobHolder getBlobHolder(DocumentModel doc) {
        if(LabsSiteConstants.Docs.SITE.type().equals(doc.getType())) {
            return new LabsSiteBlobHolder(doc, "labssite:siteTemplatePreview");
        }
        if(LabsSiteConstants.Docs.LABSNEWS.type().equals(doc.getType())) {
            return new LabsNewsBlobHolder(doc, "");
        }
        return null;
	}

}
