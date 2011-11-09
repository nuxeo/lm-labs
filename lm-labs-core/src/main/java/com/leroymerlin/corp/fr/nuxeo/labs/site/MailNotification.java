package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;

public interface MailNotification {

    public static final String PROPERTY_ISNOTIFIED = "mailnotif:isnotified";
    public static final String PROPERTY_NOTIFIED = "mailnotif:notified";
    public static final String FACET_NAME = "MailNotification";

    public void setAsNotified() throws ClientException;
    
    public boolean isNotified() throws ClientException;
    
    public Calendar getNotificationDate() throws ClientException;
    
    public void reset() throws ClientException;
    
    public boolean hasFacet();
}
