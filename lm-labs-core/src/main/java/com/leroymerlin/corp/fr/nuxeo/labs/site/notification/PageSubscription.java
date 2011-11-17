package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.util.List;

public interface PageSubscription {

    List<String> getNotificationNames();

    /**
     * @param principal user ID
     * @throws Exception
     */
    void subscribe(String principal) throws Exception;
    
    /**
     * @param principal user ID
     * @throws Exception
     */
    void unsubscribe(String principal) throws Exception;
    
    /**
     * @param principal user ID
     * @throws Exception
     */
    boolean isSubscribed(String principal) throws Exception;
}
