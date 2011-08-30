package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.features.directory.LMTestDirectoryFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features({ LMTestDirectoryFeature.class, SiteFeatures.class })
@RepositoryConfig(user = "Administrator")
public class LabsSiteWebAppUtilsTest {

    @Inject
    private CoreSession session;
    @Test
    public void canGetTreeview() throws ClientException {
        generateSite();

        final DocumentModel site1 = session.getDocument(new PathRef("/"
                + Docs.DEFAULT_DOMAIN.docName() + "/"
                + Docs.SITESROOT.docName() + "/" + SiteFeatures.SITE_NAME));

        String treeview = LabsSiteWebAppUtils.getTreeview(site1, null);
        assertNotNull(treeview);
        
        treeview = LabsSiteWebAppUtils.getTreeview(site1, null, false);
         assertNotNull(treeview);
        
        // TODO test result with regex
    }

    // TODO
    private void generateSite() throws ClientException {
        // SITE ROOT
        DocumentModel sitesRoot = session.getDocument(new PathRef("/"
                + Docs.DEFAULT_DOMAIN.docName() + "/"
                + Docs.SITESROOT.docName()));
        assertTrue(session.exists(sitesRoot.getRef()));
        // SITE
        DocumentModel site1 = session.createDocumentModel(
                sitesRoot.getPathAsString(), SiteFeatures.SITE_NAME,
                Docs.SITE.type());
        // when the "site" is created, an event is fired
        site1 = session.createDocument(site1);
        // ROOT FOLDER
        DocumentModel folder1 = session.createDocumentModel(
                site1.getPathAsString() + "/" + Docs.TREE.docName() + "/"
                        + Docs.WELCOME.docName(), "folder1", Docs.PAGE.type());
        DocumentModel folder2 = session.createDocumentModel(
                site1.getPathAsString() + "/" + Docs.TREE.docName() + "/"
                        + Docs.WELCOME.docName(), "folder2", Docs.PAGE.type());
        DocumentModel folder3 = session.createDocumentModel(
                site1.getPathAsString() + "/" + Docs.TREE.docName() + "/"
                        + Docs.WELCOME.docName(), "folder3", Docs.PAGE.type());
        session.createDocument(folder1);
        session.createDocument(folder2);
        session.createDocument(folder3);
        // SUB FOLDER
        DocumentModel sub1_1 = session.createDocumentModel(
                site1.getPathAsString() + "/" + Docs.TREE.docName() + "/"
                        + Docs.WELCOME.docName() + "/folder1", "sub1_1",
                Docs.PAGE.type());
        DocumentModel sub1_2 = session.createDocumentModel(
                site1.getPathAsString() + "/" + Docs.TREE.docName() + "/"
                        + Docs.WELCOME.docName() + "/folder1", "sub1_2",
                Docs.PAGE.type());
        DocumentModel sub2_1 = session.createDocumentModel(
                site1.getPathAsString() + "/" + Docs.TREE.docName() + "/"
                        + Docs.WELCOME.docName() + "/folder2", "sub2_1",
                Docs.PAGE.type());
        session.createDocument(sub1_1);
        session.createDocument(sub1_2);
        session.createDocument(sub2_1);
        session.save();

        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName()
                + "/folder1")));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName()
                + "/folder2")));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName()
                + "/folder3")));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName()
                + "/folder1/sub1_1")));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName()
                + "/folder1/sub1_2")));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName()
                + "/folder2/sub2_1")));
    }

    /**
     * @param time
     */
    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
