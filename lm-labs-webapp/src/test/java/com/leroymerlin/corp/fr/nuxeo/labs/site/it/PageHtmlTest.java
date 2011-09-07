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
import com.leroymerlin.corp.fr.nuxeo.labs.site.Tools;
import com.leroymerlin.corp.fr.nuxeo.labs.site.elts.HtmlContentElt;
import com.leroymerlin.corp.fr.nuxeo.labs.site.elts.HtmlRowElt;
import com.leroymerlin.corp.fr.nuxeo.labs.site.elts.HtmlSectionElt;
import com.leroymerlin.corp.fr.nuxeo.labs.site.features.LabsWebAppFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.it.pages.HtmlContentPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.LoginPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.PageHtmlPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.SitesRootPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.repository.HtmlPageRepositoryInit;

@RunWith(FeaturesRunner.class)
@Features({ LabsWebAppFeature.class })
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

        assertThat(page.getSections()
                .size(), is(0));

        page = page.addSection("Titre de section", "Sous-titre de section");
        assertThat(page.getSections()
                .size(), is(1));

        HtmlSectionElt section = page.getSections()
                .get(0);
        assertThat(section.getTitle(), is("Titre de section"));
        assertThat(section.getDescription(), is("Sous-titre de section"));

        section.remove();
        assertThat(page.getSections()
                .size(), is(0));

    }

    @Test
    public void canAddAndRemoveRowInSection() throws Exception {
        PageHtmlPage page = goToHtmlPage();
        page.edit();
        page = page.addSection("Titre de section", "Sous-titre de section");
        HtmlSectionElt section = page.getSections()
                .get(0);

        assertThat(section.getRows()
                .size(), is(0));
        section.addRow("2COL_2575");

        section = page.getSections()
                .get(0);
        assertThat(section.getRows()
                .size(), is(1));

        HtmlRowElt row = section.getRows()
                .get(0);
        assertThat(row.getContents()
                .size(), is(2));

        row.remove();
        section = page.getSections()
                .get(0);
        assertThat(section.getRows()
                .size(), is(0));
        Tools.sleep(1000);
        section.remove();
    }

    @Test
    public void canEditContent() throws Exception {
        PageHtmlPage page = goToHtmlPage();
        page.edit();
        page = page.addSection("Titre de section", "Sous-titre de section");
        HtmlSectionElt section = page.getSections()
                .get(0);
        section.addRow("2COL_2575");

        section = page.getSections()
                .get(0);
        HtmlRowElt row = section.getRows()
                .get(0);
        
        HtmlContentElt content = row.getContents().get(0);
        HtmlContentPage contentPage = page.editContent(content);
        
        page = contentPage.setContent("Contenu de test");
        
        section = page.getSections().get(0);
        content = section.getRows().get(0).getContents().get(0);
        assertThat(content.getHtml(), is("Contenu de test"));
        Tools.sleep(1000);
        section.remove();

    }

    private PageHtmlPage goToHtmlPage() {
        ensureLoggedIn();
        return rootPage.goToPageHtml("http://localhost:8089/labssites" + "/"
                + HtmlPageRepositoryInit.SITE_URL, "htmlTestPage");
    }

    public void ensureLoggedIn() {
        LoginPage login = rootPage.getLoginPage();
        if (!login.isAuthenticated()) {
            login.ensureLogin("Administrator", "Administrator");
        }
    }

}
