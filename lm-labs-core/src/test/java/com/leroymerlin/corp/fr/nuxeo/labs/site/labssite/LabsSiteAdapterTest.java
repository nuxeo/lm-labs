package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.blocs.ExternalURL;
import com.leroymerlin.corp.fr.nuxeo.labs.site.publisher.LabsPublisher;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

@RunWith(FeaturesRunner.class)
@Features(com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures.class)
@Deploy("com.leroymerlin.labs.core.test")
@RepositoryConfig(cleanup = Granularity.METHOD)
public class LabsSiteAdapterTest {

    private static final String LABSSITE_TYPE = LabsSiteConstants.Docs.SITE.type();

    @Inject
    private CoreSession session;

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

    @Ignore("temporarily") @Test()
    public void iCanGetHomePageRef() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        doc.setPropertyValue("dc:title", "le titre");
        doc.setPropertyValue("labssite:homePageRef", "123456");
        doc = session.createDocument(doc);

        LabsSite labsSite = doc.getAdapter(LabsSite.class);
        assertTrue(labsSite.getHomePageRef().equals("123456"));
    }

    @Ignore("temporarily") @Test()
    public void iCanSetHomePageRef() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        doc.setPropertyValue("dc:title", "le titre");
        doc = session.createDocument(doc);
        LabsSite labsSite = doc.getAdapter(LabsSite.class);
        labsSite.setHomePageRef("123456");
        doc = session.saveDocument(doc);
        session.save();

        doc = session.getDocument(new PathRef("/NameSite1"));
        labsSite = doc.getAdapter(LabsSite.class);
        assertTrue(labsSite.getHomePageRef().equals("123456"));
    }

    @Test
    public void iCanGetLastUpdatedDocs() throws Exception {
        // site
        DocumentModel site = session.createDocumentModel("/", "NameSite1",
                LABSSITE_TYPE);
        site = session.createDocument(site);
        // folder 1
        DocumentModel folder = session.createDocumentModel(
                site.getPathAsString()+"/"+LabsSiteConstants.Docs.TREE.docName(), "folder", "Folder");
        folder = session.createDocument(folder);
        // folder 2
        DocumentModel folder2 = session.createDocumentModel(
                site.getPathAsString()+"/"+LabsSiteConstants.Docs.TREE.docName(), "folder2", "Folder");
        folder = session.createDocument(folder2);
        session.save();

        LabsSite labsSite = site.getAdapter(LabsSite.class);
        DocumentModelList lastUpdatedDocs = labsSite.getLastUpdatedDocs();
        assertNotNull(lastUpdatedDocs);
        assertEquals(3, lastUpdatedDocs.size());

        // folder 3
        DocumentModel folder3 = session.createDocumentModel(
                site.getPathAsString()+"/"+LabsSiteConstants.Docs.TREE.docName(), "folder3", "Folder");
        folder = session.createDocument(folder3);
        session.save();

        lastUpdatedDocs = labsSite.getLastUpdatedDocs();
        assertEquals(4, lastUpdatedDocs.size());

        // hidden document
        DocumentModel hiddenFolder = session.createDocumentModel(
                site.getPathAsString()+"/"+LabsSiteConstants.Docs.TREE.docName(), "hiddenFolder", "HiddenFolder");
        folder = session.createDocument(hiddenFolder);
        session.save();

        lastUpdatedDocs = labsSite.getLastUpdatedDocs();
        assertEquals(4, lastUpdatedDocs.size());
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
}
