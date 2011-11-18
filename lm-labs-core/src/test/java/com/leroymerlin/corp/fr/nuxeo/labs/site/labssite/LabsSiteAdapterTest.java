package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.LabsPublisher;
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
        DocumentModel doc = session.createDocumentModel("/", "NameSite1", LABSSITE_TYPE);

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
        DocumentModel doc = session.createDocumentModel("/", "NameSite1", LABSSITE_TYPE);
        doc = session.createDocument(doc);
        LabsSite labssite = doc.getAdapter(LabsSite.class);
        assertTrue(labssite.isAdministrator("Administrator"));
        assertFalse(labssite.isAdministrator("CGM"));
    }

    @Test
    public void iCanCreateALabsSiteAdapter() throws Exception {
      //Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "NameSite1",LABSSITE_TYPE);

        LabsSite labssite = doc.getAdapter(LabsSite.class);
        assertThat(labssite,is(notNullValue()));
        labssite.setTitle("Le titre du site");

        //Persist document in db
        doc = session.createDocument(doc);

        //Commit
        session.save();

        doc = session.getDocument(new PathRef("/NameSite1"));
        labssite = doc.getAdapter(LabsSite.class);
        assertThat(labssite,is(notNullValue()));
        assertThat(labssite.getTitle(), is("Le titre du site"));

    }

    @Test
    public void iCanGetPropertiesOnLabsSiteAdapter() throws Exception {
      //Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "nameSite1",LABSSITE_TYPE);

        doc.setPropertyValue("dc:creator", "creator");

        LabsSite labssite = doc.getAdapter(LabsSite.class);
        assertThat(labssite,is(notNullValue()));
        labssite.setTitle("Le titre du site");
        labssite.setDescription("Description");
        labssite.setURL("URL");
        Blob blob = getTestBlob();
        labssite.setLogo(blob);

        //Persist document in db
        doc = session.createDocument(doc);

        //Commit
        session.save();

        doc = session.getDocument(new PathRef("/nameSite1"));
        labssite = doc.getAdapter(LabsSite.class);
        assertThat(labssite,is(notNullValue()));
        assertThat(labssite.getTitle(), is("Le titre du site"));
        assertThat(labssite.getDescription(), is("Description"));
        assertThat(labssite.getURL(), is("URL"));
        assertThat(labssite.getLogo(), is(notNullValue()));
        assertEquals(labssite.getLogo().getFilename(), blob.getFilename());
        assertEquals(labssite.getLogo().getLength(), blob.getLength());

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
        DocumentModel doc = session.createDocumentModel("/", "NameSite1", LABSSITE_TYPE);
        doc.setPropertyValue("dc:title", "le titre");
        doc = session.createDocument(doc);
        session.save();
        assertTrue(!doc.getAdapter(LabsPublisher.class).isVisible());
    }

    @Test()
    public void iCanDisplayAPublishedSite() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1", LABSSITE_TYPE);
        doc.setPropertyValue("dc:title", "le titre");
        doc = session.createDocument(doc);
        doc.getAdapter(LabsPublisher.class).publish();
        assertTrue(doc.getAdapter(LabsPublisher.class).isVisible());
    }

    @Test()
    public void iCanHaveDeletedTagOnADeletedSite() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1", LABSSITE_TYPE);
        doc.setPropertyValue("dc:title", "le titre");
        doc = session.createDocument(doc);
        doc.getAdapter(LabsPublisher.class).delete();
        assertTrue(doc.getAdapter(LabsPublisher.class).isDeleted());
    }

    @Test()
    public void iCantHaveDeletedTagOnAOtherDeletedSite() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1", LABSSITE_TYPE);
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
        DocumentModel doc = session.createDocumentModel("/", "NameSite1", LABSSITE_TYPE);
        doc.setPropertyValue("dc:title", "le titre");
        doc.setPropertyValue("labssite:homePageRef", "123456");
        doc = session.createDocument(doc);
        
        LabsSite labsSite = doc.getAdapter(LabsSite.class);
        assertTrue(labsSite.getHomePageRef().equals("123456"));
    }

    @Test()
    public void iCanSetHomePageRef() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "NameSite1", LABSSITE_TYPE);
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
}
