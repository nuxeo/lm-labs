package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;

public class SiteDocumentAdapter implements SiteDocument {

    private final DocumentModel doc;

    public SiteDocumentAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public Page getPage() throws ClientException {
        DocumentModel parentDocument = doc;
        while (!LabsSiteConstants.Docs.DEFAULT_DOMAIN.type().equals(
                parentDocument.getType())) {
            try {
                parentDocument = doc.getCoreSession().getParentDocument(
                        parentDocument.getRef());
                if (parentDocument.hasSchema(Schemas.PAGE.getName())) {
                    return parentDocument.getAdapter(Page.class);
                }
            } catch (ClientException e) {
                break;
            }
        }
        return doc.getAdapter(Page.class);
    }

}
