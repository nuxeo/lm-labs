package com.leroymerlin.corp.fr.nuxeo.labs.site.repository;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.annotations.RepositoryInit;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;

public class PageClasseurRepositoryInit implements RepositoryInit {

    @Override
    public void populate(CoreSession session) throws ClientException {
        DocumentModel root = LabsSiteUtils.getSitesRoot(session);
        DocumentModel site = session.createDocumentModel(root.getPathAsString(), "site1", LabsSiteConstants.Docs.SITE.type());
        site = session.createDocument(site);
        DocumentModel classeur = session.createDocumentModel(site.getPathAsString() + "/" + LabsSiteConstants.Docs.TREE.docName(), "pageclasseur", LabsSiteConstants.Docs.PAGECLASSEUR.type());
        session.createDocument(classeur);
        session.save();
    }

}
