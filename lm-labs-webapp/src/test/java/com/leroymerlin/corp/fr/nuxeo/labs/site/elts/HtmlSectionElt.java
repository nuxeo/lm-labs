package com.leroymerlin.corp.fr.nuxeo.labs.site.elts;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HtmlSectionElt {

    private final WebElement sectionElt;
    private final WebDriver driver;

    public HtmlSectionElt(WebElement sectionElt, WebDriver driver) {
        this.sectionElt = sectionElt;
        this.driver = driver;
    }

    public String getTitle() {
        WebElement titleElt = sectionElt.findElement(By.xpath("//div[@class='page-header']/h1"));
        String fullTitle = titleElt.getText();
        return StringUtils.strip(fullTitle.replaceAll(getDescription(), ""));
    }

    public String getDescription() {
        WebElement descElt = sectionElt.findElement(By.xpath("//div[@class='page-header']/h1/small"));
        return descElt.getText();
    }

    public void remove() {
        WebElement deleteBtn = sectionElt.findElement(By.xpath("//form[contains(@id,'editsection_')]//button[contains(@class,'danger')]"));
        deleteBtn.click();
        Alert alert = driver.switchTo()
                .alert();
        alert.accept();

    }

}
