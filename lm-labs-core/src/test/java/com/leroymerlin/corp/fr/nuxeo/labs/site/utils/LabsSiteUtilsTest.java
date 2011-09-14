package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
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
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
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

    @Test
    public void iCanGetSitesRoot() throws Exception {
        DocumentModel root = LabsSiteUtils.getSitesRoot(session);
        assertNotNull(root);
        SecurityData data = SecurityDataHelper.buildSecurityData(root);
        List<String> list = data.getCurrentDocGrant()
                .get(SecurityConstants.MEMBERS);
        assertTrue(list.contains(SecurityConstants.EVERYTHING));
    }

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

    @Test
    public void iCanGetSiteTreePath() throws Exception {
        DocumentModel site = session.createDocumentModel(
                LabsSiteUtils.getSitesRootPath(), "MonSite", Docs.SITE.type());
        assertNotNull(site);
        site = session.createDocument(site);

        LabsSite ls = site.getAdapter(LabsSite.class);

        assertNotNull(ls.getTree());
        String treePath = ls.getTree().getPathAsString();
        assertTrue(treePath.endsWith("/tree"));
        assertTrue(treePath.contains("/MonSite/"));
    }



    @Test
    public void canGetAllDoc() throws ClientException {
        generateSite();

        final DocumentModel site1 = session.getDocument(new PathRef("/"
                + Docs.DEFAULT_DOMAIN.docName() + "/"
                + Docs.SITESROOT.docName() + "/" + SiteFeatures.SITE_NAME));

        LabsSite site  = site1.getAdapter(LabsSite.class);

        List<Page> allPages = site.getAllPages();
        assertNotNull(allPages);
        assertTrue(allPages.size() > 1);
    }

    @Test
    public void iCanGetParentSite() throws Exception {
        final String siteName = "monsite";
        DocumentModel site = session.createDocumentModel(
                LabsSiteUtils.getSitesRootPath(), siteName, Docs.SITE.type());
        site = session.createDocument(site);
        SiteDocument sd = site.getAdapter(SiteDocument.class);
        assertEquals(site.getId(), sd.getSite().getDocument().getId());

        DocumentModel tree = session.getDocument(new PathRef(
                LabsSiteUtils.getSitesRootPath() + "/" + siteName + "/"
                        + Docs.TREE.docName()));
        sd = tree.getAdapter(SiteDocument.class);
        assertEquals(site.getId(), sd.getSite().getDocument().getId());

        DocumentModel pageClasseur = session.createDocumentModel(
                tree.getPathAsString(), "page1", Docs.PAGECLASSEUR.type());
        pageClasseur = session.createDocument(pageClasseur);
        sd = pageClasseur.getAdapter(SiteDocument.class);
        assertEquals(site.getId(),
                sd.getSite().getDocument().getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void iCannotGetParentSiteOfSitesRoot() throws Exception {

        DocumentModel sitesRoot = LabsSiteUtils.getSitesRoot(session);
        SiteDocument sd = sitesRoot.getAdapter(SiteDocument.class);
        sd.getSite();
    }

    @Test
    public void testGetClosestPage() throws Exception {
        final String siteName = "monsite";

        DocumentModel sitesRoot = LabsSiteUtils.getSitesRoot(session);
        SiteDocument sd = sitesRoot.getAdapter(SiteDocument.class);
        assertThat(sd, is(notNullValue()));
        assertThat(sd.getPage(), is(notNullValue()));
        assertThat(sd.getPage()
                .getDocument()
                .getRef(), is(sitesRoot.getRef()));

        DocumentModel pageClasseur = session.getDocument(new PathRef(
                LabsSiteUtils.getSitesRootPath() + "/" + siteName + "/"
                        + Docs.TREE.docName() + "/page1"));
        sd = pageClasseur.getAdapter(SiteDocument.class);
        assertThat(sd.getPage()
                .getDocument()
                .getRef(), is(pageClasseur.getRef()));

        DocumentModel folder = session.createDocumentModel(
                pageClasseur.getPathAsString(), "folder", "Folder");
        folder = session.createDocument(folder);
        sd = folder.getAdapter(SiteDocument.class);
        assertThat(sd.getPage()
                .getDocument()
                .getRef(), is(pageClasseur.getRef()));

        DocumentModel file = session.createDocumentModel(
                pageClasseur.getPathAsString(), "file", "File");
        file = session.createDocument(file);
        sd = file.getAdapter(SiteDocument.class);
        assertThat(sd.getPage()
                .getDocument()
                .getRef(), is(pageClasseur.getRef()));
    }



    protected void changeUser(String username) throws ClientException {
        CoreFeature coreFeature = featuresRunner.getFeature(CoreFeature.class);
        session = coreFeature.getRepository()
                .getRepositoryHandler()
                .changeUser(session, username);
    }

    private UserManager getUserManager() throws Exception {
        UserManager userManager = Framework.getService(UserManager.class);
        if (userManager == null) {
            throw new Exception("unable to get userManager");
        }
        return userManager;
    }
}
