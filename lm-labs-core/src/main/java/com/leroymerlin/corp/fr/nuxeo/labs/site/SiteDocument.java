package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.util.Collection;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;

/**
 * This interface can be adapted to every document in a site
 * tree. It has various methods to get information about it
 * @author dmetzler
 *
 */
public interface SiteDocument extends LabsAdapter {

    /**
     * Return the parent page of this document
     * @return
     * @throws ClientException
     */
    Page getParentPage() throws ClientException;

    /**
     * Return the parent site for this document
     * @return
     * @throws ClientException
     * @throws IllegalArgumentException If document not located in a site
     */
    LabsSite getSite() throws ClientException, IllegalArgumentException;


    String getParentPagePath() throws ClientException;

    String getResourcePath() throws ClientException;

    BlobHolder getBlobHolder();

    /**
     * TODO unit test.
     * @return
     * @throws ClientException
     */
    Collection<Page> getChildrenPages() throws ClientException;

    /**
     * TODO unit test.
     * @return
     * @throws ClientException
     */
    DocumentModelList getChildrenPageDocuments() throws ClientException;
    
    /**
     * TODO unit test.
     * @param userName
     * @return
     * @throws ClientException
     */
    public Collection<Page> getChildrenNavigablePages(final String userName) throws ClientException;

}
