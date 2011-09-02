package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;
import org.nuxeo.runtime.test.runner.web.Browser;
import org.nuxeo.runtime.test.runner.web.BrowserFamily;
import org.nuxeo.runtime.test.runner.web.HomePage;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Tools;
import com.leroymerlin.corp.fr.nuxeo.labs.site.features.LabsWebAppFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.CreateSitePopup;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.LoginPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.MesSitesPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitesRootPage;

@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = SitesRootPage.class, url = "http://localhost:8089/labssites")
@Jetty(port = 8089)
@Deploy({ "com.leroymerlin.corp.fr.nuxeo.portal.user",
"org.nuxeo.ecm.platform.usermanager" })
public class SitesRootPageTest {

    private static final String TITLE = "title";

    private static final String URL = "url";

    private static final String DESCRIPTION = "description";

    @Inject SitesRootPage homePage;
    
    @Rule public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void pageIsReachable() throws Exception {
        assertTrue(homePage.isloaded());
    }    
    
    @Test
    public void iCannotCreateSiteAsInvite() throws Exception {
        assertFalse(homePage.canClickAddSite());
    }
    
    @Test
    public void iCreateSiteforTestCRUD() throws Exception {
        ensureLoggedIn();
        CreateSitePopup popup = homePage.createSite();
        popup.setTitle("OFM");
        popup.setURL("ofm");
        popup.setDescription("site Supply Chain OFM");
        MesSitesPage mesSitesPage = popup.validate();
        assertTrue(mesSitesPage.containsSite("OFM"));
    }
    
    @Test
    public void iCanModifySite() throws Exception {
        homePage.home();
        MesSitesPage mesSitesPage = homePage.getMesSitesPage();
        assertTrue(mesSitesPage.canModify());
    }
    
    @Test
    public void iModifySite() throws Exception {
        homePage.home();
        MesSitesPage mesSitesPage = homePage.getMesSitesPage();
        mesSitesPage.clickModify();
        Tools.sleep(3000);
        mesSitesPage.setFields(TITLE, URL, DESCRIPTION);
        mesSitesPage.clickSubmit();
        Tools.sleep(3000);
        assertTrue(mesSitesPage.hasModifiedLasSite(TITLE, URL, DESCRIPTION));
    }
    
    @Test
    public void iCanDeleteSite() throws Exception {
        homePage.home();
        MesSitesPage mesSitesPage = homePage.getMesSitesPage();
        assertTrue(mesSitesPage.canModify());
        
    }
    
    @Test
    public void iDeleteSite() throws Exception {
        homePage.home();
        MesSitesPage mesSitesPage = homePage.getMesSitesPage();
        mesSitesPage.clickDelete();
        Tools.sleep(3000);
        assertFalse(mesSitesPage.hasModifiedLasSite(TITLE, URL, DESCRIPTION));
    }
    
    @Test
    public void iCanCreateSite() throws Exception {
        homePage.home();
        CreateSitePopup popup = homePage.createSite();
        popup.setTitle("OFM");
        popup.setURL("ofm");
        popup.setDescription("site Supply Chain OFM");
        MesSitesPage mesSitesPage = popup.validate();
        assertTrue(mesSitesPage.containsSite("OFM"));
        
    }
    
    public void ensureLoggedIn() {
        LoginPage login = homePage.getLoginPage();
        if(!login.isAuthenticated()) {
            login.ensureLogin("Administrator", "Administrator");
        }
    }
}
