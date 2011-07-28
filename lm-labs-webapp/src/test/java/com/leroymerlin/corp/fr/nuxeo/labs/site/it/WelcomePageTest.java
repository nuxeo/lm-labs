package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;
import org.nuxeo.runtime.test.runner.web.Browser;
import org.nuxeo.runtime.test.runner.web.BrowserFamily;
import org.nuxeo.runtime.test.runner.web.HomePage;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = LabsSiteWelcomePage.class, url = "http://localhost:8084/labssites/site-ofm/welcome")
@Jetty(port = 8084)
@RepositoryConfig(init = OfmRepositoryInit.class)
@Ignore
public class WelcomePageTest {

    @Inject
    LabsSiteWelcomePage ofmWelcomePage;

    @Test
    public void displayWelcomePage() throws Exception {
        assertTrue(ofmWelcomePage.hasMainDiv());
    }
}
