package com.leroymerlin.corp.fr.nuxeo.labs.site.repository;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.RepositoryInit;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;

public class OfmRepositoryInit implements RepositoryInit {
    
    public static final String SITE_URL = "ofm";
    public static final String SITE_TITLE = "OFM";
    public static final String TREE = "tree";
    
    protected DocumentModel ofm;

    @Override
    public void populate(CoreSession session) throws ClientException {
        DocumentModel root = LabsSiteUtils.getSitesRoot(session);
        if (!session.exists(new PathRef(root.getPathAsString() + "/" + SITE_TITLE))) {
            ofm = session.createDocumentModel(root.getPathAsString(), SITE_TITLE, LabsSiteConstants.Docs.SITE.type());
            LabsSite site = ofm.getAdapter(LabsSite.class);
            site.setURL(SITE_URL);
            site.setTitle(SITE_TITLE);
            ofm = session.createDocument(ofm);
            session.save();
        }
    }

}
