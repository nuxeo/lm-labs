package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import java.util.List;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PageClasseurAddFolderPopup extends WebPage {

    public void setTitle(String title) {
        findElement(By.id("folderName")).sendKeys(title);
    }

    public PageClasseurPage validate() {
        WebElement buttonsDiv = findElement(By.className("ui-dialog-buttonset"));
        List<WebElement> buttons = buttonsDiv.findElements(By.className("ui-button"));
//        buttons.get(1).click();
//        return getPage(PageClasseurPage.class);
        
        
        for (WebElement button : buttons) {
            try {
                WebElement span = button.findElement(By.xpath("//span[text()='Ajouter']"));
                button.click();
                return getPage(PageClasseurPage.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            WebElement span = button.findElement(By.className("ui-button-text"));
//            if ("Ajouter".equals(span.getText())) {
//                button.click();
//                return getPage(PageClasseurPage.class);
//            }
        }
        return null;
    }
}
