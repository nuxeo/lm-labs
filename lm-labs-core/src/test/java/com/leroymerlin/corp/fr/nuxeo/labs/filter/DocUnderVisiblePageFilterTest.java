package com.leroymerlin.corp.fr.nuxeo.labs.filter;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.local.LocalSession;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.features.directory.LMTestDirectoryFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features({ LMTestDirectoryFeature.class, SiteFeatures.class })
@Deploy("com.leroymerlin.labs.core.test")
public class DocUnderVisiblePageFilterTest {

    private final String USERNAME1 = "CGM";

    @Inject
    private CoreSession session;

    @Inject
    protected FeaturesRunner featuresRunner;

    // TODO vdu
    @Ignore("TODO") @Test
    public void test() {
        fail("Not yet implemented");
    }
    
    private CoreSession changeUser(String username) throws ClientException {
        CoreFeature coreFeature = featuresRunner.getFeature(CoreFeature.class);
        Map<String, Serializable> ctx = new HashMap<String, Serializable>();
        ctx.put("username", username);
        CoreSession userSession = LocalSession.createInstance();
        userSession.connect(coreFeature.getRepository().getName(), ctx);
        return userSession;
    }

}
