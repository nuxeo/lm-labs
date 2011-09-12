package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import com.leroymerlin.corp.fr.nuxeo.labs.site.repository.AllDocTypeRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features({ LabsWebAppFeature.class })
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = SitesRootPage.class, url = "http://localhost:8089/labssites")
@RepositoryConfig(init = AllDocTypeRepositoryInit.class)
@Jetty(port = 8089)
@Deploy({ "com.leroymerlin.corp.fr.nuxeo.portal.user",
        "org.nuxeo.ecm.platform.usermanager" })
public class RichtextTest {
    public static final String COMMENT_VALUE = "LABS IS COOL!";

    @Inject
    SitesRootPage rootPage;

    @Test
    public void welcomingPageIsCommentable() throws Exception {
        ensureLoggedIn();
        MesSitesPage mesSitesPage = rootPage.getMesSitesPage();
        assertTrue(mesSitesPage.containsSite("OFM"));
        WelcomePage welcomePage = mesSitesPage.welcomePage("OFM");
        assertNotNull(welcomePage);
        assertTrue(welcomePage.hasEditCommentButton());
        welcomePage.clickEditCommentButton();
        welcomePage.setCommentField(COMMENT_VALUE);
        welcomePage.clickSaveCommentButton();
        assertTrue(welcomePage.commentFieldHasChanged(COMMENT_VALUE));
    }

    public void ensureLoggedIn() {
        LoginPage login = rootPage.getLoginPage();
        if (!login.isAuthenticated()) {
            login.ensureLogin("Administrator", "Administrator");
        }
    }
}
