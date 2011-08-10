package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SitesRootPage extends WebPage {

    private static final int WAITING_TIME = 5;

    @Override
    public WebPage ensureLoaded() {
        waitUntilElementFound(By.id("homePage"), 5);
        return super.ensureLoaded();
    }
    
    public boolean isloaded() {
        waitUntilElementFound(By.id("homePage"), WAITING_TIME);
        return true;
    }

    public boolean canClickAddSite() {
        WebElement bt;
        try {
            bt = this.findElement(By.id("bt_create_labssite"));
        } catch (Exception e) {
            return false;
        }
        return (bt != null);
    }

    public LoginPage getLoginPage() {
        return getPage(LoginPage.class);
    }
    
    public MesSitesPage getMesSitesPage() {
        WebElement bt = findElement(By.id("bt_display_labssite"));
        bt.click();
        waitUntilElementFound(By.id("MySites"), WAITING_TIME);
        return getPage(MesSitesPage.class);
    }

    public CreateSitePopup createSite() {
        WebElement createButton = findElement(By.id("bt_create_labssite"));
        createButton.click();
        waitUntilElementFound(By.className("create-labs-site"), WAITING_TIME);
        return getPage(CreateSitePopup.class);
   }

}
