package com.leroymerlin.corp.fr.nuxeo.labs.site.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.features.LabsWebAppFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.OfmRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.PageClasseurPageRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Deploy({ "org.nuxeo.ecm.automation.core"
})
@RepositoryConfig(init = PageClasseurPageRepositoryInit.class)
@Jetty(port=9090)
public class LastUploadsOperationTest {

    @Inject
    AutomationService service;

    @Inject
    CoreSession session;

    @Ignore("I don't know why I cannot add a blob to folder1 ...") @Test
    public void iCanGetLastUploads() throws Exception {
//        Blob blob = new FileBlob(getClass().getResourceAsStream("/" + PageClasseurPageRepositoryInit.FILE1_NAME));
//        blob.setFilename(PageClasseurPageRepositoryInit.FILE1_NAME);
//        PageClasseurPageRepositoryInit.addBlobToFolder1(session, blob, PageClasseurPageRepositoryInit.FILE1_DESCRIPTION);
//        session.save();
        
        OperationContext ctx = new OperationContext(session);
        OperationChain chain = new OperationChain("test" + LastUploads.ID);
        chain.add(LastUploads.ID).set("docId", getSiteManager().getSite(session, OfmRepositoryInit.SITE_URL).getTree().getId()).set("pageSize", new Integer(5));
        @SuppressWarnings("unchecked")
        List<DocumentModel> uploads = (List<DocumentModel>) service.run(ctx, chain);
        assertFalse(uploads.isEmpty());
        assertEquals(1, uploads.size());
    }
    
    protected SiteManager getSiteManager() {
        try {
            return Framework.getService(SiteManager.class);
        } catch (Exception e) {
            return null;
        }
    }

}
