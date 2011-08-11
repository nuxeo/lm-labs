package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.annotations.RepositoryInit;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageClasseurRepositoryInit implements RepositoryInit {

    public static final String PAGECLASSEUR_NAME = "page_classeur";
    public static final String PAGECLASSEUR_CONTAINER_PATH = "/";

    public void populate(CoreSession session) throws ClientException {
        DocumentModel doc = session.createDocumentModel(PAGECLASSEUR_CONTAINER_PATH, PAGECLASSEUR_NAME, Docs.PAGECLASSEUR.type());
        doc = session.createDocument(doc);
        session.save();
    }
}
