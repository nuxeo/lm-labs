package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import java.io.File;
import java.util.List;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.RenderedWebElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.TimeoutException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Tools;
import com.leroymerlin.corp.fr.nuxeo.labs.site.it.SearchResultsPage;

public class WelcomePage extends WebPage {

    private static final int WAITING_TIME = 5;

    @Override
    public WebPage ensureLoaded() {
        waitUntilElementFound(By.className("content"), WAITING_TIME);
        return super.ensureLoaded();
    }

    public boolean hasSidebar() {
        try {
            this.waitUntilElementFound(By.id("sidebar"), WAITING_TIME);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean hasExternalURL() {
        try {
            this.waitUntilElementFound(By.id("div_externalURL"), WAITING_TIME);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean hasAddExternalURL() {
        try {
            this.waitUntilElementFound(By.id("addExternalURL"), WAITING_TIME);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean hasEditCommentButton() {
        try {
            this.waitUntilElementFound(By.id("editCommentButton"), WAITING_TIME);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void clickAddExternalURL() {
        try {
            findElement(By.id("addExternalURL"), WAITING_TIME).click();
        } catch (Exception e) {
        }
    }

    public void setFieldsExternalURL(String pName, String pURL, String pOrder) {
        try {
            // Name
            WebElement element = findElement(By.id("extUrlName"), WAITING_TIME);
            element.clear();
            element.sendKeys(pName);
            // URL
            element = findElement(By.id("extURLURL"), WAITING_TIME);
            element.clear();
            element.sendKeys(pURL);
            // Order
            element = findElement(By.id("extURLOrder"), WAITING_TIME);
            element.clear();
            element.sendKeys(pOrder);
        } catch (Exception e) {
        }
    }

    public void clickSubmitExternalURL() {
        try {
            String xpath = "//div[@class='ui-dialog ui-widget ui-widget-content ui-corner-all edit-external-url ui-draggable ui-resizable']";
            WebElement div = findElement(By.xpath(xpath), WAITING_TIME);
            if (div != null) {
                xpath = "//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only']";
                List<WebElement> bt_valids = div.findElements(By.xpath(xpath));
                for (WebElement element : bt_valids) {
                    if (element != null
                            && element.getText().contains("valider")) {
                        element.click();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickModifyExternalURL() {
        try {
            String xpath = "//div[@class='actionExternalURL']/img";
            WebElement bt_valid = findElement(By.xpath(xpath));
            if (bt_valid != null) {
                bt_valid.click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickDeleteExternalURL() {
        try {
            String xpath = "//div[@class='actionExternalURL']/img[last()]";
            WebElement bt_valid = findElement(By.xpath(xpath));
            if (bt_valid != null) {
                bt_valid.click();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickEditCommentButton() {
        try {
            WebElement element = findElement(By.id("editCommentButton"),
                    WAITING_TIME);
            if (element != null) {
                element.click();
            }
        } catch (Exception e) {
        }
    }

    public void clickSaveCommentButton() {
        try {
            WebElement element = findElement(By.id("saveCommentButton"),
                    WAITING_TIME);
            if (element != null) {
                element.click();
            }
        } catch (Exception e) {
        }
    }

    public boolean hasMyExternalURL(String pName, String pURL) {
        try {
            String xpath = "//div[@id='div_externalURL']/ul/li/a";
            WebElement element = findElement(By.xpath(xpath), WAITING_TIME);
            if (element != null && element.getAttribute("href").equals(pURL)
                    && element.getText().equals(pName)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean canModifyBanner() {
        try {
            WebElement element = findElement(By.id("bt_modifyBanner"),
                    WAITING_TIME);
            if (element != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean canDeleteBanner() {
        try {
            WebElement element = findElement(By.id("bt_deleteBanner"),
                    WAITING_TIME);
            if (element != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public void clickModifyBanner() {
        try {
            WebElement element = findElement(By.id("bt_modifyBanner"),
                    WAITING_TIME);
            if (element != null) {
                element.click();
            }
        } catch (Exception e) {
        }
    }

    public void setFieldsBanner(File testFile) {
        try {
            WebElement element = findElement(By.id("bannerFileId"),
                    WAITING_TIME);
            if (element != null) {
                element.sendKeys(testFile.getAbsolutePath());
            }
        } catch (Exception e) {
        }
    }

    public void setCommentField(final String value) {
        try {
            WebElement element = Tools.getContentElementInCheckEditor(this,
                    "comment_form_commentField");
            if (element != null) {
                element.sendKeys(value);
            }
            Tools.returnToDefaultContent(this);
        } catch (Exception e) {
            System.out.println("error while finding comment field: "
                    + e.getMessage());
        }
    }

    public boolean commentFieldHasChanged(final String expectedValue) {
        try {
            List<WebElement> elements = findElements(By.id("commentField"));
            if (!elements.isEmpty()) {
                for (WebElement element : elements) {
                    String innerHTML = element.getText();
                    // Contains and not equals because i can't delete the before
                    // text if it's not empty.
                    if (innerHTML != null && innerHTML.contains(expectedValue)) {
                        Tools.returnToDefaultContent(this);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Tools.returnToDefaultContent(this);
            return false;
        }
        Tools.returnToDefaultContent(this);
        return false;
    }

    public void clickSubmitBanner() {
        try {
            String xpath = "//div[@class='ui-dialog ui-widget ui-widget-content ui-corner-all modify-banner ui-draggable ui-resizable']";
            WebElement div = findElement(By.xpath(xpath), WAITING_TIME);
            if (div != null) {
                xpath = "//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only']";
                List<WebElement> bt_valids = div.findElements(By.xpath(xpath));
                for (WebElement element : bt_valids) {
                    if (element != null
                            && element.getText().contains("Modifier")) {
                        element.click();
                        flushPageCache();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasOKBanner(Dimension pDim) {
        try {
            WebElement element = findElement(By.id("bannerImgId"), WAITING_TIME);
            if (element != null) {
                RenderedWebElement rend = (RenderedWebElement) element;
                return pDim.equals(rend.getSize());
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public void clickDeleteBanner() {
        try {
            WebElement element = findElement(By.id("bt_deleteBanner"),
                    WAITING_TIME);
            if (element != null) {
                element.click();
            }
        } catch (Exception e) {
        }
    }

    public SitemapPage sitemapPage() {
        WebElement element = findElement(By.className("siteMapButton"), 10);
        if (element != null) {
            element.click();
            return getPage(SitemapPage.class);
        }
        return null;
    }

    public boolean hasBlocs() {
        List<WebElement> blocs = this.findElements(By.className("bloc"));
        return !blocs.isEmpty();
    }

    public List<WebElement> getLatestUploads() {
        return findElements(By.xpath("//div[contains(@class,'latestuploads')]/ul/li"));
    }

    public boolean hasLatestUploads() {
        return !getLatestUploads().isEmpty();
    }

    // public PageClasseurPage goToPageClasseur(String siteURL, String name) {
    // this.getDriver().navigate().to(siteURL + "/" + name);
    // return this.getPage(PageClasseurPage.class);
    // }

    public LoginPage getLoginPage() {
        return getPage(LoginPage.class);
    }

    public SearchResultsPage search(String searchString) {
        WebElement searchBox = findElement(By.xpath("//div[@id='searchbox']/input[@id='fullText']"));
        searchBox.sendKeys(searchString);
        WebElement button = findElement(By.xpath("//div[@id='searchbox']/img[@id='searchBt']"));
        button.click();
        waitUntilElementFound(By.id("resultsSearch"), 5);
        return getPage(SearchResultsPage.class);
    }

}
