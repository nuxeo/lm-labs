package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import java.util.List;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class MesSitesPage extends WebPage {
    
    private static final int WAITING_TIME = 10;

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
    
    public boolean canModify() {
        try {
            WebElement element = findElement(By.id("modifyLabsSite"), WAITING_TIME);
            if (element != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    public void clickModify() {
        try {
            WebElement element = findElement(By.id("modifyLabsSite"), WAITING_TIME);
            if (element != null) {
                element.click();
            }
        } catch (Exception e) {
        }
    }
    
    public boolean canDelete() {
        try {
            WebElement element = findElement(By.id("deleteLabsSite"), WAITING_TIME);
            if (element != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    public void clickDelete() {
        try {
            WebElement element = findElement(By.id("deleteLabsSite"), WAITING_TIME);
            if (element != null) {
                ((JavascriptExecutor) driver).executeScript("window.confirm = function(){return true;};");
                element.click();
            }
        } catch (Exception e) {
        }
    }
    
    public void setFields(String pTitle, String pURL, String pDescription) {
        try {
            WebElement element = findElement(By.id("labsSiteTitle"), WAITING_TIME);
            if (element != null) {
                element.clear();
                element.sendKeys(pTitle);
            }
            element = findElement(By.id("labsSiteURL"), WAITING_TIME);
            if (element != null) {
                element.clear();
                element.sendKeys(pURL);
            }
            element = findElement(By.id("labsSiteDescription"), WAITING_TIME);
            if (element != null) {
                element.clear();
                element.sendKeys(pDescription);
            }
        } catch (Exception e) {
        }
    }
    
    public void clickSubmit() {
        try {
            WebElement element = findElement(By.id("btSubmitLabsSite"), WAITING_TIME);
            if (element != null) {
                element.click();
            }
        } catch (Exception e) {
        }
    }
    
    public boolean hasModifiedLasSite(String pTitle, String pURL, String pDescription) {
        try {
            boolean hasTitle = false, hasURL = false;
            WebElement element = findElement(By.className("titleLabsSite"), WAITING_TIME);
            if (element != null) {
                hasTitle = pTitle.equals(element.getText());
            }
            if (element != null) {
                hasURL = ("/labssites/" + pURL).equals(element.getAttribute("href"));
            }
            return hasTitle && hasURL;
        } catch (Exception e) {
            return false;
        }
    }

}
