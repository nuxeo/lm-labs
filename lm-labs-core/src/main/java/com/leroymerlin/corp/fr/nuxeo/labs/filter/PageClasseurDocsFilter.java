package com.leroymerlin.corp.fr.nuxeo.labs.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.publisher.LabsPublisher;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageClasseurDocsFilter implements Filter {

    private static final Log LOG = LogFactory.getLog(PageClasseurDocsFilter.class);
    private static final long serialVersionUID = 1L;
    
    private CoreSession session;

    @SuppressWarnings("unused")
	private PageClasseurDocsFilter() {}

    public PageClasseurDocsFilter(CoreSession session) {
		super();
		this.session = session;
	}

	@Override
    public boolean accept(DocumentModel doc) {
        try {
            SiteDocument sd = doc.getAdapter(SiteDocument.class);
            boolean isAdmin = sd.getSite(session).isAdministrator(session.getPrincipal().getName(), session);
            DocumentModel parent = session.getParentDocument(doc.getRef());
            DocumentModel grandParent = session.getDocument(parent.getParentRef());
            LabsPublisher publisher = grandParent.getAdapter(LabsPublisher.class);
            boolean filter = isAdmin || (publisher != null && publisher.isVisible());
            return publisher != null
                    && Docs.PAGECLASSEURFOLDER.type().equals(parent.getType())
                    && Docs.pageDocs().contains(Docs.fromString(grandParent.getType()))
                    && filter
                    && doc.getAdapter(BlobHolder.class) != null;
        } catch (ClientException e) {
            LOG.error(e, e);
        }
        return false;
    }

}
