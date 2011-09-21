package com.leroymerlin.corp.fr.nuxeo.labs.site.test;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManagerException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class PageListRepositoryInit extends OfmRepositoryInit {

    public static final String PAGE_LIST_TITLE = "pageList";

    @Override
    public void populate(CoreSession session) throws ClientException {
        super.populate(session);
        try {
            LabsSite site = getSiteManager().getSite(session,
                    OfmRepositoryInit.SITE_URL);
            DocumentModel ofmTree = site.getTree();
            DocumentModel pageList = session.createDocumentModel(
                    ofmTree.getPathAsString(), PAGE_LIST_TITLE,
                    LabsSiteConstants.Docs.PAGELIST.type());
            pageList = session.createDocument(pageList);
            session.save();
        } catch (SiteManagerException e) {
            // At this point we can't do anythings
        }

    }

}
