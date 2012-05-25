package com.leroymerlin.corp.fr.nuxeo.labs.site.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.features.LabsWebAppFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.OfmRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.PageClasseurPageRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Deploy({ "org.nuxeo.ecm.automation.core"
})
@RepositoryConfig(init = PageClasseurPageRepositoryInit.class)
@Jetty(port=9090)
public class GetChildrenPagesOperationTest extends AbstractTestOperation {

    @Inject
    AutomationService service;

    @Inject
    CoreSession session;

    @Test
    public void iCanGetChildrenPagesOfTreeUsingDocId() throws Exception {
        LabsSite site = getSiteManager().getSite(session, OfmRepositoryInit.SITE_URL);
        DocumentModel treeDocument = site.getTree(session);
        assertNotNull(treeDocument);
        OperationContext ctx = new OperationContext(session);
        OperationChain chain = new OperationChain("test" + GetChildrenPages.ID);
        chain.add(GetChildrenPages.ID).set("docId", treeDocument.getId());
        @SuppressWarnings("unchecked")
        List<DocumentModel> operationChildrenPageDocuments = (List<DocumentModel>) service.run(ctx, chain);
        assertEquals(2, operationChildrenPageDocuments.size());
    }

    @Test
    public void iCanGetChildrenPagesOfTreeUsingDocPath() throws Exception {
        LabsSite site = getSiteManager().getSite(session, OfmRepositoryInit.SITE_URL);
        DocumentModel treeDocument = site.getTree(session);
        assertNotNull(treeDocument);
        OperationContext ctx = new OperationContext(session);
        OperationChain chain = new OperationChain("test" + GetChildrenPages.ID);
        chain.add(GetChildrenPages.ID).set("docPath", treeDocument.getPathAsString());
        @SuppressWarnings("unchecked")
        List<DocumentModel> operationChildrenPageDocuments = (List<DocumentModel>) service.run(ctx, chain);
        assertEquals(2, operationChildrenPageDocuments.size());
    }

}
