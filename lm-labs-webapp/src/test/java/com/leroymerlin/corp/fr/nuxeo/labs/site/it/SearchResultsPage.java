package com.leroymerlin.corp.fr.nuxeo.labs.site.it;

import java.util.List;

import org.nuxeo.runtime.test.runner.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SearchResultsPage extends WebPage {

    public int getNbrResults() {
        List<WebElement> searchResults = getResults();
        return searchResults.size();
    }

    private List<WebElement> getResults() {
        return findElements(By.xpath("//ul[@id='resultsSearch']/li"));
    }

}
