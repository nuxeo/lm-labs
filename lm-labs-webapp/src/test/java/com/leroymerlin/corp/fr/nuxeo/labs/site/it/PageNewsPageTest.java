package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import static org.junit.Assert.assertTrue;

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
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.PageNewsPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitesRootPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.repository.PageNewsRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = SitesRootPage.class, url = "http://localhost:8089/labssites")
@Jetty(port = 8089)
@RepositoryConfig(init = PageNewsRepositoryInit.class)

public class PageNewsPageTest {

    @Inject
    SitesRootPage rootPage;

    @Test
    public void pageIsReachable() throws Exception {
        PageNewsPage pageNews = getPageClasseur();
        assertTrue(pageNews.isLoaded());
    }
    
    @Test
    public void iCanAddNews() throws Exception {
        PageNewsPage pageNews = getPageClasseur();
        assertTrue(pageNews.canAddNews());
    }
    
    @Test
    public void iCanModifyAndDeleteNews() throws Exception {
        PageNewsPage pageNews = getPageClasseur();
        assertTrue(pageNews.canModifyAndDeleteNews());
    }

    
    private PageNewsPage getPageClasseur() {
        ensureLoggedIn();
        PageNewsPage pageNews = rootPage.goToPageNews("http://localhost:8089/labssites" + "/" + PageNewsRepositoryInit.SITE_URL, PageNewsRepositoryInit.PAGE_NEWS_TITLE);
        return pageNews;
    }
    
    public void ensureLoggedIn() {
        LoginPage login = rootPage.getLoginPage();
        if(!login.isAuthenticated()) {
            login.ensureLogin("Administrator", "Administrator");
        }
    }

}
