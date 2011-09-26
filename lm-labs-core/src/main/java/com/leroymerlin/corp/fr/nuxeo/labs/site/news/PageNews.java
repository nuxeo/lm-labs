package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;

public interface PageNews extends Page {

    LabsNews createNews(String pTitle) throws ClientException;

    List<LabsNews> getAllNews() throws ClientException;

}
