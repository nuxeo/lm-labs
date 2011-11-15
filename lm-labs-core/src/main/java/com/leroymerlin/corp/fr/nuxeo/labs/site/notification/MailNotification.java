package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

public interface MailNotification {

    public static final String PROPERTY_TONOTIFY = "mailnotif:tonotify";
    public static final String PROPERTY_NOTIFIED = "mailnotif:notified";
    public static final String FACET_NAME = "MailNotification";

    public void setAsNotified() throws ClientException;

    public void setAsToBeNotified() throws ClientException;
    
    public boolean isToBeNotified() throws ClientException;
    
    public Calendar getNotificationDate() throws ClientException;
    
    public void reset() throws ClientException;
    
    public boolean hasFacet();
    
    public List<DocumentModel> getSubDocumentsToNotify() throws ClientException;
    
    public DocumentEventContext getEventContext() throws ClientException;
    
    public void fireNotificationEvent() throws Exception;
}
