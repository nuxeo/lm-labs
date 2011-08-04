package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.annotations.RepositoryInit;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class LabsSiteRepositoryInit implements RepositoryInit {

    public void populate(CoreSession session) throws ClientException {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1", LabsSiteConstants.Docs.SITE.type());
        doc = session.createDocument(doc);
        session.save();
    }
}
