package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.api.local.LocalSession;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.features.directory.LMTestDirectoryFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.blocs.ExternalURL;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurFolder;
import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.SiteManagerException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labstemplate.LabsTemplate;
import com.leroymerlin.corp.fr.nuxeo.labs.site.publisher.LabsPublisher;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.DefaultRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.PermissionsHelper;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

@RunWith(FeaturesRunner.class)
@Features({ LMTestDirectoryFeature.class, SiteFeatures.class })
@Deploy("com.leroymerlin.labs.core.test")
@RepositoryConfig(cleanup = Granularity.METHOD, init = DefaultRepositoryInit.class)
public class LabsSiteAdapterTest {

    //private static final String PAGE_TEMPLATE_DEFAULT = "homeSimple";
    private static final String PAGE_TEMPLATE_CUSTOM = "customTemplate";
    private static final String PAGE_TEMPLATE_CUSTOM2 = "customTemplate2";

    private static final String LABSSITE_TYPE = LabsSiteConstants.Docs.SITE.type();


    private final String USERNAME1 = "CGM";

    @Inject
    private CoreSession session;

    @Inject
    protected FeaturesRunner featuresRunner;

    @Inject
    SiteManager sm;

    @Test
    public void iCanCreateALabsSiteDocument() throws Exception {
        // Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);

        // Modify property
        doc.setPropertyValue("dc:title", "le titre");

        // Persist document in db
        doc = session.createDocument(doc);

        // Commit
        session.save();

        doc = session.getDocument(new PathRef("/NameSite1"));
        assertThat(doc, is(notNullValue()));
        assertThat(doc.getTitle(), is("le titre"));

    }

    @Test
    public void testIsAdministrator() throws Exception {
        DocumentModel doc = createSite("NameSite1");
        LabsSite labssite = Tools.getAdapter(LabsSite.class, doc, session);
        assertTrue(labssite.isAdministrator("Administrator"));
        assertFalse(labssite.isAdministrator(USERNAME1));
    }

    @Test
    public void testIsContributor() throws Exception {
        DocumentModel site = createSite("NameSite1");
        session.save();

        PermissionsHelper.addPermission(site, SecurityConstants.READ_WRITE,
                USERNAME1, true);
        assertTrue(PermissionsHelper.hasPermission(site,
                SecurityConstants.READ_WRITE, USERNAME1));

        LabsSite labssite = Tools.getAdapter(LabsSite.class, site, session);
        assertTrue(labssite.isContributor(USERNAME1));
    }

    @Test
    public void iCanCreateALabsSiteAdapter() throws Exception {
        // Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);

        LabsSite labssite = Tools.getAdapter(LabsSite.class, doc, session);
        assertThat(labssite, is(notNullValue()));
        labssite.setTitle("Le titre du site");

        // Persist document in db
        doc = session.createDocument(doc);

        // Commit
        session.save();

        doc = session.getDocument(new PathRef("/NameSite1"));
        labssite = Tools.getAdapter(LabsSite.class, doc, session);
        assertThat(labssite, is(notNullValue()));
        assertThat(labssite.getTitle(), is("Le titre du site"));

    }

    @Test
    public void iCanGetPropertiesOnLabsSiteAdapter() throws Exception {
        // Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "nameSite1",
                LABSSITE_TYPE);

        doc.setPropertyValue("dc:creator", "creator");
        doc = session.createDocument(doc);

        LabsSite labssite = Tools.getAdapter(LabsSite.class, doc, session);
        assertThat(labssite, is(notNullValue()));
        labssite.setTitle("Le titre du site");
        labssite.setDescription("Description");
        labssite.setURL("URL");
        doc = session.saveDocument(doc);
        Blob blob = getTestBlob();
        labssite.setBanner(blob);

        session.save();

