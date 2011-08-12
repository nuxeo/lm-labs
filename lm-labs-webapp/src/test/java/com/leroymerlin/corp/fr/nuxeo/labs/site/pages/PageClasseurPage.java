package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import java.util.List;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PageClasseurPage extends WebPage {

    private static final int WAITING_TIME = 60;

    @Override
    public WebPage ensureLoaded() {
        waitUntilElementFound(By.className("classeur"), WAITING_TIME);
        return super.ensureLoaded();
    }
    
    public boolean isLoaded() {
        try {
            WebElement element = findElement(By.className("classeur"), WAITING_TIME);
            if (element != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public LoginPage getLoginPage() {
        return getPage(LoginPage.class);
    }

    public boolean hasFolder(String folderName) {
        List<WebElement> folders = findElements(By.cssSelector(".row.Folder"));
        for (WebElement folder : folders) {
            if (folderName.equals(folder.findElement(By.className("colFolderTitle")).getText())) {
                return true;
            }
        }
        return false;
    }

    public PageClasseurAddFolderPopup clickAddFolder() {
        WebElement createButton = findElement(By.id("addFolder"));
        createButton.click();
        waitUntilElementFound(By.id("div-addfolder"), WAITING_TIME);
        return getPage(PageClasseurAddFolderPopup.class);
        // TODO Auto-generated method stub
        
    }

}
