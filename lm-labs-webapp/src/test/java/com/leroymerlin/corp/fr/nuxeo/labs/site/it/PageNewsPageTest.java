package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import static org.junit.Assert.assertFalse;
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
import com.leroymerlin.corp.fr.nuxeo.labs.site.Tools;
import com.leroymerlin.corp.fr.nuxeo.labs.site.features.LabsWebAppFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.LoginPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.PageNewsPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitesRootPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.PageNewsRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = SitesRootPage.class, url = "http://localhost:8089/labssites")
@Jetty(port = 8089)
@RepositoryConfig(init = PageNewsRepositoryInit.class)

public class PageNewsPageTest {

    private static final String DESCRIPTION = "Description";
    private static final String CONTENT = "Content";
    private static final String TITRE_NEWS_ADD = "Titre news 1.";
    @Inject
    SitesRootPage rootPage;

    @Test
    public void pageIsReachable() throws Exception {
        PageNewsPage pageNews = getPageNews();
        assertTrue(pageNews.isLoaded());
    }

    @Test
    public void iCanAddNews() throws Exception {
        PageNewsPage pageNews = getPageNews();
        assertTrue(pageNews.canAddNews());
    }

    @Test
    public void iCanModifyAndDeleteNews() throws Exception {
        PageNewsPage pageNews = getPageNews();
        assertTrue(pageNews.canModifyAndDeleteNews());
    }

    @Test
    public void iCanModifyDescription() throws Exception {
        PageNewsPage pageNews = getPageNews();
        Tools.sleep(3000);
        assertTrue(pageNews.canModifyDescription());
    }

    @Test
    public void deleteNews() throws Exception {
        //delete
        PageNewsPage pageNews = getPageNews();
        Tools.sleep(3000);
        assertTrue(pageNews.containNews(PageNewsRepositoryInit.LABS_NEWS_TITLE));
        pageNews.deleteNews();
        Tools.sleep(3000);
        assertFalse(pageNews.containNews(PageNewsRepositoryInit.LABS_NEWS_TITLE));
    }

    @Test
    public void addDNews() throws Exception {
        //add
        PageNewsPage pageNews = getPageNews();
        pageNews.displayEditWithAdd();
        Tools.sleep(3000);
        pageNews.setTitle(TITRE_NEWS_ADD);
        pageNews.setNewsStartPublication("23/01/2011");
        pageNews.setContent(CONTENT);
        pageNews = pageNews.clickSubmitNews();
        Tools.sleep(3000);
        assertTrue(pageNews.canDisplayMyNews(TITRE_NEWS_ADD, CONTENT));
    }

    @Test
    public void modifyNews() throws Exception {
        //modify
        PageNewsPage pageNews = getPageNews();
        pageNews.displayEditWithModify();
        Tools.sleep(3000);
        pageNews.setClearTitle();
        pageNews.setTitle(TITRE_NEWS_ADD + "ert");
        pageNews.setContent(CONTENT + "ert");
        pageNews = pageNews.clickSubmitNews();
        Tools.sleep(3000);
        assertTrue( pageNews.canDisplayMyNews(TITRE_NEWS_ADD + "ert", CONTENT + "ert"));
    }

    @Test
    public void modifyDescriptionOfPage() throws Exception {
        PageNewsPage pageNews = getPageNews();
        pageNews.displayModifyDescriptionOfPage();
        Tools.sleep(3000);
        pageNews.setDescription(DESCRIPTION);
        pageNews = pageNews.clickSubmitDescriptionOfPage();
        Tools.sleep(3000);
        assertTrue( pageNews.canDisplayMyDescription(DESCRIPTION));
    }

    private PageNewsPage getPageNews() {
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
