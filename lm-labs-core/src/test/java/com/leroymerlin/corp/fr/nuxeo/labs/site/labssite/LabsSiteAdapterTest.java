package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.leroymerlin.corp.fr.nuxeo.labs.site.blocs.ExternalURL;
import com.leroymerlin.corp.fr.nuxeo.labs.site.publisher.LabsPublisher;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.PermissionsHelper;

@RunWith(FeaturesRunner.class)
@Features({ LMTestDirectoryFeature.class, SiteFeatures.class })
@Deploy("com.leroymerlin.labs.core.test")
@RepositoryConfig(cleanup = Granularity.METHOD)
public class LabsSiteAdapterTest {

    private static final String LABSSITE_TYPE = LabsSiteConstants.Docs.SITE.type();
    private final String USERNAME1 = "CGM";

    @Inject
    private CoreSession session;

    @Inject
    protected FeaturesRunner featuresRunner;
    
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
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        doc = session.createDocument(doc);
        LabsSite labssite = doc.getAdapter(LabsSite.class);
        assertTrue(labssite.isAdministrator("Administrator"));
        assertFalse(labssite.isAdministrator("CGM"));
    }

    @Test
    public void iCanCreateALabsSiteAdapter() throws Exception {
        // Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);

        LabsSite labssite = doc.getAdapter(LabsSite.class);
        assertThat(labssite, is(notNullValue()));
        labssite.setTitle("Le titre du site");

        // Persist document in db
        doc = session.createDocument(doc);

        // Commit
        session.save();

        doc = session.getDocument(new PathRef("/NameSite1"));
        labssite = doc.getAdapter(LabsSite.class);
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

        LabsSite labssite = doc.getAdapter(LabsSite.class);
        assertThat(labssite, is(notNullValue()));
        labssite.setTitle("Le titre du site");
        labssite.setDescription("Description");
        labssite.setURL("URL");
        doc = session.saveDocument(doc);
        Blob blob = getTestBlob();
        labssite.setBanner(blob);

        session.save();

        doc = session.getDocument(new PathRef("/nameSite1"));
        labssite = doc.getAdapter(LabsSite.class);
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
        LabsSite labsSite = site.getAdapter(LabsSite.class);
        DocumentModel welcome = session.getChild(labsSite.getTree().getRef(), Docs.WELCOME.docName());

        assertEquals(welcome.getId(), labsSite.getHomePageRef());
    }

    @Test()
    public void iCanSetHomePageRef() throws Exception {
        DocumentModel site = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        site.setPropertyValue("dc:title", "le titre");
        site = session.createDocument(site);
        LabsSite labsSite = site.getAdapter(LabsSite.class);
        DocumentModel page = session.createDocumentModel(labsSite.getTree().getPathAsString(), "page", Docs.HTMLPAGE.type());
        page = session.createDocument(page);
        labsSite.setHomePageRef(page.getId());
        site = session.saveDocument(site);
        session.save();

        site = session.getDocument(new PathRef("/NameSite1"));
        labsSite = site.getAdapter(LabsSite.class);
        assertTrue(labsSite.getHomePageRef().equals(page.getId()));
    }

    @Test
    public void iCanGetLastUpdatedDocs() throws Exception {
        // site
        DocumentModel site = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        site = session.createDocument(site);
        session.save();
        PermissionsHelper.addPermission(site, SecurityConstants.READ, USERNAME1, true);
        assertTrue(PermissionsHelper.hasPermission(site, SecurityConstants.READ, USERNAME1));
        session.save();
        DocumentModel page = session.createDocumentModel(
                site.getPathAsString()+"/"+LabsSiteConstants.Docs.TREE.docName(), "page1", Docs.PAGECLASSEUR.type());
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
        LabsSite labsSite = site.getAdapter(LabsSite.class);
        DocumentModelList lastUpdatedDocs = labsSite.getLastUpdatedDocs();

        CoreSession userSession = changeUser(USERNAME1);
        DocumentModel userSite = userSession.getDocument(new PathRef("/NameSite1"));
        LabsSite userLabsSite = userSite.getAdapter(LabsSite.class);
        lastUpdatedDocs = userLabsSite.getLastUpdatedDocs();
        assertEquals(0, lastUpdatedDocs.size());
        CoreInstance.getInstance().close(userSession);
        
        LabsPublisher publisher = page.getAdapter(LabsPublisher.class);
        publisher.publish();
        page = session.saveDocument(page);
        session.save();
        userSession = changeUser(USERNAME1);
        userSite = userSession.getDocument(new PathRef("/NameSite1"));
        userLabsSite = userSite.getAdapter(LabsSite.class);
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
    public void iCanGetExternalURLs() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        doc.setPropertyValue("dc:title", "le titre");
        doc = session.createDocument(doc);
        LabsSite labsSite = doc.getAdapter(LabsSite.class);
        assertTrue(labsSite.getExternalURLs().isEmpty());
        labsSite.createExternalURL("b").setURL("www.b.org");
        labsSite.createExternalURL("a").setURL("www.a.org");
        ArrayList<ExternalURL> list = labsSite.getExternalURLs();
        assertEquals(2, list.size());
        assertEquals("a", list.get(0).getName());
    }
    
    private CoreSession changeUser(String username) throws ClientException {
        CoreFeature coreFeature = featuresRunner.getFeature(CoreFeature.class);
        Map<String, Serializable> ctx = new HashMap<String, Serializable>();
        ctx.put("username", username);
        CoreSession userSession = LocalSession.createInstance();
        userSession.connect(coreFeature.getRepository().getName(), ctx);
        return userSession;
    }
}
