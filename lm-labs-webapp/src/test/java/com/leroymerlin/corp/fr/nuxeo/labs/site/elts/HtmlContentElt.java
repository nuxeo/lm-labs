package com.leroymerlin.corp.fr.nuxeo.labs.site.elts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HtmlContentElt {

    private final WebElement elt;

    public HtmlContentElt(WebElement elt) {
        this.elt = elt;
    }

    public void edit() {
        WebElement modifyLink = getModifyLink();
        modifyLink.click();
    }

    public String getHtml() {
        return elt.getText().replace(getModifyLink().getText(), "").replace("\n","");
    }

    private WebElement getModifyLink() {
        return elt.findElement(By.xpath("//a[contains(@class,'btn')]"));
    }

}
