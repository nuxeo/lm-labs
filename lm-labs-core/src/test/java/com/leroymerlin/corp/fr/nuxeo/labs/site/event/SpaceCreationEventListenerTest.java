package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@Deploy({ "org.nuxeo.ecm.opensocial.spaces",
    "com.leroymerlin.labs.core.test:OSGI-INF/core-types-contribTest.xml"
    })
public class SpaceCreationEventListenerTest {

    @Inject
    private CoreSession session;

    @Test
    public void spaceDocumentHasSchemaPage() throws Exception {
        DocumentModel sitesRoot = session.getDocument(new PathRef("/"
                + LabsSiteConstants.Docs.DEFAULT_DOMAIN.docName() + "/"
                + LabsSiteConstants.Docs.SITESROOT.docName()));
        DocumentModel site1 = session.createDocumentModel(
                sitesRoot.getPathAsString(), SiteFeatures.SITE_NAME,
                LabsSiteConstants.Docs.SITE.type());
        site1 = session.createDocument(site1);
        DocumentModel tree = session.getDocument(new PathRef(site1.getPathAsString() + "/"
                + LabsSiteConstants.Docs.TREE.docName()));
        DocumentModel page = session.createDocumentModel(
                tree.getPathAsString(), "Dashboard",
                LabsSiteConstants.Docs.DASHBOARD.type());
        page = session.createDocument(page);
        assertTrue(page.hasSchema(Schemas.PAGE.getName()));
    }
    
    @Test
    public void spaceDocumentHasFacetCommentable() throws Exception {
        DocumentModel sitesRoot = session.getDocument(new PathRef("/"
                + LabsSiteConstants.Docs.DEFAULT_DOMAIN.docName() + "/"
                + LabsSiteConstants.Docs.SITESROOT.docName()));
        DocumentModel site1 = session.createDocumentModel(
                sitesRoot.getPathAsString(), SiteFeatures.SITE_NAME,
                LabsSiteConstants.Docs.SITE.type());
        site1 = session.createDocument(site1);
        DocumentModel tree = session.getDocument(new PathRef(site1.getPathAsString() + "/"
                + LabsSiteConstants.Docs.TREE.docName()));
        DocumentModel page = session.createDocumentModel(
                tree.getPathAsString(), "Dashboard",
                LabsSiteConstants.Docs.DASHBOARD.type());
        page = session.createDocument(page);
        assertTrue(page.hasFacet(org.nuxeo.ecm.core.schema.FacetNames.COMMENTABLE));
    }
}
