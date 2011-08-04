package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;

public class PageClasseurPage extends WebPage {

    private static final int WAITING_TIME = 120;

    @Override
    public WebPage ensureLoaded() {
        WebPage page = super.ensureLoaded();
        waitUntilElementFound(By.className("classeur"), WAITING_TIME);
        return page;
    }

}
