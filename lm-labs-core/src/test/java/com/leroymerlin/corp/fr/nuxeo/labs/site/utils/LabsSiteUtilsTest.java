package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.local.LocalSession;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.common.core.security.PermissionsHelper;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.publisher.LabsPublisher;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.DefaultRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

@RunWith(FeaturesRunner.class)
@Features({  SiteFeatures.class })
@Deploy("com.leroymerlin.labs.core.test")
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

        LabsSite site  = Tools.getAdapter(LabsSite.class, site1, session);

        List<Page> allPages = site.getAllPages();
        assertNotNull(allPages);
        assertTrue(allPages.size() > 1);
    }

    @Test
    public void canGetAllNotDeletedDoc() throws Exception {
        generateSite();

        final DocumentModel site1 = session.getDocument(new PathRef("/"
                + Docs.DEFAULT_DOMAIN.docName() + "/"
                + Docs.SITESROOT.docName() + "/" + SiteFeatures.SITE_NAME));

        LabsSite site  = Tools.getAdapter(LabsSite.class, site1, session);

        List<Page> allPages = site.getAllPages();
        assertNotNull(allPages);
        assertTrue(allPages.size() == 7);
        for (Page page:allPages){
            assertTrue(!page.getDocument().getAdapter(LabsPublisher.class).isDeleted());
        }
        allPages.get(6).getDocument().getAdapter(LabsPublisher.class).delete();
        allPages.get(5).getDocument().getAdapter(LabsPublisher.class).delete();
        session.save();
        allPages = site.getAllPages();
        assertTrue(allPages.size() == 5);
        for (Page page:allPages){
            assertTrue(!page.getDocument().getAdapter(LabsPublisher.class).isDeleted());
        }
    }

    @Test
    public void canGetAllDeletedDoc() throws Exception {
        generateSite();

        final DocumentModel site1 = session.getDocument(new PathRef("/"
                + Docs.DEFAULT_DOMAIN.docName() + "/"
                + Docs.SITESROOT.docName() + "/" + SiteFeatures.SITE_NAME));

        LabsSite site  = Tools.getAdapter(LabsSite.class, site1, session);

        List<Page> allPages = site.getAllPages();
        assertNotNull(allPages);
        assertTrue(allPages.size() == 7);
        for (Page page:allPages){
            assertTrue(!page.getDocument().getAdapter(LabsPublisher.class).isDeleted());
        }
        allPages.get(6).getDocument().getAdapter(LabsPublisher.class).delete();
        allPages.get(5).getDocument().getAdapter(LabsPublisher.class).delete();
        allPages.get(4).getDocument().getAdapter(LabsPublisher.class).delete();
        session.save();
        allPages = site.getAllDeletedPages();
        assertTrue(allPages.size() == 3);
        for (Page page:allPages){
            assertTrue(page.getDocument().getAdapter(LabsPublisher.class).isDeleted());
        }
    }

    @Test
    public void iCanGetParentSite() throws Exception {

        LabsSite site = sm.createSite(session, "Mon Site", "monsite");
        SiteDocument sd = Tools.getAdapter(SiteDocument.class, site.getDocument(), session);
        assertEquals(site, sd.getSite());

        DocumentModel tree = site.getTree();
        sd = Tools.getAdapter(SiteDocument.class, tree, session);
        assertEquals(site, sd.getSite());

        DocumentModel pageClasseur = session.createDocumentModel(
                tree.getPathAsString(), "page1", Docs.PAGECLASSEUR.type());
        pageClasseur = session.createDocument(pageClasseur);
        sd = Tools.getAdapter(SiteDocument.class, pageClasseur, session);
        assertEquals(site,sd.getSite());
    }

    @Test(expected = IllegalArgumentException.class)
    public void iCannotGetParentSiteOfSitesRoot() throws Exception {

        DocumentModel sitesRoot = session.getDocument(new PathRef("/default-domain"));
        SiteDocument sd = Tools.getAdapter(SiteDocument.class, sitesRoot, session);
        sd.getSite();
    }

    @Test
    public void testGetClosestPage() throws Exception {
        LabsSite site = sm.createSite(session, "Mon Site", "monsite");
        SiteDocument sd = Tools.getAdapter(SiteDocument.class, site.getDocument(), session);
        assertEquals(site, sd.getSite());

        DocumentModel tree = site.getTree();
        sd = Tools.getAdapter(SiteDocument.class, tree, session);
        assertEquals(site, sd.getSite());

        DocumentModel pageClasseur = session.createDocumentModel(
                tree.getPathAsString(), "page1", Docs.PAGECLASSEUR.type());
        pageClasseur = session.createDocument(pageClasseur);
        session.save();

        pageClasseur = session.getDocument(new PathRef(
                site.getTree().getPathAsString()+ "/page1"));


        sd = Tools.getAdapter(SiteDocument.class, pageClasseur, session);
        assertThat(sd.getParentPage()
                .getDocument()
                .getRef(), is(pageClasseur.getRef()));

        DocumentModel folder = session.createDocumentModel(
                pageClasseur.getPathAsString(), "folder", "Folder");
        folder = session.createDocument(folder);
        sd = Tools.getAdapter(SiteDocument.class, folder, session);
        assertThat(sd.getParentPage()
                .getDocument()
                .getRef(), is(pageClasseur.getRef()));

        DocumentModel file = session.createDocumentModel(
                pageClasseur.getPathAsString(), "file", "File");
        file = session.createDocument(file);
        sd = Tools.getAdapter(SiteDocument.class, file, session);
        assertThat(sd.getParentPage()
                .getDocument()
                .getRef(), is(pageClasseur.getRef()));
    }



    private CoreSession changeUser(String username) throws ClientException {
        CoreFeature coreFeature = featuresRunner.getFeature(CoreFeature.class);
        Map<String, Serializable> ctx = new HashMap<String, Serializable>();
        ctx.put("username", username);
        CoreSession userSession = LocalSession.createInstance();
        userSession.connect(coreFeature.getRepository().getName(), ctx);
        return userSession;
    }

    @Test
    public void isOnlyRead() throws Exception {
        DocumentModel docu = session.createDocumentModel("/", "myfolder", "Folder");
        docu = session.createDocument(docu);
        PermissionsHelper.addPermission(docu, SecurityConstants.READ, "toto", true);
        session.save();
        assertTrue(PermissionsHelper.hasPermission(docu, SecurityConstants.READ, "toto"));
        CoreSession sessionToto = changeUser("toto");
        docu = sessionToto.getDocument(docu.getRef());
        assertTrue(LabsSiteUtils.isOnlyRead(docu, sessionToto));
    }

    @Test
    public void isNotOnlyRead() throws Exception {
        DocumentModel docu = session.createDocumentModel("/", "myfolder", "Folder");
        docu = session.createDocument(docu);
        PermissionsHelper.addPermission(docu, SecurityConstants.READ_WRITE, "toto", true);
        session.save();
        assertTrue(PermissionsHelper.hasPermission(docu, SecurityConstants.READ_WRITE, "toto"));
        CoreSession sessionToto = changeUser("toto");
        docu = sessionToto.getDocument(docu.getRef());
        assertTrue(!LabsSiteUtils.isOnlyRead(docu, session));
    }

    @Test
    public void existPageName() throws Exception {
        LabsSite site = sm.createSite(session, "Mon Site", "monsite");

        DocumentModel pageClasseur = session.createDocumentModel(
                site.getTree().getPathAsString(), "pageClasseur", Docs.PAGECLASSEUR.type());
        session.createDocument(pageClasseur);

        DocumentModel pageListe = session.createDocumentModel(
                site.getTree().getPathAsString(), "pageListe", Docs.PAGELIST.type());
        session.createDocument(pageListe);

        DocumentModel pageNews = session.createDocumentModel(
                site.getTree().getPathAsString(), "pageNews", Docs.PAGENEWS.type());
        pageNews = session.createDocument(pageNews);
        session.save();
        assertTrue(LabsSiteUtils.pageNameExists("pageNews", site.getTree().getRef(), session));
        pageNews.getAdapter(LabsPublisher.class).publish();
        session.save();
        assertTrue(LabsSiteUtils.pageNameExists("pageNews", site.getTree().getRef(), session));
        pageNews.getAdapter(LabsPublisher.class).draft();
        session.save();
        assertTrue(LabsSiteUtils.pageNameExists("pageNews", site.getTree().getRef(), session));
        pageNews.getAdapter(LabsPublisher.class).delete();
        session.save();

        assertFalse(LabsSiteUtils.pageNameExists("papage", site.getTree().getRef(), session));
        assertFalse(LabsSiteUtils.pageNameExists("pageList", site.getTree().getRef(), session));
        assertTrue(LabsSiteUtils.pageNameExists("pageNews", site.getTree().getRef(), session));
    }

    @Test
    public void getPageName() throws Exception {
        LabsSite site = sm.createSite(session, "Mon Site", "monsite");

        DocumentModel pageClasseur = session.createDocumentModel(
                site.getTree().getPathAsString(), "pageClasseur", Docs.PAGECLASSEUR.type());
        session.createDocument(pageClasseur);

        DocumentModel pageListe = session.createDocumentModel(
                site.getTree().getPathAsString(), "pageListe", Docs.PAGELIST.type());
        session.createDocument(pageListe);

        DocumentModel pageNews = session.createDocumentModel(
                site.getTree().getPathAsString(), "pageNews", Docs.PAGENEWS.type());
        pageNews = session.createDocument(pageNews);
        assertNotNull(LabsSiteUtils.getPageName("pageNews", site.getTree().getRef(), session));
        pageNews.getAdapter(LabsPublisher.class).delete();

        session.save();

        assertNull(LabsSiteUtils.getPageName("papage", site.getTree().getRef(), session));
        assertNull(LabsSiteUtils.getPageName("pageList", site.getTree().getRef(), session));
        assertNotNull(LabsSiteUtils.getPageName("pageNews", site.getTree().getRef(), session));
    }


    @Test
    public void slugify() throws Exception {
        assertEquals("test", LabsSiteUtils.doLabsSlugify("test"));
    }

}
