package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PageListPage extends WebPage {

    private static final int WAITING_TIME = 30;

    @Override
    public WebPage ensureLoaded() {
        waitUntilElementFound(By.id("content"), WAITING_TIME);
        return super.ensureLoaded();
    }

    public boolean isLoaded() {
        try {
            WebElement element = findElement(By.className("pageList"), WAITING_TIME);
            if (element != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean canAddEntryLine() {
        try {
            WebElement element = findElement(By.id("addLineEntry"), WAITING_TIME);
            if (element != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean canDisplayManageList() {
        try {
            WebElement element = findElement(By.id("displayManageList"), WAITING_TIME);
            if (element != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
