package com.leroymerlin.corp.fr.nuxeo.labs.site.providers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.ecm.platform.query.api.AbstractPageProvider;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;

import com.leroymerlin.corp.fr.nuxeo.labs.filter.PageClasseurDocsFilter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public class LatestUploadsPageProvider extends AbstractPageProvider<DocumentModel> implements PageProvider<DocumentModel> {

    private static final long serialVersionUID = 1L;

    public static final String PARENT_DOCUMENT_PROPERTY = "parentDocument";

    private static final Log LOG = LogFactory.getLog(LatestUploadsPageProvider.class);

    private static final String UPLOADS_SORT_FIELD = "dc:modified";

    protected List<DocumentModel> pageUploads = null;

    @Override
    public List<DocumentModel> getCurrentPage() {
        if (pageUploads == null) {
            pageUploads = new ArrayList<DocumentModel>();
            DocumentModel doc = getParentDocument();
            StringBuilder query = new StringBuilder();
            try {
                CoreSession session = (CoreSession)getProperties().get(CoreQueryDocumentPageProvider.CORE_SESSION_PROPERTY);
                SiteDocument sd = Tools.getAdapter(SiteDocument.class, doc, session);
                query.append("SELECT * FROM Document WHERE ")
                .append(NXQL.ECM_PATH).append(" STARTSWITH '" + sd.getSite().getTree().getPathAsString().replace("'", "\\'") + "'")
                .append(" ORDER BY " + UPLOADS_SORT_FIELD + " DESC");
                List<DocumentModel> documents = session.query(query.toString(), new PageClasseurDocsFilter(session));
                if (!hasError()) {
                    resultsCount = documents.size();
                    long pageSize = getMinMaxPageSize();
                    if (pageSize == 0) {
                        pageUploads.addAll(documents);
                    } else {
                        // handle offset
                        if (offset <= resultsCount) {
                            for (int i = Long.valueOf(offset).intValue(); i < resultsCount
                                    && i < offset + pageSize; i++) {
                                pageUploads.add(documents.get(i));
                            }
                        }
                    }
                }
            } catch (ClientException e) {
                error = e;
                LOG.error(e, e);
            }
        }
        return pageUploads;
    }

    private DocumentModel getParentDocument() {
        Map<String, Serializable> props = this.getProperties();
        return (DocumentModel) props.get(PARENT_DOCUMENT_PROPERTY);
    }

    /* (non-Javadoc)
     * @see org.nuxeo.ecm.platform.query.api.AbstractPageProvider#isSortable()
     */
    @Override
    public boolean isSortable() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.nuxeo.ecm.platform.query.api.AbstractPageProvider#pageChanged()
     */
    @Override
    protected void pageChanged() {
        super.pageChanged();
        pageUploads = null;
    }

    /* (non-Javadoc)
     * @see org.nuxeo.ecm.platform.query.api.AbstractPageProvider#refresh()
     */
    @Override
    public void refresh() {
        super.refresh();
        pageUploads = null;
    }

}
