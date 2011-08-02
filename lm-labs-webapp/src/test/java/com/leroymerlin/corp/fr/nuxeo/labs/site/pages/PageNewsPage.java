package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;

public class PageNewsPage extends WebPage {

    private static final int WAITING_TIME = 120;

    @Override
    public WebPage ensureLoaded() {
        waitUntilElementFound(By.id("content"), WAITING_TIME);
        return super.ensureLoaded();
    }

//    public boolean hasHeader() {
//        try {
//            this.waitUntilElementFound(By.id("header"), WAITING_TIME);
//            return true;
//        } catch (TimeoutException e) {
//            return false;
//        }
//    }

//    public LoginPage getLoginPage() {
//        return getPage(LoginPage.class);
//    }
    
}
