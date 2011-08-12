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
        List<WebElement> buttons = buttonsDiv.findElements(By.className("ui-button-text"));
        for (WebElement button : buttons) {
            if ("Ajouter".equals(button.getText())) {
                button.click();
                return getPage(PageClasseurPage.class);
            }
        }
        return null;
    }
}
