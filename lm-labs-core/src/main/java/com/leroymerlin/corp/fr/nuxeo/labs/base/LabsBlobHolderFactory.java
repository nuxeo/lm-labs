package com.leroymerlin.corp.fr.nuxeo.labs.base;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.BlobHolderFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNewsBlobHolder;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class LabsBlobHolderFactory implements BlobHolderFactory {

	@Override
	public BlobHolder getBlobHolder(DocumentModel doc) {
        if(LabsSiteConstants.Docs.SITE.type().equals(doc.getType()) 
        		|| LabsSiteConstants.Docs.HTMLPAGE.type().equals(doc.getType())
        		|| LabsSiteConstants.Docs.PAGEBLOCS.type().equals(doc.getType())
        		|| LabsSiteConstants.Docs.PAGENEWS.type().equals(doc.getType())
        		|| LabsSiteConstants.Docs.PAGECLASSEUR.type().equals(doc.getType())
        		|| LabsSiteConstants.Docs.PAGELIST.type().equals(doc.getType())
        		|| LabsSiteConstants.Docs.PAGEFORUM.type().equals(doc.getType())) {
            return new LabsElementTemplateBlobHolder(doc, "let:preview");
        }
        if(LabsSiteConstants.Docs.LABSNEWS.type().equals(doc.getType())) {
            return new LabsNewsBlobHolder(doc, "");
        }
        return null;
	}

}
