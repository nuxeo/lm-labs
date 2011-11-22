package com.leroymerlin.corp.fr.nuxeo.labs.site.publisher;

import org.nuxeo.ecm.core.api.ClientException;

/**
 * This interface can be adapted to every document in a siteRoot.
 * It has various methods to change life cycle about it
 * @author fvandaele
 *
 */
public interface LabsPublisher {

    void publish() throws ClientException;
    
    void draft() throws ClientException;
    
    boolean isVisible() throws ClientException;
    
    void delete() throws ClientException;
    
    void undelete() throws ClientException;
    
    boolean isDeleted() throws ClientException;
    
    boolean isDraft() throws ClientException;
}
