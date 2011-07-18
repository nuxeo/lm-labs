package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.ISiteConstant;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
public class SiteCreationEventListenerTest {
    private static final String SITE_NAME = "site1";

    @Inject
    private CoreSession session;

    @Test
    public void canHandleEvent() throws Exception {
        DocumentModel sitesRoot = session.createDocumentModel("/",
                ISiteConstant.Type.SITE_ROOT,
                ISiteConstant.Type.SITE_ROOT);
        sitesRoot = session.createDocument(sitesRoot);

        DocumentModel site1 = session.createDocumentModel("/"
                + ISiteConstant.Type.SITE_ROOT, SITE_NAME,
                ISiteConstant.Type.SITE);
        // when the "site" is created, an event is fired
        site1 = session.createDocument(site1);
        session.save();

        assertTrue(session.exists(sitesRoot.getRef()));
        assertTrue(session.exists(site1.getRef()));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + ISiteConstant.Path.TREE)));
        assertTrue(session.exists(new PathRef(site1.getPathAsString() + "/"
                + ISiteConstant.Path.ASSETS)));
    }
}
