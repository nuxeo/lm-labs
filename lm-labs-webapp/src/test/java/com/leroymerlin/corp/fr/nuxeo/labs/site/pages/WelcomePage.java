package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import java.util.List;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.TimeoutException;

public class WelcomePage extends WebPage {

    private static final int WAITING_TIME = 5;

    @Override
    public WebPage ensureLoaded() {
        waitUntilElementFound(By.className("pageBlocs"), WAITING_TIME);
        return super.ensureLoaded();
    }

    public boolean hasSidebar() {
        try {
            this.waitUntilElementFound(By.id("sidebar"), WAITING_TIME);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean hasExternalURL() {
        try {
            this.waitUntilElementFound(By.id("div_externalURL"), WAITING_TIME);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean hasAddExternalURL() {
        try {
            this.waitUntilElementFound(By.id("addExternalURL"), WAITING_TIME);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clickAddExternalURL() {
        try {
            findElement(By.id("addExternalURL"), WAITING_TIME).click();
        } catch (Exception e) {
        }
    }

    public void setFieldsExternalURL(String pName, String pURL, String pOrder) {
        try {
            // Name
            WebElement element = findElement(By.id("extUrlName"), WAITING_TIME);
            element.clear();
            element.sendKeys(pName);
            // URL
            element = findElement(By.id("extURLURL"), WAITING_TIME);
            element.clear();
            element.sendKeys(pURL);
            // Order
            element = findElement(By.id("extURLOrder"), WAITING_TIME);
            element.clear();
            element.sendKeys(pOrder);
        } catch (Exception e) {
        }
    }

    public void clickSubmitExternalURL() {
        try {
            String xpath = "//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only'] [last()]";
            WebElement bt_valid = findElement(By.xpath(xpath));
            if (bt_valid != null) {
                bt_valid.click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickModifyExternalURL() {
        try {
            String xpath = "//div[@class='actionExternalURL']/img";
            WebElement bt_valid = findElement(By.xpath(xpath));
            if (bt_valid != null) {
                bt_valid.click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickDeleteExternalURL() {
        try {
            String xpath = "//div[@class='actionExternalURL']/img[last()]";
            WebElement bt_valid = findElement(By.xpath(xpath));
            if (bt_valid != null) {
                bt_valid.click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasMyExternalURL(String pName, String pURL) {
        try {
            String xpath = "//div[@id='div_externalURL']/ul/li/a";
            WebElement element = findElement(By.xpath(xpath), WAITING_TIME);
            if (element != null && element.getAttribute("href").equals(pURL) && element.getText().equals(pName)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean hasBlocs() {
        List<WebElement> blocs = this.findElements(By.className("bloc"));
        return !blocs.isEmpty();
    }

    // public PageClasseurPage goToPageClasseur(String siteURL, String name) {
    // this.getDriver().navigate().to(siteURL + "/" + name);
    // return this.getPage(PageClasseurPage.class);
    // }

    public LoginPage getLoginPage() {
        return getPage(LoginPage.class);
    }

}
