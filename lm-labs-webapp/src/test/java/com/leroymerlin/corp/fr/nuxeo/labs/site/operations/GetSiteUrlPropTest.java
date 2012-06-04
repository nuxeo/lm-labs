package com.leroymerlin.corp.fr.nuxeo.labs.site.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.features.LabsWebAppFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.OfmRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.PageClasseurPageRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Deploy({ "org.nuxeo.ecm.automation.core"
})
@RepositoryConfig(init = PageClasseurPageRepositoryInit.class)
@Jetty(port=9090)
public class GetSiteUrlPropTest extends AbstractTestOperation {

    @Inject
    AutomationService service;

    @Inject
    CoreSession session;

    @Test
    public void iCanGetSiteUrlProp() throws Exception {
        OperationContext ctx = new OperationContext(session);
        OperationChain chain = new OperationChain("test" + GetSiteUrlProp.ID);
        chain.add(GetSiteUrlProp.ID).set("docId", getSiteManager().getSite(session, OfmRepositoryInit.SITE_URL).getTree().getId());
        String url = (String) service.run(ctx, chain);
        assertNotNull(url);
        assertEquals(OfmRepositoryInit.SITE_URL, url);

    }

}
