package com.leroymerlin.corp.fr.nuxeo.labs.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.publisher.LabsPublisher;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class DocUnderVisiblePageFilter implements Filter {

    private static final long serialVersionUID = 1L;
    private static final Log LOG = LogFactory.getLog(DocUnderVisiblePageFilter.class);
    
    private CoreSession session;
    
    @SuppressWarnings("unused")
	private DocUnderVisiblePageFilter() {}
    
    public DocUnderVisiblePageFilter(CoreSession session) {
		super();
		this.session = session;
	}

	@Override
    public boolean accept(DocumentModel doc) {
        try {
            SiteDocument sd = doc.getAdapter(SiteDocument.class);
            boolean isAdmin = sd.getSite(session).isAdministrator(session.getPrincipal().getName(), session);
            Page parentPage;
            if (Docs.pageDocs().contains(Docs.fromString(doc.getType()))) {
                parentPage = doc.getAdapter(Page.class);
            } else {
                parentPage = doc.getAdapter(SiteDocument.class).getParentPage(session);
            }
            if (Docs.pageDocs().contains(Docs.fromString(parentPage.getDocument().getType()))) {
                if (isAdmin) {
                    return true;
                }
                LabsPublisher publisher = parentPage.getDocument().getAdapter(LabsPublisher.class);
                if (publisher != null) {
                    if (publisher.isVisible()) {
                        return true;
                    }
                }
            }
        } catch (ClientException e) {
            LOG.error(e, e);
            return false;
        }
        return false;
    }

}
