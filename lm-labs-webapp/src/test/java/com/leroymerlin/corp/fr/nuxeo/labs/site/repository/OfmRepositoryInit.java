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

    @Override
    public void populate(CoreSession session) throws ClientException {
        DocumentModel root = LabsSiteUtils.getSitesRoot(session);
        if (!session.exists(new PathRef(root.getPathAsString() + "/OFM"))) {
            DocumentModel ofm = session.createDocumentModel(root.getPathAsString(), "OFM", LabsSiteConstants.Docs.SITE.type());
            LabsSite site = ofm.getAdapter(LabsSite.class);
            site.setURL("ofm");
            site.setTitle("OFM");
            ofm = session.createDocument(ofm);
            session.save();
        }
    }

}
