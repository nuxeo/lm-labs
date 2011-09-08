package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
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
import com.leroymerlin.corp.fr.nuxeo.labs.site.features.LabsWebAppFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.LoginPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.MesSitesPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitemapPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitesRootPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.WelcomePage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.repository.OfmRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features({ LabsWebAppFeature.class })
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = SitesRootPage.class, url = "http://localhost:8089/labssites")
@RepositoryConfig(init = OfmRepositoryInit.class)
@Jetty(port = 8089)
public class SitemapTest {
    @Inject
    SitesRootPage rootPage;

    @Before
    public void init() {
        rootPage.home();
        ensureLoggedIn();
    }

    @Test
    public void siteMapIsVisible() throws Exception {
        MesSitesPage mesSitesPage = rootPage.getMesSitesPage();
        assertTrue(mesSitesPage.containsSite("OFM"));
        WelcomePage welcomePage = mesSitesPage.welcomePage("OFM");
        assertNotNull(welcomePage);
        assertTrue(welcomePage.hasSidebar());
        SitemapPage sitemapPage = welcomePage.sitemapPage();
        assertTrue(sitemapPage.containsTreeview());
    }

    @Test
    public void actionsAreAvailable() throws Exception {
        SitemapPage sitemapPage = rootPage.getMesSitesPage().welcomePage("OFM").sitemapPage();
        sitemapPage.reduceTreeview();
        sitemapPage.switchToListview();
        // assertTrue(sitemapPage.containsListview());
        // sitemapPage.switchToTreeview();
        // assertTrue(sitemapPage.containsTreeview());
    }

    public void ensureLoggedIn() {
        LoginPage login = rootPage.getLoginPage();
        if (!login.isAuthenticated()) {
            login.ensureLogin("Administrator", "Administrator");
        }
    }
}
