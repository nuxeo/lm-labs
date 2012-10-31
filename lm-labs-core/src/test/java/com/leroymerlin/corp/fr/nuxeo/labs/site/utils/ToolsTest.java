package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.features.directory.LMTestDirectoryFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.base.LabsSession;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.publisher.LabsPublisher;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.DefaultRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

@RunWith(FeaturesRunner.class)
@Features({ LMTestDirectoryFeature.class, SiteFeatures.class })
@Deploy("com.leroymerlin.labs.core.test")
@RepositoryConfig(user = "Administrator",init=DefaultRepositoryInit.class, cleanup=Granularity.METHOD)
public class ToolsTest {

    @Inject
    private CoreSession session;

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
    public void hasInterfaceLabsSession() throws Exception {
        generateSite();

        final DocumentModel site1 = session.getDocument(new PathRef("/"
                + Docs.DEFAULT_DOMAIN.docName() + "/"
                + Docs.SITESROOT.docName() + "/" + SiteFeatures.SITE_NAME));

        LabsSite site  = Tools.getAdapter(LabsSite.class, site1, session);
        assertTrue(Tools.hasInterface(site.getClass(), LabsSession.class));
    }
	
    @Test
    public void hasNoInterfaceLabsSession() throws Exception {
        generateSite();

        final DocumentModel site1 = session.getDocument(new PathRef("/"
                + Docs.DEFAULT_DOMAIN.docName() + "/"
                + Docs.SITESROOT.docName() + "/" + SiteFeatures.SITE_NAME));

        LabsPublisher publish  = site1.getAdapter(LabsPublisher.class);
        assertFalse(Tools.hasInterface(publish.getClass(), LabsSession.class));
    }

}
