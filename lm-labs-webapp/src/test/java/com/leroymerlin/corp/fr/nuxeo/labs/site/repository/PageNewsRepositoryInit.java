package com.leroymerlin.corp.fr.nuxeo.labs.site.repository;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;

public class PageNewsRepositoryInit extends OfmRepositoryInit {

    @Override
    public void populate(CoreSession session) throws ClientException {
        super.populate(session);
        
        DocumentModel ofmTree = session.getDocument(new PathRef(LabsSiteUtils.getSitesRootPath() + "/ofm/tree"));
        DocumentModel pageNews = session.createDocumentModel(ofmTree.getPathAsString(), "pageNews", LabsSiteConstants.Docs.PAGENEWS.type());
        session.createDocument(pageNews);
        session.save();
//        DocumentModel root = LabsSiteUtils.getSitesRoot(session);
//        DocumentModel ofm = session.createDocumentModel(root.getPathAsString(), "ofm", LabsSiteConstants.Docs.SITE.type());
//        session.createDocument(ofm);
//        session.save();
    }

}
