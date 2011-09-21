package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;
import org.nuxeo.runtime.test.runner.web.Browser;
import org.nuxeo.runtime.test.runner.web.BrowserFamily;
import org.nuxeo.runtime.test.runner.web.HomePage;
import org.openqa.selenium.Dimension;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Tools;
import com.leroymerlin.corp.fr.nuxeo.labs.site.features.LabsWebAppFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.LoginPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.MesSitesPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitesRootPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.WelcomePage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.repository.AllDocTypeRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = MesSitesPage.class, url = "http://localhost:8089/labssites")
@RepositoryConfig(init = AllDocTypeRepositoryInit.class)
@Jetty(port = 8089)
@Deploy({ "com.leroymerlin.corp.fr.nuxeo.portal.user",
"org.nuxeo.ecm.platform.usermanager" })
public class WelcomePageTest {

    private static final String URL_ORDER = "1";
    private static final String URL_URL = "http://www.yahoo.fr";
    private static final String URL_URL_MODIFY = "http://www.google.fr";
    private static final String URL_NAME = "yahoo";
    private static final String URL_NAME_MODIFY = "google";
    @Inject MesSitesPage mesSitesPage;
    
    @Test
    public void pageIsReachable() throws Exception {
        ensureLoggedIn();
        assertTrue(mesSitesPage.containsSite(AllDocTypeRepositoryInit.SITE_TITLE));
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        assertNotNull(welcomePage);
        assertTrue(welcomePage.hasSidebar());
        mesSitesPage.home();
    }
    
    @Ignore("welcome page should not have any blocs, welcome page is a work in progress.") @Test
    public void pageDoesNotHaveBlocs() throws Exception {
        ensureLoggedIn();
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        assertNotNull(welcomePage);
        assertFalse(welcomePage.hasBlocs());
    }

    @Test
    public void pageHasSidebar() throws Exception {
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        assertTrue(welcomePage.hasSidebar());
    }

    @Test
    public void pageHasExternalURL() throws Exception {
        mesSitesPage.home();
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        assertTrue(welcomePage.hasExternalURL());
    }

    @Test
    public void pageHasAddExternalURL() throws Exception {
        mesSitesPage.home();
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        assertTrue(welcomePage.hasAddExternalURL());
    }

    @Test
    public void iAddExternalURL() throws Exception {
        mesSitesPage.home();
//        ensureLoggedIn();
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        welcomePage.clickAddExternalURL();
        Tools.sleep(3000);
        welcomePage.setFieldsExternalURL(URL_NAME, URL_URL, URL_ORDER);
        welcomePage.clickSubmitExternalURL();
        Tools.sleep(3000);
        assertTrue(welcomePage.hasMyExternalURL(URL_NAME, URL_URL));
    }

    @Test
    public void iModifyExternalURL() throws Exception {
        mesSitesPage.home();
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        welcomePage.clickModifyExternalURL();
        Tools.sleep(3000);
        welcomePage.setFieldsExternalURL(URL_NAME_MODIFY, URL_URL_MODIFY, URL_ORDER);
        welcomePage.clickSubmitExternalURL();
        Tools.sleep(3000);
        assertTrue(welcomePage.hasMyExternalURL(URL_NAME_MODIFY, URL_URL_MODIFY));
    }

    @Test
    public void iDeleteExternalURL() throws Exception {
        mesSitesPage.home();
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        welcomePage.clickDeleteExternalURL();
        Tools.sleep(3000);
        assertFalse(welcomePage.hasMyExternalURL(URL_NAME_MODIFY, URL_URL_MODIFY));
    }

    @Test
    public void iCanModifyBanner() throws Exception {
        mesSitesPage.home();
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        assertTrue(welcomePage.canModifyBanner());
    }

    @Test
    public void iCanDeleteBanner() throws Exception {
        mesSitesPage.home();
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        assertTrue(welcomePage.canDeleteBanner());
    }

    @Test
    public void iHaveADefaultBanner() throws Exception {
        mesSitesPage.home();
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        Tools.sleep(3000);
        assertTrue(welcomePage.hasOKBanner(new Dimension(959, 79)));
    }

    @Test
    public void iModifyBanner() throws Exception {
        mesSitesPage.home();
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        assertTrue(welcomePage.canModifyBanner());
        welcomePage.clickModifyBanner();
        welcomePage.setFieldsBanner(getTestFile());
        welcomePage.clickSubmitBanner();
        Tools.sleep(3000);
        assertTrue(welcomePage.hasOKBanner(new Dimension(561, 191)));
    }

    @Test
    public void iDeleteBanner() throws Exception {
        mesSitesPage.home();
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        assertTrue(welcomePage.canDeleteBanner());
        welcomePage.clickDeleteBanner();
        Tools.sleep(3000);
        assertTrue(welcomePage.hasOKBanner(new Dimension(959, 79)));
    }
    
    @Test
    public void iDontHaveAnyUpload() throws Exception {
        mesSitesPage.home();
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        assertFalse(welcomePage.hasLatestUploads());
    }
    
    @Test
    public void iHaveOneLatestUpload() throws Exception {
        
    }
    
    @Test
    public void iCanSearchWelcomePage() throws Exception {
        mesSitesPage.home();
        WelcomePage welcomePage = mesSitesPage.welcomePage(AllDocTypeRepositoryInit.SITE_TITLE);
        SearchResultsPage searchResultsPage = welcomePage.search("Welcome");
        assertEquals(1, searchResultsPage.getNbrResults());
    }
    
    private static File getTestFile() {
        return new File(
                FileUtils.getResourcePathFromContext("testFiles/vision.jpg"));
    }
    
    public void ensureLoggedIn() {
        LoginPage login = mesSitesPage.getLoginPage();
        if(!login.isAuthenticated()) {
            login.ensureLogin("Administrator", "Administrator");
        }
        mesSitesPage.home();
    }

}
