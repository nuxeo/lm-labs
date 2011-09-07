package com.leroymerlin.corp.fr.nuxeo.labs.site.pages;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.leroymerlin.corp.fr.nuxeo.labs.site.elts.HtmlContentElt;
import com.leroymerlin.corp.fr.nuxeo.labs.site.elts.HtmlSectionElt;
import com.leroymerlin.corp.fr.nuxeo.labs.site.it.pages.HtmlContentPage;

public class PageHtmlPage extends WebPage {

    @FindBy(xpath = "//form[@id='addsection']//input[@name='title']")
    WebElement addSectionTitleInput;

    @FindBy(xpath = "//form[@id='addsection']//input[@name='description']")
    WebElement addSectionDescriptionInput;

    @FindBy(xpath = "//form[@id='addsection']//div[@class='actions']//button[contains(@class,'primary')]")
    WebElement addSectionAddBtn;

    public String getTitle() {
        return getDriver().getTitle();
    }

    public List<HtmlSectionElt> getSections() {
        List<WebElement> sections = getDriver().findElements(By.xpath("//section"));
        List<HtmlSectionElt> result = new ArrayList<HtmlSectionElt>();
        
        if(sections == null) {
            return result;
        }
        
        for (WebElement elt : sections) {
            result.add(new HtmlSectionElt(elt,getDriver()));
        }
        return result;
    }

    public PageHtmlPage addSection(String title, String subtitle) {
        addSectionTitleInput.clear();
        addSectionTitleInput.sendKeys(title);
        addSectionDescriptionInput.clear();
        addSectionDescriptionInput.sendKeys(subtitle);
        addSectionAddBtn.click();
        flushPageCache();
        return getPage(PageHtmlPage.class);
        
    }

    public void edit() {
        getDriver().findElement(By.xpath("//a[@class='dropdown-toggle']")).click();
        WebElement editMenuItem = this.waitUntilElementFound(By.id("page_edit"), 3);
        editMenuItem.click();
        
    }

    public HtmlContentPage editContent(HtmlContentElt content) {
        content.edit();
        return getPage(HtmlContentPage.class);
    }

}
