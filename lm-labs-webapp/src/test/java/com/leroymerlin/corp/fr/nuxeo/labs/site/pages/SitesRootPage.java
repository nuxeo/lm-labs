package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;

public class SitesRootPage extends WebPage {

    @Override
    public WebPage ensureLoaded() {
        waitUntilElementFound(By.className("info"), 5);
        return super.ensureLoaded();
    }

}
