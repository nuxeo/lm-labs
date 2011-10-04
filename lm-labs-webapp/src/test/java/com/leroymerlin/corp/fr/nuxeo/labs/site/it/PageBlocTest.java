package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
import com.leroymerlin.corp.fr.nuxeo.labs.site.features.OpenSocialFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitesRootPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.PageDashBoardRepositoryInit;


@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class, OpenSocialFeature.class })
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = SitesRootPage.class, url = "http://localhost:8089/we/labssites")
@RepositoryConfig(init = PageDashBoardRepositoryInit.class)
@Jetty(port = 8089)

public class PageBlocTest {


    @Inject SitesRootPage rootPage;

    @Test
    public void canReachSite() throws Exception {
        assertThat(rootPage.isloaded(),is(true));
    }

}
