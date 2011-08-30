package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import java.util.List;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtilsTest;

public class PageNewsPage extends WebPage {

    private static final int WAITING_TIME = 30;

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

    public boolean containNews(String title) {
        try {
            List<WebElement> elements = findElements(By.className("titleNews"));
            if (!elements.isEmpty()) {
                for (WebElement element : elements) {
                    if (element != null && title.equals(element.getText())) {
                        return true;
                    }
                }
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

    public void displayEditWithAdd() {
        findElement(By.id("linkAddNews"), WAITING_TIME).click();
    }

    public void displayEditWithModify() {
        findElement(By.className("modifyActionNews"), WAITING_TIME).click();
    }

    public void deleteNews() {
        findElement(By.className("deleteActionNews"), WAITING_TIME).click();
    }

    public PageNewsPage clickSubmitNews() {
        WebElement button = findElement(By.id("FKNews"), WAITING_TIME);
        button.click();
        return getPage(PageNewsPage.class);
    }

    public void setTitle(String pTitle) {
        WebElement findElement = findElement(By.id("newsTitle"), WAITING_TIME);
        findElement.sendKeys(pTitle);
    }

    public void setClearTitle() {
        WebElement findElement = findElement(By.id("newsTitle"), WAITING_TIME);
        findElement.clear();
    }

    public void setNewsStartPublication(String pNewsStartPublication) {
        findElement(By.id("newsStartPublication")).sendKeys(pNewsStartPublication);
    }

    public void setContent(String pContent) {
        WebElement body = LabsSiteWebAppUtilsTest.getContentElementInCheckEditor(this, "newsContent");
        if (body != null) {
            body.sendKeys(pContent);
        }
        LabsSiteWebAppUtilsTest.returnToDefaultContent(this);
    }

    public boolean canDisplayMyNews(String pTitle, String pContent) {
        try {
            boolean findTitle = false;
            boolean findContent = false;
            List<WebElement> elements = findElements(By.className("titleNews"));
            if (!elements.isEmpty()) {
                for (WebElement element : elements) {
                    if (pTitle.equals(element.getText())) {
                        findTitle = true;
                    }
                }
            }
            elements = findElements(By.className("contentNews"));
            if (!elements.isEmpty()) {
                for (WebElement element : elements) {
                    String innerHTML = element.getText();
                    // Contains and not equals because i can't delete the before
                    // text if it's not empty.
                    if (innerHTML != null && innerHTML.contains(pContent)) {
                        findContent = true;
                    }
                }
            }
            if (findTitle && findContent) {
                return true;
            }
        } catch (Exception e) {
            LabsSiteWebAppUtilsTest.returnToDefaultContent(this);
            return false;
        }
        return false;
    }

}
