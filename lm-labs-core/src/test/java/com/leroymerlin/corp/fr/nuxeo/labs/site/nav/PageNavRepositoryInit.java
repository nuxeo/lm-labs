package com.leroymerlin.corp.fr.nuxeo.labs.site.nav;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.annotations.RepositoryInit;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageNavRepositoryInit implements RepositoryInit {

    public static final String PAGENAV_NAME  = "page_nav";
    public static final String PAGENAV_CONTAINER_PATH = "/";

    public void populate(CoreSession session) throws ClientException {
        DocumentModel doc = session.createDocumentModel(PAGENAV_CONTAINER_PATH, PAGENAV_NAME, Docs.PAGENAV.type());
        doc = session.createDocument(doc);
        session.save();
    }
}
