package com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.AbstractContentProvider;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class AdminSiteContentProvider extends AbstractContentProvider {

    private static final long serialVersionUID = 1L;
    
    private static final class PageFilter implements Filter {
        
        private static final long serialVersionUID = 1L;
        
        @Override
        public boolean accept(DocumentModel docModel) {
            return (docModel.getAdapter(Page.class) != null);
        }
    }
    
    private static final Filter pageFilter = new PageFilter();
    
    public AdminSiteContentProvider(CoreSession session) {
        super(session);
    }

    @Override
    public Filter getDocFilter() {
        return pageFilter;
    }

    @Override
    public String getLabel(Object obj) {

        String result = super.getLabel(obj);
        if (StringUtils.capitalize(LabsSiteConstants.Docs.TREE.docName()).equals(
                result)) {
            DocumentModel doc = (DocumentModel) obj;
            SiteDocument sd = doc.getAdapter(SiteDocument.class);
            try {
                result = sd.getSite(session).getTitle();
            } catch (ClientException e) {
            }
        }
        return result;
    }

}
