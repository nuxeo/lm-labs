package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
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
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.PageClasseurPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.repository.PageClasseurRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Browser(type = BrowserFamily.FIREFOX)
@RepositoryConfig(init = PageClasseurRepositoryInit.class)
@HomePage(type = PageClasseurPage.class, url = "http://localhost:8089/labssites/site1/pageclasseur")
@Jetty(port = 8089)
@Ignore
public class PageClasseurTest {

    @Inject PageClasseurPage page;
    
    @Test
    public void pageIsReachable() throws Exception {
        assertNotNull(page);
    }

}
