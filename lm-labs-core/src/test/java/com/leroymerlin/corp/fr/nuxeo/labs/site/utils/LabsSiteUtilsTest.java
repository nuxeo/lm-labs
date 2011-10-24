package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.features.directory.LMTestDirectoryFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.DefaultRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

@RunWith(FeaturesRunner.class)
@Features({ LMTestDirectoryFeature.class, SiteFeatures.class })
@RepositoryConfig(user = "Administrator",init=DefaultRepositoryInit.class, cleanup=Granularity.METHOD)
public final class LabsSiteUtilsTest {

    @Inject
    private CoreSession session;

    @Inject
    protected FeaturesRunner featuresRunner;

    @Inject
    private SiteManager sm;


    private void generateSite() throws Exception {
        // SITE ROOT
        // SITE
        LabsSite site = null;
        if(!sm.siteExists(session, SiteFeatures.SITE_NAME)) {
            site = sm.createSite(session, SiteFeatures.SITE_NAME, SiteFeatures.SITE_NAME);
        } else {
            site = sm.getSite(session, SiteFeatures.SITE_NAME);
        }

        // ROOT FOLDER
        DocumentModel folder1 = session.createDocumentModel(
                site.getTree().getPathAsString() + "/"
                        + Docs.WELCOME.docName(), "folder1", Docs.PAGE.type());
        DocumentModel folder2 = session.createDocumentModel(
                site.getTree().getPathAsString() + "/"
                        + Docs.WELCOME.docName(), "folder2", Docs.PAGE.type());
        DocumentModel folder3 = session.createDocumentModel(
                site.getTree().getPathAsString()+ "/"
                        + Docs.WELCOME.docName(), "folder3", Docs.PAGE.type());
        folder1 = session.createDocument(folder1);
        folder2 = session.createDocument(folder2);
        folder3 = session.createDocument(folder3);
        // SUB FOLDER
        DocumentModel sub1_1 = session.createDocumentModel(
                folder1.getPathAsString(), "sub1_1",Docs.PAGE.type());
        DocumentModel sub1_2 = session.createDocumentModel(
                folder1.getPathAsString(), "sub1_2",
                Docs.PAGE.type());
        DocumentModel sub2_1 = session.createDocumentModel(
                folder2.getPathAsString(), "sub2_1",
                Docs.PAGE.type());
        session.createDocument(sub1_1);
        session.createDocument(sub1_2);
        session.createDocument(sub2_1);
        session.save();

        assertTrue(session.exists(new PathRef(site.getDocument().getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName()
                + "/folder1")));
        assertTrue(session.exists(new PathRef(site.getDocument().getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName()
                + "/folder2")));
        assertTrue(session.exists(new PathRef(site.getDocument().getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName()
                + "/folder3")));
        assertTrue(session.exists(new PathRef(site.getDocument().getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName()
                + "/folder1/sub1_1")));
        assertTrue(session.exists(new PathRef(site.getDocument().getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName()
                + "/folder1/sub1_2")));
        assertTrue(session.exists(new PathRef(site.getDocument().getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName()
                + "/folder2/sub2_1")));
    }

    @Test
    public void iCanGetSiteTreePath() throws Exception {
        LabsSite site = sm.createSite(session, "MonSite", "monsite");

        assertNotNull(site);




        assertNotNull(site.getTree());
        String treePath = site.getTree().getPathAsString();
        assertTrue(treePath.endsWith("/tree"));
        assertTrue(treePath.contains("/MonSite/"));
    }



    @Test
    public void canGetAllDoc() throws Exception {
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

        LabsSite site = sm.createSite(session, "Mon Site", "monsite");
        SiteDocument sd = site.getDocument().getAdapter(SiteDocument.class);
        assertEquals(site, sd.getSite());

        DocumentModel tree = site.getTree();
        sd = tree.getAdapter(SiteDocument.class);
        assertEquals(site, sd.getSite());

        DocumentModel pageClasseur = session.createDocumentModel(
                tree.getPathAsString(), "page1", Docs.PAGECLASSEUR.type());
        pageClasseur = session.createDocument(pageClasseur);
        sd = pageClasseur.getAdapter(SiteDocument.class);
        assertEquals(site,sd.getSite());
    }

    @Test(expected = IllegalArgumentException.class)
    public void iCannotGetParentSiteOfSitesRoot() throws Exception {

        DocumentModel sitesRoot = session.getDocument(new PathRef("/default-domain"));
        SiteDocument sd = sitesRoot.getAdapter(SiteDocument.class);
        sd.getSite();
    }

    @Test
    public void testGetClosestPage() throws Exception {
        LabsSite site = sm.createSite(session, "Mon Site", "monsite");
        SiteDocument sd = site.getDocument().getAdapter(SiteDocument.class);
        assertEquals(site, sd.getSite());

        DocumentModel tree = site.getTree();
        sd = tree.getAdapter(SiteDocument.class);
        assertEquals(site, sd.getSite());

        DocumentModel pageClasseur = session.createDocumentModel(
                tree.getPathAsString(), "page1", Docs.PAGECLASSEUR.type());
        pageClasseur = session.createDocument(pageClasseur);
        session.save();

        pageClasseur = session.getDocument(new PathRef(
                site.getTree().getPathAsString()+ "/page1"));


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
    
    @Test 
    public void isOnlyRead() throws Exception {
        DocumentModel docu = session.createDocumentModel("/", "myfolder", "Folder");
        docu = session.createDocument(docu);
        session.save();
        PermissionsHelper.addPermission(docu, SecurityConstants.READ, "toto", true);
        assertTrue(PermissionsHelper.hasPermission(docu, SecurityConstants.READ, "toto"));
        assertTrue(LabsSiteUtils.isOnlyRead(docu, "toto"));
    }
    
    @Test 
    public void isNotOnlyRead() throws Exception {
        DocumentModel docu = session.createDocumentModel("/", "myfolder", "Folder");
        docu = session.createDocument(docu);
        session.save();
        PermissionsHelper.addPermission(docu, SecurityConstants.READ_WRITE, "toto", true);
        assertTrue(PermissionsHelper.hasPermission(docu, SecurityConstants.READ_WRITE, "toto"));
        assertTrue(!LabsSiteUtils.isOnlyRead(docu, "toto"));
    }
}
