package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import java.util.List;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class MesSitesPage extends WebPage {

    public SitesRootPage backToHomePage() {
        return getPage(SitesRootPage.class);
    }

    public boolean containsSite(String title) {
        List<WebElement> elements = findElements(By.className("titleLabsSite"));
        for (WebElement element : elements) {
            if (title.equals(element.getText())) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.nuxeo.runtime.test.runner.web.WebPage#ensureLoaded()
     */
    @Override
    public WebPage ensureLoaded() {
        this.waitUntilElementFound(By.id("MySites"), 10);
        return this;
    }

    public WelcomePage welcomePage(String title) {
        List<WebElement> elements = findElements(By.className("titleLabsSite"));
        for (WebElement element : elements) {
            if (title.equals(element.getText())) {
                element.click();
                return getPage(WelcomePage.class);
            }
        }
        return null;
    }

}
