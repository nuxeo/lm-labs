package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.features.directory.LMTestDirectoryFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.portal.security.SecurityData;
import com.leroymerlin.corp.fr.nuxeo.portal.security.SecurityDataHelper;

@RunWith(FeaturesRunner.class)
@Features({ LMTestDirectoryFeature.class, SiteFeatures.class })
@RepositoryConfig(user = "Administrator")
public final class LabsSiteUtilsTest {

    @Inject
    private CoreSession session;

    @Inject
    protected FeaturesRunner featuresRunner;

    @Rule public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void iCanGetSitesRoot() throws Exception {
        DocumentModel root = LabsSiteUtils.getSitesRoot(session);
        assertNotNull(root);
        SecurityData data = SecurityDataHelper.buildSecurityData(root);
        List<String> list = data.getCurrentDocGrant().get(
                SecurityConstants.MEMBERS);
        assertTrue(list.contains(SecurityConstants.EVERYTHING));
    }

    @Test
    @Deprecated
    public void canGetRootFolderAndChildren() throws ClientException {
        generateSite();

        final DocumentModel site1 = session.getDocument(new PathRef("/"
                + LabsSiteConstants.Docs.DEFAULT_DOMAIN.docName() + "/"
                + LabsSiteConstants.Docs.SITESROOT.docName() + "/"
                + SiteFeatures.SITE_NAME));

        DocumentModelList rootFolder = LabsSiteUtils.getRootFolder(site1,
                session);
        assertNotNull(rootFolder);
        assertEquals(3, rootFolder.size());

        for (DocumentModel doc : rootFolder) {
            if (new String(site1.getPathAsString() + "/"
                    + LabsSiteConstants.Docs.TREE.docName() + "/"
                    + LabsSiteConstants.Docs.WELCOME.docName() + "/folder1").equals(doc.getPathAsString())) {
                assertEquals(2, session.getChildren(doc.getRef()).size());
            } else if (new String(site1.getPathAsString() + "/"
                    + LabsSiteConstants.Docs.TREE.docName() + "/"
                    + LabsSiteConstants.Docs.WELCOME.docName() + "/folder2").equals(doc.getPathAsString())) {
                assertEquals(1, session.getChildren(doc.getRef()).size());
            } else if (new String(site1.getPathAsString() + "/"
                    + LabsSiteConstants.Docs.TREE.docName() + "/"
                    + LabsSiteConstants.Docs.WELCOME.docName() + "/folder3").equals(doc.getPathAsString())) {
                assertEquals(0, session.getChildren(doc.getRef()).size());
            }
        }
    }

    private void generateSite() throws ClientException {
        // SITE ROOT
        DocumentModel sitesRoot = session.getDocument(new PathRef("/"
                + LabsSiteConstants.Docs.DEFAULT_DOMAIN.docName() + "/"
                + LabsSiteConstants.Docs.SITESROOT.docName()));
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
    }
    
    @Test
    public void iCanGetSiteTreePath() throws Exception {
        DocumentModel site = session.createDocumentModel(LabsSiteUtils.getSitesRootPath(), "MonSite", LabsSiteConstants.Docs.SITE.type());
        assertNotNull(site);
        site = session.createDocument(site);
        String treePath = LabsSiteUtils.getSiteTreePath(site);
        assertNotNull(treePath);
        assertTrue(treePath.endsWith("/tree"));
        assertTrue(treePath.contains("/MonSite/"));
    }
    
    @Test
    public void iCannotGetTreePathOfNonSite() throws Exception {
        DocumentModel welcomePage = session.getDocument(new PathRef(LabsSiteUtils.getSitesRootPath() + "/MonSite/tree/welcome"));
        assertNotNull(welcomePage);
        thrown.expect(IllegalArgumentException.class);
        LabsSiteUtils.getSiteTreePath(welcomePage);
    }

    @Ignore
    @Test
    public void iCanGetSitesRootAsUser() throws Exception {
        NuxeoPrincipal principal = getUserManager().getPrincipal("CGM");
        assertNotNull(principal);
        assertTrue(principal.getAllGroups().contains(SecurityConstants.MEMBERS));
        CoreFeature coreFeature = featuresRunner.getFeature(CoreFeature.class);
        CoreSession sessionCGM = coreFeature.getRepository().getRepositoryHandler().openSessionAs(
                "CGM");
        // changeUser("CGM");
        assertNotNull(LabsSiteUtils.getSitesRoot(sessionCGM));
        // changeUser("Administrator");
        coreFeature.getRepository().getRepositoryHandler().releaseSession(
                sessionCGM);
    }

    @Test
    public void cannotGetTreeviewChildren() throws ClientException {
        String treeviewChildren = LabsSiteUtils.getTreeviewChildren(null, null);
        assertNull(treeviewChildren);
        treeviewChildren = LabsSiteUtils.getTreeviewChildren(null, session);
        assertNull(treeviewChildren);

        generateSite();
        final DocumentModel site1 = session.getDocument(new PathRef("/"
                + LabsSiteConstants.Docs.DEFAULT_DOMAIN.docName() + "/"
                + LabsSiteConstants.Docs.SITESROOT.docName() + "/"
                + SiteFeatures.SITE_NAME));
        treeviewChildren = LabsSiteUtils.getTreeviewChildren(site1, null);
        assertNull(treeviewChildren);
    }

    @Test
    public void canGetTreeviewChildren() throws ClientException {
        generateSite();

        final DocumentModel site1 = session.getDocument(new PathRef("/"
                + LabsSiteConstants.Docs.DEFAULT_DOMAIN.docName() + "/"
                + LabsSiteConstants.Docs.SITESROOT.docName() + "/"
                + SiteFeatures.SITE_NAME));

        String treeviewChildren = LabsSiteUtils.getTreeviewChildren(site1,
                session);
        assertNotNull(treeviewChildren);
        
        // TODO test de la chaîne?
    }

    protected void changeUser(String username) throws ClientException {
        CoreFeature coreFeature = featuresRunner.getFeature(CoreFeature.class);
        session = coreFeature.getRepository().getRepositoryHandler().changeUser(
                session, username);
    }

    private UserManager getUserManager() throws Exception {
        UserManager userManager = Framework.getService(UserManager.class);
        if (userManager == null) {
            throw new Exception("unable to get userManager");
        }
        return userManager;
    }
}
