package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(user="Administrator")
public class SiteCreationEventListenerTest {

    @Inject
    private CoreSession session;

    @Test
    public void treeAndAssetsIsCreatedUnderSite() throws Exception {
        DocumentModel sitesRoot = session.getDocument(new PathRef("/"
                + LabsSiteConstants.Docs.DEFAULT_DOMAIN.docName() + "/"
                + LabsSiteConstants.Docs.SITESROOT.docName()));
        assertTrue(session.exists(sitesRoot.getRef()));

        DocumentModel site1 = session.createDocumentModel(
                sitesRoot.getPathAsString(), SiteFeatures.SITE_NAME,
                LabsSiteConstants.Docs.SITE.type());
        // when the "site" is created, an event is fired
        site1 = session.createDocument(site1);

        assertTrue(session.exists(site1.getRef()));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + LabsSiteConstants.Docs.TREE.docName())));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + LabsSiteConstants.Docs.ASSETS.docName())));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + LabsSiteConstants.Docs.TREE.docName() + "/"
                + LabsSiteConstants.Docs.WELCOME.docName())));
    }

    @Test
    public void sidebarIsCreatedUnderSite() throws Exception {
        DocumentModel sitesRoot = session.getDocument(new PathRef("/"
                + LabsSiteConstants.Docs.DEFAULT_DOMAIN.docName() + "/"
                + LabsSiteConstants.Docs.SITESROOT.docName()));

        DocumentModel site1 = session.createDocumentModel(
                sitesRoot.getPathAsString(), SiteFeatures.SITE_NAME,
                LabsSiteConstants.Docs.SITE.type());
        // when the "site" is created, an event is fired
        site1 = session.createDocument(site1);

        PathRef pathRefSidebar = new PathRef(site1.getPathAsString() + "/"
                + LabsSiteConstants.Docs.SIDEBAR.docName());
		assertTrue(session.exists(pathRefSidebar));
    }

    @Test
    public void treeNotCreatedUnderFolder() throws Exception {
        DocumentModel sitesRoot = session.getDocument(new PathRef("/"
                + LabsSiteConstants.Docs.DEFAULT_DOMAIN.docName() + "/"
                + LabsSiteConstants.Docs.SITESROOT.docName()));
        assertTrue(session.exists(sitesRoot.getRef()));
        DocumentModel folder = session.createDocumentModel(
                sitesRoot.getPathAsString(), "folder", "Folder");
        folder = session.createDocument(folder);
        assertNotNull(folder);
        DocumentModelList children = session.getChildren(folder.getRef());
        assertTrue("Folder should NOT contains children.", children.isEmpty());
    }
}
