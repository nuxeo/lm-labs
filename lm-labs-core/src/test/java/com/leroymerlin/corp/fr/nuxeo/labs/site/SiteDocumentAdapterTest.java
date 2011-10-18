package com.leroymerlin.corp.fr.nuxeo.labs.site;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.OfmRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.PageClasseurPageRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(init = PageClasseurPageRepositoryInit.class)
public class SiteDocumentAdapterTest {

    @Inject
    CoreSession session;

    @Test
    public void iCanGetChildrenPagesOfTree() throws Exception {
        LabsSite site = getSiteManager().getSite(session, OfmRepositoryInit.SITE_URL);
        DocumentModel treeDocument = site.getTree();
        assertNotNull(treeDocument);
        SiteDocument tree = treeDocument.getAdapter(SiteDocument.class);
        assertNotNull(tree);
        DocumentModelList childrenPageDocuments = tree.getChildrenPageDocuments();
        assertEquals(2, childrenPageDocuments.size());
        assertEquals(tree.getChildrenPages().size(), childrenPageDocuments.size());
        DocumentModel pageDoc = childrenPageDocuments.get(0);
        SiteDocument page = pageDoc.getAdapter(SiteDocument.class);
        assertNotNull(page);
        assertTrue(page.getChildrenPageDocuments().isEmpty());
        assertTrue(page.getChildrenPages().isEmpty());
    }

    protected SiteManager getSiteManager() {
        try {
            return Framework.getService(SiteManager.class);
        } catch (Exception e) {
            return null;
        }
    }

}
