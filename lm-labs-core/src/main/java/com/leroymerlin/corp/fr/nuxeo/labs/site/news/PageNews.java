package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;

public interface PageNews extends Page {

    LabsNews createNews(String pTitle) throws ClientException;

    List<LabsNews> getAllNews() throws ClientException;
    
    /**
     * Get the lasted news
     * @param pMaxNews number max of news
     * @return the lasted news
     * @throws ClientException
     */
    List<LabsNews> getTopNews(int pMaxNews) throws ClientException;

    Collection<DocumentModel> getTopNewsStartingOn(Calendar today) throws ClientException;

}
