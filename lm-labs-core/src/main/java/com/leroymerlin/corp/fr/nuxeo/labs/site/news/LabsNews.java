package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import java.io.IOException;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.common.core.adapter.SessionAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.base.LabsCommentable;
import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.LabsBlobHolderException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlSection;


public interface LabsNews  extends HtmlSection, SessionAdapter, LabsCommentable {

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

    LabsNewsBlobHolder getBlobHolder() throws ClientException;

    void setOriginalPicture(Blob blob) throws ClientException;

    void setSummaryPicture(Blob fileBlob) throws ClientException, IOException;
    
    boolean hasSummaryPicture();

    Blob getSummaryPicture() throws ClientException, IOException;
    
    void deleteSummaryPicture() throws ClientException;

    String getCropCoords() throws ClientException;

    void setCropCoords(String cropCoords) throws ClientException;

    void checkPicture(Blob blob) throws LabsBlobHolderException, ClientException;
    
    boolean isTop() throws ClientException;
    
    void setTop(boolean isTop) throws ClientException;
}
