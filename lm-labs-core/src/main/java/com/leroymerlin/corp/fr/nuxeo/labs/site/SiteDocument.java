package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.ClientException;

/**
 * This interface can be adapted to every document in a site
 * tree. It has various methods to get information about it
 * @author dmetzler
 *
 */
public interface SiteDocument {

    /**
     * Return the parent page of this document
     * @return
     * @throws ClientException
     */
    Page getPage() throws ClientException;
}
