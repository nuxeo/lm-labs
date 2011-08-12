package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import static org.junit.Assert.*;

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
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.PageClasseurAddFolderPopup;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.PageClasseurPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitesRootPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.WelcomePage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.repository.PageClasseurPageRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Deploy({
    "org.nuxeo.ecm.platform.types.api",
    "org.nuxeo.ecm.platform.types.core",
    "org.nuxeo.ecm.platform.mimetype.api",
    "org.nuxeo.ecm.platform.mimetype.core",
    "org.nuxeo.ecm.platform.filemanager.api",
    "org.nuxeo.ecm.platform.filemanager.core"
})
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = SitesRootPage.class, url = "http://localhost:8089/labssites")
@RepositoryConfig(init = PageClasseurPageRepositoryInit.class)
@Jetty(port = 8089)
public class PageClasseurPageTest {

    @Inject SitesRootPage rootPage;
    
    @Test
    public void pageIsReachable() throws Exception {
        PageClasseurPage pageClasseur = getPageClasseur();
        assertTrue(pageClasseur.isLoaded());
    }
    
    @Test
    public void pageClasseurHasSubFolder() throws Exception {
        PageClasseurPage pageClasseur = getPageClasseur();
        assertTrue(pageClasseur.hasFolder(PageClasseurPageRepositoryInit.FOLDER1_NAME));
    }

    @Ignore @Test
    public void addFolder() throws Exception {
        PageClasseurPage pageClasseur = getPageClasseur();
        PageClasseurAddFolderPopup popup = pageClasseur.clickAddFolder();
        popup.setTitle("folder 2");
        pageClasseur = popup.validate();
        assertTrue(pageClasseur.hasFolder("folder 2"));
    }
    
    private PageClasseurPage getPageClasseur() {
        ensureLoggedIn();
        PageClasseurPage pageClasseur = rootPage.goToPageClasseur("http://localhost:8089/labssites" + "/" + PageClasseurPageRepositoryInit.SITE_URL, PageClasseurPageRepositoryInit.PAGE_CLASSEUR_TITLE);
        return pageClasseur;
    }

    public void ensureLoggedIn() {
        LoginPage login = rootPage.getLoginPage();
        if(!login.isAuthenticated()) {
            login.ensureLogin("Administrator", "Administrator");
        }
    }
    
}
