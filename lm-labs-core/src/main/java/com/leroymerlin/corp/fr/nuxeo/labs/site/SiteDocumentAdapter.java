package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;

public class SiteDocumentAdapter implements SiteDocument {

    private final DocumentModel doc;

    public SiteDocumentAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public Page getPage() throws ClientException {
        DocumentModel parentDocument = doc;
        while (!LabsSiteConstants.Docs.DEFAULT_DOMAIN.type()
                .equals(parentDocument.getType())) {
            try {
                parentDocument = doc.getCoreSession()
                        .getParentDocument(parentDocument.getRef());
                if (parentDocument.hasSchema(Schemas.PAGE.getName())) {
                    return parentDocument.getAdapter(Page.class);
                }
            } catch (ClientException e) {
                break;
            }
        }
        return doc.getAdapter(Page.class);
    }

    @Override
    public LabsSite getSite() throws ClientException {
        CoreSession session = doc.getCoreSession();
        DocumentModel parent = doc;
        if (Docs.SITE.type()
                .equals(parent.getType())) {
            return parent.getAdapter(LabsSite.class);
        }
        while (!parent.getParentRef()
                .equals(session.getRootDocument()
                        .getRef())) {
            parent = session.getDocument(parent.getParentRef());
            if (Docs.SITE.type()
                    .equals(parent.getType())) {
                return parent.getAdapter(LabsSite.class);
            }
        }
        throw new IllegalArgumentException("document '" + doc.getPathAsString()
                + "' is not lacated in a site.");
    }

}
