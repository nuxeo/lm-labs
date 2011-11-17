package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;

public interface PageNotificationService {

    public static final String PROPERTY_TONOTIFY = "mailnotif:tonotify";
    public static final String PROPERTY_NOTIFIED = "mailnotif:notified";

    public boolean markForNotification(DocumentModel doc) throws ClientException;
    
    public void notifyPagesOfSite(DocumentModel site) throws ClientException;
    
    public boolean notifyPage(Page page) throws ClientException;
    
    public boolean notifyPageEvent(Page page, String eventName) throws ClientException;
    
//    public void unmarkForNotification(DocumentModel page, CoreSession session) throws ClientException;
    
//    public List<DocumentModel> getMarkedPagesOfSite(DocumentModel site, CoreSession session) throws ClientException;

//    void fireEvent(Page page, String eventName) throws Exception;
}
