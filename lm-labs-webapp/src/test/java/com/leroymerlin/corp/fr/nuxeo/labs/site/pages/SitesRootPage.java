package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;

public class SitesRootPage extends WebPage {

    private static final int WAITING_TIME = 5;

//        @Override
//    public WebPage ensureLoaded() {
//        waitUntilElementFound(By.id("homePage"), 5);
//        return super.ensureLoaded();
//    }
    
    public boolean isloaded() {
        waitUntilElementFound(By.id("homePage"), WAITING_TIME);
        return true;
    }

}
