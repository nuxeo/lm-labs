package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.sun.syndication.feed.synd.SyndFeed;

public interface PageNews extends Page {

    LabsNews createNews(String pTitle, CoreSession session) throws ClientException;

    List<LabsNews> getAllNews(CoreSession session) throws ClientException;
    
    /**
     * Get the lasted news
     * @param pMaxNews number max of news
     * @return the lasted news
     * @throws ClientException
     */
    List<LabsNews> getTopNews(int pMaxNews, CoreSession session) throws ClientException;

    Collection<DocumentModel> getTopNewsStartingOn(Calendar today, CoreSession session) throws ClientException;
    
    /**
     * @see Rome version 1.0
     * @param pLabsNews
     * @param pPathBase
     * @param pDefaultDescription
     * @return
     * @throws ClientException
     */
    SyndFeed buildRssLabsNews(List<LabsNews> pLabsNews, String pPathBase, String pDefaultDescription, CoreSession session) throws ClientException;
}
