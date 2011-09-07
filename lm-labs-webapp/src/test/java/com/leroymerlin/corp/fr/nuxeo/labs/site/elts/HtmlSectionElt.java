package com.leroymerlin.corp.fr.nuxeo.labs.site.elts;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

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

    public List<HtmlRowElt> getRows() {
        List<HtmlRowElt> result = new ArrayList<HtmlRowElt>();
        List<WebElement> rowElements = sectionElt.findElements(By.className("row"));
        for(WebElement elt : rowElements) {
            result.add(new HtmlRowElt(elt,driver));
        }
        return result;
    }

    public void addRow(String template) {
        WebElement selectTemplateInput = sectionElt.findElement(By.xpath("//form[contains(@id,'addrow_')]//select"));
        Select select = new Select(selectTemplateInput);
        select.selectByValue(template);
        WebElement addRowBtn = sectionElt.findElement(By.xpath("//form[contains(@id,'addrow_')]//button[contains(@class,'primary')]"));
        addRowBtn.click();
        
    }

}
