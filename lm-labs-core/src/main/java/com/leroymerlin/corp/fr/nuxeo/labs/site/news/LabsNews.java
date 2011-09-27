package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlSection;


public interface LabsNews  {

    String getTitle() throws ClientException;

    void setTitle(String pTitle) throws ClientException;

    Calendar getStartPublication() throws ClientException;

    void setStartPublication(Calendar pStartPublication) throws ClientException;

    Calendar getEndPublication() throws ClientException;

    void setEndPublication(Calendar pEndPublication) throws ClientException;

    String getCreator() throws ClientException;

    String getLastContributor() throws ClientException;

    String getAccroche() throws ClientException;

    void setAccroche(String pAccroche) throws ClientException;

    String getContent() throws ClientException;

    void setContent(String pContent) throws ClientException;

    String getNewsTemplate() throws ClientException;

    void setNewsTemplate(String pNewsTemplate) throws ClientException;

    DocumentModel getDocumentModel();

    String getLastContributorFullName() throws Exception;
}
