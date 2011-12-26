package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;

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
    public Page getParentPage() throws ClientException {
        DocumentModel parentDocument = doc;
        while (!LabsSiteConstants.Docs.DEFAULT_DOMAIN.type()
                .equals(parentDocument.getType())) {
            try {
                parentDocument = doc.getCoreSession()
                        .getParentDocument(parentDocument.getRef());
                if (parentDocument.hasSchema(Schemas.PAGE.getName()) && !LabsSiteConstants.Docs.SITE.type().equals(parentDocument.getType())) {
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
                + "' is not located in a site.");
    }

    @Override
    public String getParentPagePath() throws ClientException {
        return getParentPage().getPath();
    }

    @Override
    public String getResourcePath() throws ClientException {
        LabsSite site = getSite();
        String endUrl = doc.getPathAsString();
        if (endUrl.contains(site.getTree().getPathAsString())) {
            endUrl = endUrl.replace(site.getTree().getPathAsString(), "");
        } else {
            endUrl = endUrl.replace(site.getDocument().getPathAsString(), "");
        }
        return site.getURL() + endUrl;
    }

    @Override
    public BlobHolder getBlobHolder() {
        return doc.getAdapter(BlobHolder.class);
    }

    @Override
    public Collection<Page> getChildrenPages() throws ClientException {
        List<Page> pages = new ArrayList<Page>();
        for (DocumentModel child : getChildrenPageDocuments()) {
            Page adapter = child.getAdapter(Page.class);
            if (adapter != null) {
                pages.add(adapter);
            }
        }
        return pages;
    }

    @Override
    public DocumentModelList getChildrenPageDocuments() throws ClientException {
        @SuppressWarnings("serial")
        DocumentModelList children = doc.getCoreSession().getChildren(doc.getRef(), null, new Filter() {
            @Override
            public boolean accept(DocumentModel document) {
                return Docs.pageDocs().contains(Docs.fromString(document.getType()));
            }}, null);
        return children;
    }

}
