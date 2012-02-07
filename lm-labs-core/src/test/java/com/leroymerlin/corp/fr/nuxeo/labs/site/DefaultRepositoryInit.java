package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.annotations.RepositoryInit;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class DefaultRepositoryInit implements RepositoryInit {

    @Override
    public void populate(CoreSession session) throws ClientException {
        DocumentModel doc = session.createDocumentModel("/",
                Docs.DEFAULT_DOMAIN.docName(), Docs.DEFAULT_DOMAIN.type());
        session.createDocument(doc);
        session.save();

    }

}
