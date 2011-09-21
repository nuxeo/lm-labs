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
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.PageListPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitesRootPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.OfmRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.PageListRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = SitesRootPage.class, url = "http://localhost:8089/labssites")
@Jetty(port = 8089)
@RepositoryConfig(init = PageListRepositoryInit.class)

public class PageListPageTest {

    @Inject
    SitesRootPage rootPage;

    @Test
    public void pageIsReachable() throws Exception {
        PageListPage pageList = getPageList();
        assertTrue(pageList.isLoaded());
    }

    @Test
    public void iCanAddLineEntry() throws Exception {
        PageListPage pageList = getPageList();
        assertTrue(pageList.canAddEntryLine());
    }

    @Test
    public void iCanManageList() throws Exception {
        PageListPage pageList = getPageList();
        assertTrue(pageList.canDisplayManageList());
    }


    private PageListPage getPageList() {
        ensureLoggedIn();
        PageListPage pageList = rootPage.goToPageList("http://localhost:8089/labssites" + "/" + OfmRepositoryInit.SITE_URL, PageListRepositoryInit.PAGE_LIST_TITLE);
        return pageList;
    }

    public void ensureLoggedIn() {
        LoginPage login = rootPage.getLoginPage();
        if(!login.isAuthenticated()) {
            login.ensureLogin("Administrator", "Administrator");
        }
    }

}
