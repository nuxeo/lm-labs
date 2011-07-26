package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

public class LabsNewsAdapter implements LabsNews {

    private final DocumentModel doc;

    static final String TITLE = "dublincore:title";
    static final String CREATOR = "dublincore:creator";
    static final String LAST_CONTRIBUTOR = "dublincore:lastContributor";
    
    static final String START_PUBLICATION = "ln:startPublication";
    static final String END_PUBLICATION = "ln:endPublication";
    static final String ACCROCHE = "ln:accroche";
    static final String CONTENT = "ln:content";
    static final String NEWS_TEMPLATE = "ln:template";
    

    public LabsNewsAdapter(DocumentModel doc) {
        this.doc = doc;
    }
    
    @Override
    public String getTitle() throws ClientException {
        return (String) doc.getPropertyValue(TITLE);
    }
    @Override
    public Calendar getStartPublication() throws ClientException  {
        return (Calendar) doc.getPropertyValue(START_PUBLICATION);
    }
    @Override
    public String getCreator() throws ClientException  {
        return (String) doc.getPropertyValue(CREATOR);
    }

    @Override
    public String getAccroche() throws ClientException {
        String acc = (String) doc.getPropertyValue(ACCROCHE);
        acc = acc.replaceAll("\n", "<br/>");
        return acc;
    }

    @Override
    public String getContent() throws ClientException {
        return (String) doc.getPropertyValue(CONTENT);
    }

    @Override
    public void setTitle(String title) throws ClientException {
        doc.setPropertyValue(TITLE, title);        
    }

    @Override
    public void setAccroche(String pAccroche) throws ClientException {
        doc.setPropertyValue(ACCROCHE, pAccroche); 
    }

    @Override
    public void setContent(String pContent) throws ClientException {
        doc.setPropertyValue(CONTENT, pContent); 
    }

    @Override
    public String getNewsTemplate() throws ClientException {
        return (String) doc.getPropertyValue(NEWS_TEMPLATE);
    }

    @Override
    public void setNewsTemplate(String pNewsTemplate) throws ClientException {
        doc.setPropertyValue(NEWS_TEMPLATE, pNewsTemplate);
    }

    @Override
    public void setStartPublication(Calendar pStartPublication)
            throws ClientException {
        doc.setPropertyValue(START_PUBLICATION, pStartPublication);
        
    }

    @Override
    public Calendar getEndPublication() throws ClientException {
        return (Calendar) doc.getPropertyValue(END_PUBLICATION);
    }

    @Override
    public void setEndPublication(Calendar pEndPublication)
            throws ClientException {
        doc.setPropertyValue(END_PUBLICATION, pEndPublication);
        
    }

    @Override
    public String getLastContributor() throws ClientException {
        // TODO Auto-generated method stub
        return (String) doc.getPropertyValue(LAST_CONTRIBUTOR);
    }

}
