package com.leroymerlin.corp.fr.nuxeo.labs.site.it.pages;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.leroymerlin.corp.fr.nuxeo.labs.site.pages.PageHtmlPage;

public class HtmlContentPage extends WebPage {

    public PageHtmlPage setContent(String content) {
        WebElement txtArea = getDriver().findElement(By.name("content"));
        txtArea.clear();
        txtArea.sendKeys(content);
        getDriver().findElement(
                By.xpath("//form[@id='editcontent']//input[contains(@class,'primary')]"))
                .click();
        return getPage(PageHtmlPage.class);
    }

}
