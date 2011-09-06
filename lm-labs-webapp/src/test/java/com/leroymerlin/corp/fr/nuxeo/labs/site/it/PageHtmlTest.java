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
import com.leroymerlin.corp.fr.nuxeo.labs.site.elts.HtmlSectionElt;
import com.leroymerlin.corp.fr.nuxeo.labs.site.features.LabsWebAppFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.LoginPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.PageHtmlPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitesRootPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.repository.HtmlPageRepositoryInit;
@RunWith(FeaturesRunner.class)
@Features( { LabsWebAppFeature.class })
@Browser(type = BrowserFamily.FIREFOX)
@HomePage(type = SitesRootPage.class, url = "http://localhost:8089/labssites")
@Jetty(port = 8089)
@RepositoryConfig(init = HtmlPageRepositoryInit.class)
public class PageHtmlTest {

	
    @Inject
    SitesRootPage rootPage;

    @Test
    public void pageIsReachable() throws Exception {    	
    	PageHtmlPage page = goToHtmlPage();
    	assertThat(page.getTitle(), is("HTML Test page"));
    }
    
    
    @Test
	public void canAddAndRemoveSection() throws Exception {
    	PageHtmlPage page = goToHtmlPage();
    	
    	page.edit();
    	
    	assertThat(page.getSections().size(), is(0));
    	
    	page = page.addSection("Titre de section", "Sous-titre de section");
    	assertThat(page.getSections().size(), is(1));
    	
    	HtmlSectionElt section = page.getSections().get(0);
    	assertThat(section.getTitle(), is("Titre de section"));
    	assertThat(section.getDescription(), is("Sous-titre de section"));
    	
    	
//    	section.remove();
//    	assertThat(page.getSections().size(), is(0));
    	
	}

    
    private PageHtmlPage goToHtmlPage() {
    	ensureLoggedIn();
		return rootPage.goToPageHtml("http://localhost:8089/labssites" + "/" + HtmlPageRepositoryInit.SITE_URL, "htmlTestPage");
	}


	public void ensureLoggedIn() {
        LoginPage login = rootPage.getLoginPage();
        if(!login.isAuthenticated()) {
            login.ensureLogin("Administrator", "Administrator");
        }
    }

}
