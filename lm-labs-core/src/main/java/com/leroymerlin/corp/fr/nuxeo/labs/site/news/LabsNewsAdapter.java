package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;

public class LabsNewsAdapter extends AbstractPage implements LabsNews {

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
    public Calendar getStartPublication() throws ClientException  {
        return (Calendar) doc.getPropertyValue(START_PUBLICATION);
    }
    @Override
    public String getCreator() throws ClientException  {
        return (String) doc.getPropertyValue(CREATOR);
    }

    @Override
    public String getAccroche() throws ClientException {
        return (String) doc.getPropertyValue(ACCROCHE);
    }

    @Override
    public String getContent() throws ClientException {
        return (String) doc.getPropertyValue(CONTENT);
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
        String lastContributor = (String) doc.getPropertyValue(LAST_CONTRIBUTOR);
        if (StringUtils.isEmpty(lastContributor)){
            lastContributor = getCreator();
        }
        return lastContributor;
    }

    @Override
    public DocumentModel getDocumentModel() {
        return doc;
    }

}
