package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import java.util.List;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Header;

public interface PageList extends Page {
    
    DocumentModel getDocument();

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
    
    /**
     * reset the list of header
     * @throws ClientException
     */
    void resetHeaders() throws ClientException;
    
    List<EntriesLine> getLines() throws ClientException;
    
    void saveLine(EntriesLine pLine) throws ClientException;
    
    void removeLine(DocumentRef pRef) throws ClientException;
    
    EntriesLine getLine(DocumentRef pRef) throws ClientException;
    
    void clearLine(DocumentRef pRef) throws ClientException;
    
}
