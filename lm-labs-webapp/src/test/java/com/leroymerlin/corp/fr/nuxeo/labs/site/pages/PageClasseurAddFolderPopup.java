package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PageClasseurAddFolderPopup extends WebPage {

    public void setTitle(String title) {
        findElement(By.name("folderName")).sendKeys(title);
    }

    public PageClasseurPage validate() {
        WebElement button = findElement(By.xpath("//div[@id='div-addfolder']//parent::div//div[@class='modal-footer']/a[ contains(@class,'primary') ]"));
        button.click();
        return getPage(PageClasseurPage.class);
    }
}
