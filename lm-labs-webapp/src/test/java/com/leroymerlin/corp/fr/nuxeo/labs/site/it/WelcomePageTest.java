package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.Jetty;
import org.nuxeo.runtime.test.runner.web.Browser;
import org.nuxeo.runtime.test.runner.web.BrowserFamily;
import org.nuxeo.runtime.test.runner.web.HomePage;
import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features( { MyJettyFeature.class })
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = WebPage.class, url = "http://localhost:8084/nuxeo/site/labssites/site-ofm/welcome")
@Jetty(port = 8084)
@Deploy( { "com.leroymerlin.corp.fr.nuxeo.portal.user",
        "org.nuxeo.ecm.platform.usermanager" })
public class WelcomePageTest {

    @Inject
    WelcomeHomePage welcomeHomePage;

    @Test
    public void displayWelcomePage() throws Exception {
        welcomeHomePage.home();
        assertNotNull(welcomeHomePage.findElement(By.className("pageBlocs")));
    }
}
