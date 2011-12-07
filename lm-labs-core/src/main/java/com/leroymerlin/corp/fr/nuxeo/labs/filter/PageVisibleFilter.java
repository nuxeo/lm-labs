package com.leroymerlin.corp.fr.nuxeo.labs.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.publisher.LabsPublisher;

public class PageVisibleFilter implements Filter {

    private static final long serialVersionUID = 1L;
    private static final Log LOG = LogFactory.getLog(PageVisibleFilter.class);

    @Override
    public boolean accept(DocumentModel doc) {
        LabsPublisher publisher = doc.getAdapter(LabsPublisher.class);
        if (publisher != null) {
            try {
                if (publisher.isVisible()) {
                    return true;
                }
            } catch (ClientException e) {
                LOG.error(e, e);
            }
        }
        return false;
    }

}
