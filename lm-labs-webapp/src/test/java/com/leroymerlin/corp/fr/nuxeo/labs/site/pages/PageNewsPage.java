package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PageNewsPage extends WebPage {

    private static final int WAITING_TIME = 20;

    @Override
    public WebPage ensureLoaded() {
        waitUntilElementFound(By.id("content"), WAITING_TIME);
        return super.ensureLoaded();
    }
    
    public boolean isLoaded() {
        try {
            WebElement element = findElement(By.className("pageNews"), WAITING_TIME);
            if (element != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    public boolean canAddNews() {
        try {
            WebElement element = findElement(By.id("linkAddNews"), WAITING_TIME);
            if (element != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    public boolean canModifyAndDeleteNews() {
        try {
            WebElement element = findElement(By.className("newsActions"), WAITING_TIME);
            if (element != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
}
