package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.dto.Header;

public interface PageList extends Page {

    /**
     * Add a header to list of header
     * @param pHead
     * @throws ClientException
     */
    void addHeader(Header pHead) throws ClientException;

    /**
     * Get the set of header
     * @return the set of header
     * @throws ClientException
     */
    Set<Header> getHeaderSet() throws ClientException;
    
}
