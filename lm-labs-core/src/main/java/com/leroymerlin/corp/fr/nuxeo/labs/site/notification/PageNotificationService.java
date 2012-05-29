package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;

public interface PageNotificationService {

    public static final String PROPERTY_TONOTIFY = "mailnotif:tonotify";
    public static final String PROPERTY_NOTIFIED = "mailnotif:notified";

    public boolean markForNotification(DocumentModel doc, CoreSession session) throws ClientException;
    
    public void notifyPagesOfSite(DocumentModel site) throws ClientException;
    
    public boolean notifyPage(Page page, CoreSession session) throws ClientException;
    
    public boolean notifyPageEvent(Page page, String eventName) throws ClientException;
    
    public void notifyPageNews(PageNews pageNews, List<DocumentModel> newsList) throws Exception;

    boolean canBeMarked(DocumentModel doc, CoreSession session) throws ClientException;

    Page getRelatedPage(DocumentModel doc, CoreSession session) throws ClientException;

//    public void unmarkForNotification(DocumentModel page, CoreSession session) throws ClientException;
    
//    public List<DocumentModel> getMarkedPagesOfSite(DocumentModel site, CoreSession session) throws ClientException;

//    void fireEvent(Page page, String eventName) throws Exception;
    
    public Calendar getLastNotified(DocumentModel doc, CoreSession session) throws ClientException, PropertyException;
}
