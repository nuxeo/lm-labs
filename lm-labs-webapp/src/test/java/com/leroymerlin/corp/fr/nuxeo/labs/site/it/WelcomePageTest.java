package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;
import org.nuxeo.runtime.test.runner.web.Browser;
import org.nuxeo.runtime.test.runner.web.BrowserFamily;
import org.nuxeo.runtime.test.runner.web.HomePage;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.features.LabsWebAppFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.LoginPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.MesSitesPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitesRootPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.WelcomePage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.repository.OfmRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = SitesRootPage.class, url = "http://localhost:8089/labssites")
@RepositoryConfig(init = OfmRepositoryInit.class)
@Jetty(port = 8089)
@Deploy({ "com.leroymerlin.corp.fr.nuxeo.portal.user",
"org.nuxeo.ecm.platform.usermanager" })
public class WelcomePageTest {

    @Inject SitesRootPage rootPage;
    
    @Test
    public void pageIsReachable() throws Exception {
        ensureLoggedIn();
        MesSitesPage mesSitesPage = rootPage.getMesSitesPage();
        assertTrue(mesSitesPage.containsSite("OFM"));
        WelcomePage welcomePage = mesSitesPage.welcomePage("OFM");
        assertNotNull(welcomePage);
        assertTrue(welcomePage.hasSidebar());
    }
    
    @Ignore("TODO") @Test
    public void pageDoesNotHaveBlocs() throws Exception {
        MesSitesPage mesSitesPage = rootPage.getMesSitesPage();
        WelcomePage welcomePage = mesSitesPage.welcomePage("OFM");
        assertFalse(welcomePage.hasBlocs());
    }

    @Ignore("TODO") @Test
    public void pageHasSidebar() throws Exception {
        MesSitesPage mesSitesPage = rootPage.getMesSitesPage();
        WelcomePage welcomePage = mesSitesPage.welcomePage("OFM");
        assertTrue(welcomePage.hasSidebar());
    }
    
    public void ensureLoggedIn() {
        LoginPage login = rootPage.getLoginPage();
        if(!login.isAuthenticated()) {
            login.ensureLogin("Administrator", "Administrator");
        }
    }

}
