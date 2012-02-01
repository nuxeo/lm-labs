package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.model.PropertyException;


public interface MailNotification {

    public void setAsToBeNotified() throws Exception;
    
    /**
     * @return
     * @throws ClientException
     * @throws PropertyException
     * @throws Exception
     */
    public Calendar getLastNotified() throws Exception;
    
}
