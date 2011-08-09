package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CreateSitePopup extends WebPage {

    public void setTitle(String title) {
        findElement(By.id("labsSiteTitle")).sendKeys(title);
    }

    public void setDescription(String description) {
        findElement(By.id("labsSiteDescription")).sendKeys(description);
    }

    public void setURL(String url) {
        findElement(By.id("labsSiteURL")).sendKeys(url);
    }
    
    public MesSitesPage validate() {
        WebElement bt = findElement(By.id("submit_button"));
        bt.click();
        return getPage(MesSitesPage.class);
    }
}
