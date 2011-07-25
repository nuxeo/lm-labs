package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
public final class NuxeoDocHelperTest {

    @Inject
    private CoreSession session;

    @Test
    public void canGetRootFolderAndChildren() throws ClientException {
        // SITE ROOT
        DocumentModel sitesRoot = session.getDocument(new PathRef(
                "/default-domain/" + LabsSiteConstants.Docs.SITESROOT.docName()));
        assertTrue(session.exists(sitesRoot.getRef()));
        // SITE
        DocumentModel site1 = session.createDocumentModel(
                sitesRoot.getPathAsString(), SiteFeatures.SITE_NAME,
                LabsSiteConstants.Docs.SITE.type());
        // when the "site" is created, an event is fired
        site1 = session.createDocument(site1);
        // ROOT FOLDER
        DocumentModel folder1 = session.createDocumentModel(
                site1.getPathAsString() + "/"
                        + LabsSiteConstants.Docs.TREE.docName() + "/"
                        + LabsSiteConstants.Docs.WELCOME.docName(), "folder1",
                LabsSiteConstants.Docs.FOLDER.type());
        DocumentModel folder2 = session.createDocumentModel(
                site1.getPathAsString() + "/"
                        + LabsSiteConstants.Docs.TREE.docName() + "/"
                        + LabsSiteConstants.Docs.WELCOME.docName(), "folder2",
                LabsSiteConstants.Docs.FOLDER.type());
        DocumentModel folder3 = session.createDocumentModel(
                site1.getPathAsString() + "/"
                        + LabsSiteConstants.Docs.TREE.docName() + "/"
                        + LabsSiteConstants.Docs.WELCOME.docName(), "folder3",
                LabsSiteConstants.Docs.FOLDER.type());
        session.createDocument(folder1);
        session.createDocument(folder2);
        session.createDocument(folder3);
        // SUB FOLDER
        DocumentModel sub1_1 = session.createDocumentModel(
                site1.getPathAsString() + "/"
                        + LabsSiteConstants.Docs.TREE.docName() + "/"
                        + LabsSiteConstants.Docs.WELCOME.docName() + "/folder1",
                "sub1_1", LabsSiteConstants.Docs.FOLDER.type());
        DocumentModel sub1_2 = session.createDocumentModel(
                site1.getPathAsString() + "/"
                        + LabsSiteConstants.Docs.TREE.docName() + "/"
                        + LabsSiteConstants.Docs.WELCOME.docName() + "/folder1",
                "sub1_2", LabsSiteConstants.Docs.FOLDER.type());
        DocumentModel sub2_1 = session.createDocumentModel(
                site1.getPathAsString() + "/"
                        + LabsSiteConstants.Docs.TREE.docName() + "/"
                        + LabsSiteConstants.Docs.WELCOME.docName() + "/folder2",
                "sub2_1", LabsSiteConstants.Docs.FOLDER.type());
        session.createDocument(sub1_1);
        session.createDocument(sub1_2);
        session.createDocument(sub2_1);
        session.save();

        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + LabsSiteConstants.Docs.TREE.docName() + "/"
                + LabsSiteConstants.Docs.WELCOME.docName() + "/folder1")));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + LabsSiteConstants.Docs.TREE.docName() + "/"
                + LabsSiteConstants.Docs.WELCOME.docName() + "/folder2")));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + LabsSiteConstants.Docs.TREE.docName() + "/"
                + LabsSiteConstants.Docs.WELCOME.docName() + "/folder3")));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + LabsSiteConstants.Docs.TREE.docName() + "/"
                + LabsSiteConstants.Docs.WELCOME.docName() + "/folder1/sub1_1")));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + LabsSiteConstants.Docs.TREE.docName() + "/"
                + LabsSiteConstants.Docs.WELCOME.docName() + "/folder1/sub1_2")));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + LabsSiteConstants.Docs.TREE.docName() + "/"
                + LabsSiteConstants.Docs.WELCOME.docName() + "/folder2/sub2_1")));

        // FIXME
        Map<DocumentModel, DocumentModelList> rootFolderAndChildren = NuxeoDocHelper.getRootFolderAndChildren(
                site1, session);
        assertNotNull(rootFolderAndChildren);
        assertEquals(3, rootFolderAndChildren.keySet().size());

        for (DocumentModel rootFolder : rootFolderAndChildren.keySet()) {
            if (new String(site1.getPathAsString() + "/"
                    + LabsSiteConstants.Docs.TREE.docName() + "/"
                    + LabsSiteConstants.Docs.WELCOME.docName() + "/folder1").equals(rootFolder.getPathAsString())) {
                assertEquals(2, rootFolderAndChildren.get(rootFolder).size());
            } else if (new String(site1.getPathAsString() + "/"
                    + LabsSiteConstants.Docs.TREE.docName() + "/"
                    + LabsSiteConstants.Docs.WELCOME.docName() + "/folder2").equals(rootFolder.getPathAsString())) {
                assertEquals(1, rootFolderAndChildren.get(rootFolder).size());
            } else if (new String(site1.getPathAsString() + "/"
                    + LabsSiteConstants.Docs.TREE.docName() + "/"
                    + LabsSiteConstants.Docs.WELCOME.docName() + "/folder3").equals(rootFolder.getPathAsString())) {
                assertEquals(0, rootFolderAndChildren.get(rootFolder).size());
            }
        }
    }
}
