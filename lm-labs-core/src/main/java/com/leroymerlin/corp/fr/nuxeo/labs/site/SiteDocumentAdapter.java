package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public class SiteDocumentAdapter extends LabsAdapterImpl implements SiteDocument {


    public SiteDocumentAdapter(DocumentModel document) {
		super(document);
	}

	@Override
    public Page getParentPage() throws ClientException {
        DocumentModel parentDocument = doc;
        CoreSession session = getSession();
        while (!LabsSiteConstants.Docs.DEFAULT_DOMAIN.type()
                .equals(parentDocument.getType())) {
            try {
				parentDocument = session.getParentDocument(parentDocument.getRef());
                if (parentDocument.hasSchema(Schemas.PAGE.getName()) && !LabsSiteConstants.Docs.SITE.type().equals(parentDocument.getType())) {
                    return Tools.getAdapter(Page.class, parentDocument, session);
                }
            } catch (ClientException e) {
                break;
            }
        }
        return Tools.getAdapter(Page.class, doc, session);
    }

    @Override
    public LabsSite getSite() throws ClientException, IllegalArgumentException {
    	CoreSession session = getSession();
        DocumentModel parent = doc;
        if (Docs.SITE.type()
                .equals(parent.getType())) {
            return Tools.getAdapter(LabsSite.class, parent, session);
        }
        while (!parent.getParentRef()
                .equals(session.getRootDocument()
                        .getRef())) {
            parent = session.getDocument(parent.getParentRef());
            if (Docs.SITE.type()
                    .equals(parent.getType())) {
                return Tools.getAdapter(LabsSite.class, parent, session);
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
            Page adapter = Tools.getAdapter(Page.class, child, getSession());
            if (adapter != null) {
                pages.add(adapter);
            }
        }
        return pages;
    }

    @Override
    public DocumentModelList getChildrenPageDocuments() throws ClientException {
        return LabsSiteUtils.getChildrenPageDocuments(doc, getSession());
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public Collection<Page> getChildrenNavigablePages(final String userName) throws ClientException {
    	List<Page> pages = new ArrayList<Page>();
    	LabsSite site = getSite();
    	if (!site.isAdministrator(userName)) {
    		pages.addAll(CollectionUtils.select(getChildrenPages(), new Predicate() {
    			@Override
    			public boolean evaluate(Object input) {
    				Page page = (Page) input;
    				if (!Docs.pageDocs().contains(Docs.fromString(page.getDocument().getType()))) {
    					return false;
    				}
    				try {
    					boolean result = true;
    					if (!page.isContributor(userName)){
    						result = page.isVisible() && !page.isHiddenInNavigation();
    					}
    					return (result && !page.isDeleted());
    				} catch (ClientException e) {
    					return false;
    				}
    			}
    		}));
    	} else {
    		for (DocumentModel child : getChildrenPageDocuments()) {
    			Page adapter = Tools.getAdapter(Page.class, child, getSession());
    			if (adapter != null && Docs.pageDocs().contains(Docs.fromString(adapter.getDocument().getType()))) {
    				pages.add(adapter);
    			}
    		}
    	}
    	return pages;
    }

}
