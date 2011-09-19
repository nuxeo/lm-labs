package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import java.io.File;
import java.util.List;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class PageClasseurPage extends WebPage {

    private static final String XPATH_SECTION_CLASS_FOLDER = "//section[@class='Folder']";
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
        List<WebElement> folders = findElements(By.xpath("//section/div[@class='page-header']/h1/span"));
        for(WebElement elt : folders) {
            if(folderName.equals(elt.getText())) {
                return true;
            }
        }
        return false;
        
    }

    public PageClasseurAddFolderPopup addFolder() {
        WebElement createButton = findElement(By.id("addFolder"));
        createButton.click();
        waitUntilElementFound(By.id("div-addfolder"), WAITING_TIME);
        return getPage(PageClasseurAddFolderPopup.class);
        // TODO Auto-generated method stub
        
    }

    public List<WebElement> getFolderSections() {
        return findElements(By.xpath(XPATH_SECTION_CLASS_FOLDER));
    }
    
    public String getFolderSectionXpath(String folderName) {
        return String.format(XPATH_SECTION_CLASS_FOLDER + "//div[@class='page-header']/h1/span[contains(text(),'%s')]//ancestor::section", folderName);
    }
    
    public PageClasseurPage deleteFolder(String folderName) {
        ((JavascriptExecutor) driver).executeScript("window.confirm = function(){return true;};");
        WebElement deleteBt = findElement(By.xpath(getFolderSectionXpath(folderName) + "//div[contains(@class,'row')]//button[@type='submit' and contains(@class,'danger') ]"));
        deleteBt.click();
        flushPageCache();
        return getPage(PageClasseurPage.class);
    }

    public void addFile(String folderName) {
        WebElement addFileBt = findElement(By.xpath(getFolderSectionXpath(folderName) + "//a[contains(@class,'btn') and contains(@class,'open-dialog') ]"));
        addFileBt.click();
    }

    public void setFile(String folderName, File testFile) {
        WebElement inputFile = findElement(By.xpath(getAddFileModalXpath(folderName) + "//input[@type='file' and @name='file']"));
        inputFile.sendKeys(testFile.getAbsolutePath());
    }

    public PageClasseurPage validate(String folderName) {
        WebElement addFileBt = findElement(By.xpath(getAddFileModalXpath(folderName) + "//ancestor::div//a[contains(@class,'btn') and contains(@class,'primary') ]"));
        addFileBt.click();
        flushPageCache();
        return getPage(PageClasseurPage.class);
    }

    public boolean hasFile(String folderName, String fileName) {
        try {
            findElement(By.xpath(getFileNameColumnXpath(folderName, fileName)));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    private WebElement getFolderSection(String folderName) {
        return findElement(By.xpath(getFolderSectionXpath(folderName)));
    }
    
    private String getAddFileModalXpath(String folderName) {
        WebElement folderSection = getFolderSection(folderName);
        String docId = folderSection.getAttribute("id");
        return String.format("//div[@id='addfile_%s_modal']", docId);
    }

    private String getFileRowsXpath(String folderName) {
        return String.format("%s//table[contains(@class,'classeurFiles')]//tbody/tr", getFolderSectionXpath(folderName));
    }
    
    private String getFileNameColumnXpath(String folderName, String fileName) {
        return String.format("%s//td//span[contains(text(),'%s')]", getFileRowsXpath(folderName), fileName);
    }
    
    public List<WebElement> getFiles(String folderName) {
        return findElements(By.xpath(getFileRowsXpath(folderName)));
    }

    public PageClasseurPage deleteFile(String folderName, String fileName) {
        ((JavascriptExecutor) driver).executeScript("window.confirm = function(){return true;};");
        WebElement deleteBt = findElement(By.xpath(getFileNameColumnXpath(folderName, fileName) + "//ancestor::tr//td//button[contains(@class,'btn') and contains(@class,'danger')]"));
        deleteBt.click();
        flushPageCache();
        return getPage(PageClasseurPage.class);
    }
    
}
