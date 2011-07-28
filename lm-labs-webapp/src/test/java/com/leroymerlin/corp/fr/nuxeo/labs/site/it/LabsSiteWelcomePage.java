package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.TimeoutException;

public class LabsSiteWelcomePage extends WebPage {

    private static final int WAITING_TIME = 10;

    public boolean hasMainDiv() {
        try {
            this.waitUntilElementFound(By.className("pageBlocs"), WAITING_TIME);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
    
}