        doc = session.getDocument(new PathRef("/nameSite1"));
        labssite = Tools.getAdapter(LabsSite.class, doc, session);
        assertThat(labssite, is(notNullValue()));
        assertThat(labssite.getTitle(), is("Le titre du site"));
        assertThat(labssite.getDescription(), is("Description"));
        assertThat(labssite.getURL(), is("URL"));
        assertThat(labssite.getBanner(), is(notNullValue()));
        assertEquals(labssite.getBanner().getFilename(), blob.getFilename());
        assertEquals(labssite.getBanner().getLength(), blob.getLength());

    }

    private Blob getTestBlob() {
        String filename = "vision.jpg";
        File testFile = new File(
                FileUtils.getResourcePathFromContext("testFiles/" + filename));
        Blob blob = new FileBlob(testFile);
        blob.setMimeType("image/jpeg");
        blob.setFilename(filename);
        return blob;
    }

    @Test()
    public void iCantDisplayADraftedSite() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        doc.setPropertyValue("dc:title", "le titre");
        doc = session.createDocument(doc);
        session.save();
        assertTrue(!doc.getAdapter(LabsPublisher.class).isVisible());
    }

    @Test()
    public void iCanDisplayAPublishedSite() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        doc.setPropertyValue("dc:title", "le titre");
        doc = session.createDocument(doc);
        doc.getAdapter(LabsPublisher.class).publish();
        assertTrue(doc.getAdapter(LabsPublisher.class).isVisible());
    }

    @Test()
    public void iCanHaveDeletedTagOnADeletedSite() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        doc.setPropertyValue("dc:title", "le titre");
        doc = session.createDocument(doc);
        doc.getAdapter(LabsPublisher.class).delete();
        assertTrue(doc.getAdapter(LabsPublisher.class).isDeleted());
    }

    @Test()
    public void iCantHaveDeletedTagOnAOtherDeletedSite() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        doc.setPropertyValue("dc:title", "le titre");
        doc = session.createDocument(doc);
        assertTrue(!doc.getAdapter(LabsPublisher.class).isDeleted());
        doc.getAdapter(LabsPublisher.class).publish();
        assertTrue(!doc.getAdapter(LabsPublisher.class).isDeleted());
        doc.getAdapter(LabsPublisher.class).delete();
        assertTrue(doc.getAdapter(LabsPublisher.class).isDeleted());
        doc.getAdapter(LabsPublisher.class).undelete();
        assertTrue(!doc.getAdapter(LabsPublisher.class).isDeleted());
    }

    @Test()
    public void iCanGetHomePageRef() throws Exception {
        DocumentModel site = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        site.setPropertyValue("dc:title", "le titre");
        site = session.createDocument(site);
        LabsSite labsSite = Tools.getAdapter(LabsSite.class, site, session);
        DocumentModel welcome = session.getChild(labsSite.getTree().getRef(),
                Docs.WELCOME.docName());

        assertEquals(welcome.getId(), labsSite.getHomePageRef());
    }

    @Test()
    public void iCanSetHomePageRef() throws Exception {
        DocumentModel site = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        site.setPropertyValue("dc:title", "le titre");
        site = session.createDocument(site);
        LabsSite labsSite = Tools.getAdapter(LabsSite.class, site, session);
        DocumentModel page = session.createDocumentModel(
                labsSite.getTree().getPathAsString(), "page",
                Docs.HTMLPAGE.type());
        page = session.createDocument(page);
        labsSite.setHomePageRef(page.getId());
        site = session.saveDocument(site);
        session.save();

        site = session.getDocument(new PathRef("/NameSite1"));
        labsSite = Tools.getAdapter(LabsSite.class, site, session);
        assertTrue(labsSite.getHomePageRef().equals(page.getId()));
    }

    @Test
    public void iCanGetLastUpdatedDocs() throws Exception {
        DocumentModel site = createSite("NameSite1");
        session.save();
        PermissionsHelper.addPermission(site, SecurityConstants.READ,
                USERNAME1, true);
        assertTrue(PermissionsHelper.hasPermission(site,
                SecurityConstants.READ, USERNAME1));
        session.save();
        DocumentModel page = session.createDocumentModel(site.getPathAsString()
                + "/" + LabsSiteConstants.Docs.TREE.docName(), "page1",
                Docs.PAGECLASSEUR.type());
        page = session.createDocument(page);
        // folder 1
        DocumentModel folder = session.createDocumentModel(
                page.getPathAsString(), "folder", "Folder");
        folder = session.createDocument(folder);
        // folder 2
        DocumentModel folder2 = session.createDocumentModel(
                page.getPathAsString(), "folder2", "Folder");
        folder2 = session.createDocument(folder2);
        session.save();
        LabsSite labsSite = Tools.getAdapter(LabsSite.class, site, session);
        DocumentModelList lastUpdatedDocs = labsSite.getLastUpdatedDocs();

        CoreSession userSession = changeUser(USERNAME1);
        DocumentModel userSite = userSession.getDocument(new PathRef(
                "/NameSite1"));
        LabsSite userLabsSite = Tools.getAdapter(LabsSite.class, userSite, userSession);
        lastUpdatedDocs = userLabsSite.getLastUpdatedDocs();
        assertEquals(0, lastUpdatedDocs.size());
        CoreInstance.getInstance().close(userSession);

        LabsPublisher publisher = page.getAdapter(LabsPublisher.class);
        publisher.publish();
        page = session.saveDocument(page);
        session.save();
        userSession = changeUser(USERNAME1);
        userSite = userSession.getDocument(new PathRef("/NameSite1"));
        userLabsSite = Tools.getAdapter(LabsSite.class, userSite, userSession);
        lastUpdatedDocs = userLabsSite.getLastUpdatedDocs();
        assertEquals(3, lastUpdatedDocs.size());
        CoreInstance.getInstance().close(userSession);

        lastUpdatedDocs = labsSite.getLastUpdatedDocs();
        // folder 3
        DocumentModel folder3 = session.createDocumentModel(
                page.getPathAsString(), "folder3", "Folder");
        folder = session.createDocument(folder3);
        session.save();

        lastUpdatedDocs = labsSite.getLastUpdatedDocs();
        assertEquals(5, lastUpdatedDocs.size());

        // hidden document
        DocumentModel hiddenFolder = session.createDocumentModel(
                page.getPathAsString(), "hiddenFolder", "HiddenFolder");
        folder = session.createDocument(hiddenFolder);
        session.save();

        lastUpdatedDocs = labsSite.getLastUpdatedDocs();
        assertEquals(5, lastUpdatedDocs.size());
    }

    @Test
    public void iCanGetLastUpdatedNewsDocs() throws Exception {
        DocumentModel site1 = createSite("NameSite1");
        session.save();

        // page news 1
        DocumentModel page1 = session.createDocumentModel(
                site1.getPathAsString() + "/"
                        + LabsSiteConstants.Docs.TREE.docName(), "page1",
                Docs.PAGENEWS.type());
        page1 = session.createDocument(page1);
        // news 1
        DocumentModel news1 = session.createDocumentModel(
                page1.getPathAsString(), "news1", Docs.LABSNEWS.type());
        news1 = session.createDocument(news1);
        // news 2
        DocumentModel news2 = session.createDocumentModel(
                page1.getPathAsString(), "news2", Docs.LABSNEWS.type());
        news2 = session.createDocument(news2);
        session.save();

        // page news 2
        DocumentModel page2 = session.createDocumentModel(
                site1.getPathAsString() + "/"
                        + LabsSiteConstants.Docs.TREE.docName(), "page1",
                Docs.PAGENEWS.type());
        page2 = session.createDocument(page2);
        // news 1
        DocumentModel news1bis = session.createDocumentModel(
                page2.getPathAsString(), "news1bis", Docs.LABSNEWS.type());
        news1bis = session.createDocument(news1bis);
        // news 2
        DocumentModel news2bis = session.createDocumentModel(
                page2.getPathAsString(), "news2bis", Docs.LABSNEWS.type());
        news2bis = session.createDocument(news2bis);
        session.save();
        
        // we create another site with news
        DocumentModel autreSite = createSite("NameAutreSite");
        session.save();

        // autre page news 1
        DocumentModel autrePage = session.createDocumentModel(
                autreSite.getPathAsString() + "/"
                        + LabsSiteConstants.Docs.TREE.docName(), "autrePage1",
                Docs.PAGENEWS.type());
        autrePage = session.createDocument(autrePage);
        // autre news 1
        DocumentModel autreNews1 = session.createDocumentModel(
                autrePage.getPathAsString(), "autreNews1", Docs.LABSNEWS.type());
        autreNews1 = session.createDocument(autreNews1);
        // autre news 2
        DocumentModel autreNews2 = session.createDocumentModel(
                autrePage.getPathAsString(), "autreNews2", Docs.LABSNEWS.type());
        autreNews2 = session.createDocument(autreNews2);
        session.save();

        LabsSite labsSite = Tools.getAdapter(LabsSite.class, site1, session);
        DocumentModelList lastUpdatedNewsDocs = labsSite.getLastUpdatedNewsDocs();
        assertNotNull(lastUpdatedNewsDocs);
        assertEquals(4, lastUpdatedNewsDocs.size());
    }

    @Test
    public void iCanGetExternalURLs() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        doc.setPropertyValue("dc:title", "le titre");
        doc = session.createDocument(doc);
        LabsSite labsSite = Tools.getAdapter(LabsSite.class, doc, session);
        assertTrue(labsSite.getExternalURLs().isEmpty());
        labsSite.createExternalURL("b").setURL("www.b.org");
        labsSite.createExternalURL("a").setURL("www.a.org");
        ArrayList<ExternalURL> list = labsSite.getExternalURLs();
        assertEquals(2, list.size());
        assertEquals("b", list.get(0).getName());
        assertEquals("a", list.get(1).getName());
    }

    @Test
    public void iCanInsertBeforeExternalURL() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        doc.setPropertyValue("dc:title", "le titre");
        doc = session.createDocument(doc);
        LabsSite labsSite = Tools.getAdapter(LabsSite.class, doc, session);
        ExternalURL extURLA = labsSite.createExternalURL("a");
        extURLA.setURL("www.b.org");
        ExternalURL extURLB = labsSite.createExternalURL("b");
        extURLB.setURL("www.a.org");
        ExternalURL extURLC = labsSite.createExternalURL("c");
        extURLC.setURL("www.a.org");
        session.orderBefore(extURLC.getDocument().getParentRef(),
                extURLA.getDocument().getName(),
                extURLC.getDocument().getName());
        session.save();
        ArrayList<ExternalURL> list = labsSite.getExternalURLs();
        assertEquals(3, list.size());
        assertEquals("b", list.get(0).getName());
        assertEquals("a", list.get(1).getName());
        assertEquals("c", list.get(2).getName());
    }

    @Test
    public void iCanGetDeletedDocs() throws Exception {
        DocumentModel site = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        site.setPropertyValue("dc:title", "le titre");
        site = session.createDocument(site);
        LabsSite labsSite = Tools.getAdapter(LabsSite.class, site, session);
        DocumentModel classeur = session.createDocumentModel(
                labsSite.getTree().getPathAsString(), "classeur",
                Docs.PAGECLASSEUR.type());
        classeur = session.createDocument(classeur);
        DocumentModel folder = session.createDocumentModel(
                classeur.getPathAsString(), "folder1",
                Docs.PAGECLASSEURFOLDER.type());
        folder = session.createDocument(folder);
        assertTrue(labsSite.getAllDeletedDocs().isEmpty());

        PageClasseurFolder adapter = Tools.getAdapter(PageClasseurFolder.class, folder, session);
        assertNotNull(adapter);
        boolean setAsDeleted = adapter.setAsDeleted();
        session.save();
        assertTrue(setAsDeleted);
        assertFalse(labsSite.getAllDeletedDocs().isEmpty());
    }

    @Test
    public void iCanGetDefaultElementTemplate() throws Exception {
        LabsSite site = createLabsSite("NameSite1");
        assertFalse(site.isElementTemplate());
    }

    @Test
    public void iCanSetSiteAsTemplate() throws Exception {
        LabsSite site = createLabsSite("NameSite1");
        site.setElementTemplate(true);
        session.saveDocument(site.getDocument());
        assertTrue(site.isElementTemplate());
        site.setElementTemplate(false);
        session.saveDocument(site.getDocument());
        assertFalse(site.isElementTemplate());
    }

    @Test
    public void iCanSetElementPreview() throws Exception {
        LabsSite site = createLabsSite("NameSite1");
        Blob preview = site.getElementPreview();
        assertNull(preview);

        Blob blob = getTestBlob();
        site.setElementTemplate(true);
        site.setElementPreview(blob);
        preview = site.getElementPreview();
        assertNotNull(preview);
        assertEquals(preview.getFilename(), blob.getFilename());
        assertEquals(preview.getLength(), blob.getLength());

        site.setElementPreview(null);
        preview = site.getElementPreview();
        assertNull(preview);
    }

    @Test
    public void iCanCopySiteTemplate() throws Exception {
        DocumentModel siteRoot = sm.getSiteRoot(session);
        PermissionsHelper.addPermission(siteRoot,
                SecurityConstants.ADD_CHILDREN, USERNAME1, true);
        PermissionsHelper.addPermission(siteRoot, SecurityConstants.READ,
                SecurityConstants.EVERYONE, true);

        LabsSite template1 = createTemplateSite("template1");
        PermissionsHelper.addPermission(template1.getDocument(),
                SecurityConstants.READ, USERNAME1, true);
        session.save();
        String templateName = "LeTemplate";
        String themeName = "LeTheme";
        template1.getTemplate().setTemplateName(templateName);
        template1.getThemeManager().setTheme(themeName, session);
        session.saveDocument(template1.getDocument());
        session.save();
        assertEquals(themeName, template1.getThemeName());
        assertEquals(templateName, template1.getTemplate().getTemplateName());
        DocumentModel child = session.getChild(template1.getTree().getRef(), "pagehtml");
		assertEquals(PAGE_TEMPLATE_CUSTOM, Tools.getAdapter(LabsTemplate.class, child, session).getDocumentTemplateName());
        assertEquals(PAGE_TEMPLATE_CUSTOM, child.getPropertyValue(Schemas.LABSTEMPLATE.getName() + ":name"));

        CoreSession cgmSession = changeUser(USERNAME1);
        assertNotNull(cgmSession);
        LabsSite cgmSite = sm.createSite(cgmSession, "CGMsite", "CGMsite");
        session.save();
        assertNotNull(cgmSite);
        final String welcomeId = cgmSite.getIndexDocument().getId();
        LabsSite cgmTemplateSite = sm.getSite(cgmSession, "template1");
        assertNotNull(cgmTemplateSite);
        final String templateWelcomeId = cgmTemplateSite.getIndexDocument().getId();
        cgmSite.applyTemplateSite(cgmTemplateSite.getDocument());
        session.saveDocument(cgmSite.getDocument());
        session.save();
        assertFalse(templateWelcomeId.equals(welcomeId));
        assertEquals(templateName, cgmSite.getTemplate().getTemplateName());
        assertEquals(themeName, cgmTemplateSite.getThemeName());
        assertEquals(themeName, cgmSite.getThemeName());
        DocumentModelList assetsList = cgmSession.getChildren(
                cgmSite.getAssetsDoc().getRef(), "Folder");
        assertEquals(2, assetsList.size());
        assetsList = cgmSession.getChildren(cgmSite.getAssetsDoc().getRef(),
                "File");
        assertEquals(0, assetsList.size());
        DocumentModel child2 = session.getChild(cgmSite.getTree().getRef(), "pagehtml");
		assertEquals(PAGE_TEMPLATE_CUSTOM, child2.getPropertyValue(Schemas.LABSTEMPLATE.getName() + ":name"));
        assertEquals(PAGE_TEMPLATE_CUSTOM, Tools.getAdapter(LabsTemplate.class, child2, session).getDocumentTemplateName());
        assertEquals(PAGE_TEMPLATE_CUSTOM, Tools.getAdapter(LabsTemplate.class, child2, session).getTemplateName());
        DocumentModel childPageNews = session.getChild(cgmSite.getTree().getRef(), "pagenews");
		assertEquals("", Tools.getAdapter(LabsTemplate.class, childPageNews, session).getDocumentTemplateName());
        assertEquals(templateName, Tools.getAdapter(LabsTemplate.class, childPageNews, session).getTemplateName());
        // TODO more tests
    }

    @Test
    public void iCanGetContacts() throws Exception {
        DocumentModel doc = createSite("NameSite1");
        LabsSite labssite = Tools.getAdapter(LabsSite.class, doc, session);

        labssite.addContact("10057208");
        labssite.addContact("10087898");
        labssite.addContact("118999");

        List<String> contacts = labssite.getContacts();
        assertNotNull(contacts);
        assertEquals(3, contacts.size());
        assertTrue(contacts.contains("10057208"));
        assertTrue(contacts.contains("10087898"));
        assertTrue(contacts.contains("118999"));
    }

    @Test
    public void iCanAddContact() throws Exception {
        DocumentModel doc = createSite("NameSite1");
        LabsSite labssite = Tools.getAdapter(LabsSite.class, doc, session);

        boolean success = labssite.addContact("10057208");
        assertTrue(success);
    }

    @Test
    public void iCanDeleteContact() throws Exception {
        DocumentModel doc = createSite("NameSite1");
        LabsSite labssite = Tools.getAdapter(LabsSite.class, doc, session);

        labssite.addContact("10057208");
        labssite.addContact("10087898");
        labssite.addContact("118999");

        List<String> contacts = labssite.getContacts();
        assertNotNull(contacts);
        assertEquals(3, contacts.size());

        labssite.deleteContact("10087898");
        contacts = labssite.getContacts();
        assertNotNull(contacts);
        assertEquals(2, contacts.size());
        assertTrue(contacts.contains("10057208"));
        assertTrue(contacts.contains("118999"));
    }

    private CoreSession changeUser(String username) throws ClientException {
        CoreFeature coreFeature = featuresRunner.getFeature(CoreFeature.class);
        Map<String, Serializable> ctx = new HashMap<String, Serializable>();
        ctx.put("username", username);
        CoreSession userSession = LocalSession.createInstance();
        userSession.connect(coreFeature.getRepository().getName(), ctx);
        return userSession;
    }

    private DocumentModel createSite(final String siteName)
            throws ClientException {
        DocumentModel site = session.createDocumentModel("/", siteName,
                LABSSITE_TYPE);
        site.setPropertyValue("dc:title", "le titre");
        site = session.createDocument(site);
        return site;
    }

    private LabsSite createLabsSite(final String siteName)
            throws ClientException {
		final LabsSite site = Tools.getAdapter(LabsSite.class, createSite(siteName), session);
        return site;
    }

    private LabsSite createTemplateSite(final String siteName) throws ClientException, SiteManagerException {
        LabsSite site = sm.createSite(session, siteName, siteName);
        site.setElementTemplate(true);
        session.saveDocument(site.getDocument());
        DocumentModel assetsDoc = site.getAssetsDoc();
        DocumentModel folder1 = session.createDocumentModel(
                assetsDoc.getPathAsString(), "folder1", "Folder");
        session.createDocument(folder1);
        DocumentModel folder2 = session.createDocumentModel(
                assetsDoc.getPathAsString(), "folder2", "Folder");
        session.createDocument(folder2);
        DocumentModel asset1 = session.createDocumentModel(
                folder1.getPathAsString(), "asset1", "File");
        session.createDocument(asset1);
        DocumentModel asset2 = session.createDocumentModel(
                folder2.getPathAsString(), "asset2", "File");
        session.createDocument(asset2);
        site.getTemplate().setTemplateName(PAGE_TEMPLATE_CUSTOM2);
        session.saveDocument(site.getDocument());
        DocumentModel htmlPage = session.createDocumentModel(Docs.HTMLPAGE.type());
        htmlPage.setPathInfo(site.getTree().getPathAsString(), "pagehtml");
        htmlPage = session.createDocument(htmlPage);
        LabsTemplate templatedPage = Tools.getAdapter(LabsTemplate.class, htmlPage, session);
        templatedPage.setTemplateName(PAGE_TEMPLATE_CUSTOM);
        session.saveDocument(htmlPage);
        
        DocumentModel pageNews = session.createDocumentModel(Docs.PAGENEWS.type());
        pageNews.setPathInfo(site.getTree().getPathAsString(), "pagenews");
        pageNews = session.createDocument(pageNews);
        
        return site;
    }
}
