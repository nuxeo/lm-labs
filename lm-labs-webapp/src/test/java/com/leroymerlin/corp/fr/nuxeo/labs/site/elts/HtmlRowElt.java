package com.leroymerlin.corp.fr.nuxeo.labs.site.elts;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HtmlRowElt {

    private final WebElement rowElement;
    private final WebDriver driver;

    public HtmlRowElt(WebElement elt, WebDriver driver) {
        this.rowElement = elt;
        this.driver = driver;
    }

    public List<HtmlContentElt> getContents() {
        List<WebElement> contentElts = rowElement.findElements(By.className("columns"));
        List<HtmlContentElt> result = new ArrayList<HtmlContentElt>();
        for(WebElement elt: contentElts) {
            result.add(new HtmlContentElt(elt));
        }
        return result;
    }

    public void remove() {
        WebElement deleteBtn = rowElement.findElement(By.xpath("//form[contains(@id,'rowdelete_')]//button[contains(@class,'danger')]"));
        deleteBtn.click();
        Alert alert = driver.switchTo()
                .alert();
        alert.accept();
        
    }

}
