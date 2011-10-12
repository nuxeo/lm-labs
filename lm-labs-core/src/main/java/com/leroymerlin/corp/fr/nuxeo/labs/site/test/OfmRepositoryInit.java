package com.leroymerlin.corp.fr.nuxeo.labs.site.test;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.annotations.RepositoryInit;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManagerException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;

public class OfmRepositoryInit implements RepositoryInit {

    public static final String SITE_URL = "ofm";
    public static final String SITE_TITLE = "OFM";
    public static final String TREE = "tree";

    protected DocumentModel ofm;

    @Override
    public void populate(CoreSession session) throws ClientException {
        try {
            LabsSite site = getSiteManager().createSite(session, SITE_TITLE,
                    SITE_URL);
            ofm = site.getDocument();

            DocumentModel assetsDoc = site.getAssetsDoc();
            DocumentModel doc = session.createDocumentModel(
                    assetsDoc.getPathAsString(), "folder1", "Folder");
            doc.setPropertyValue("dc:title", "folder1");
            session.createDocument(doc);

            createChildFolder(session, assetsDoc, "folder", 3);

            DocumentModel  folder1 = session.getChild(assetsDoc.getRef(), "folder1");
            createChildFolder(session, folder1, "folder1_", 4);

            DocumentModel  folder2 = session.getChild(assetsDoc.getRef(), "folder2");
            createChildFolder(session, folder2, "folder2_", 3);

            session.save();
        } catch (SiteManagerException e) {
            // Site already exists... do nothing
        }

    }

    private void createChildFolder(CoreSession session, DocumentModel parentDoc,
            String prefix, int number) throws ClientException {
        for (int i = 0; i < number; i++) {
            String name = prefix + i;
            DocumentModel doc = session.createDocumentModel(
                    parentDoc.getPathAsString(), name, "Folder");
            doc.setPropertyValue("dc:title", name);
            session.createDocument(doc);
        }

    }

    protected static SiteManager getSiteManager() {
        try {
            return Framework.getService(SiteManager.class);
        } catch (Exception e) {
            return null;
        }
    }

}
