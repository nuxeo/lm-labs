package com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.schema.FacetNames;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.publisher.LabsPublisher;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.AbstractContentProvider;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class SiteContentProvider extends AbstractContentProvider {

    private boolean isLimitedToAsset;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final class PageFilter implements Filter {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean accept(DocumentModel docModel) {
            try {
                boolean isAdmin = docModel.getAdapter(SiteDocument.class).getSite().isAdministrator(
                        docModel.getCoreSession().getPrincipal().getName());
                LabsPublisher publisher = docModel.getAdapter(LabsPublisher.class);
                boolean filter = isAdmin
                        || (publisher != null && publisher.isVisible());
                return docModel.getAdapter(Page.class) != null
                        && filter
                        && !LabsSiteConstants.State.DELETE.getState().equals(
                                docModel.getCurrentLifeCycleState());
            } catch (ClientException e) {
                return false;
            }
        }
    }

    private static final class AssetFilter implements Filter {

        private static final long serialVersionUID = 1L;

        @Override
        public boolean accept(DocumentModel docModel) {
            try {
                return /* ((docModel.getAdapter(Page.class) != null) || */(docModel.hasFacet(FacetNames.FOLDERISH))
                        && !LabsSiteConstants.State.DELETE.getState().equals(
                                docModel.getCurrentLifeCycleState());
            } catch (ClientException e) {
                return false;
            }
        }
    }

    private static final Filter pageFilter = new PageFilter();

    private static final Filter assetFilter = new AssetFilter();

    public SiteContentProvider(CoreSession session, boolean isLimitedToAsset) {
        super(session);
        this.isLimitedToAsset = isLimitedToAsset;
    }

    @Override
    public Filter getDocFilter() {
        return isLimitedToAsset ? assetFilter : pageFilter;
    }

    @Override
    public String getLabel(Object obj) {

        String result = super.getLabel(obj);
        if (StringUtils.capitalize(LabsSiteConstants.Docs.TREE.docName()).equals(
                result)) {
            DocumentModel doc = (DocumentModel) obj;
            SiteDocument sd = doc.getAdapter(SiteDocument.class);
            try {
                result = sd.getSite().getTitle();
            } catch (ClientException e) {
            }
        }
        return result;
    }

}
