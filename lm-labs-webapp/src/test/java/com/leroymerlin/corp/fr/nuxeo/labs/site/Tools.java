/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author fvandaele
 *
 */
public class Tools {
    
    /**
     * Sleep while time milliseconds
     * @param time
     */
    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get the content'element containing the value of the texteArea in checkEditor <br />
     * After yours operations on the content'element you must launch {@link com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtilsTest#returnToDefaultContent returnToDefaultContent}
     * @param pWebPage the current web page
     * @param pIdTextArea the id of textArea
     * @return the content'element containing the value of the texteArea in checkEditor
     */
    public static WebElement getContentElementInCheckEditor(WebPage pWebPage, String pIdTextArea) {
        StringBuilder xpath = new StringBuilder("//td[@id='cke_contents_");
        xpath.append(pIdTextArea);
        xpath.append("']/iframe");
        WebElement element = pWebPage.getDriver().findElement(By.xpath(xpath.toString()));
        WebElement body = null;
        if (element != null) {
            WebDriver frame = pWebPage.getDriver().switchTo().frame(element);
            body = frame.findElement(By.xpath("//body"));
        }
        return body;
    }
    
    /**
     * After switch to a frame, you must return to your web page.
     * @param pWebPage
     */
    public static void returnToDefaultContent(WebPage pWebPage){
        pWebPage.getDriver().switchTo().defaultContent();
    }

}
