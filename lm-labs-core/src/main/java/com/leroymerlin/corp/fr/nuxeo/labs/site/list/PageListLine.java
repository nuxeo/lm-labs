package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import org.nuxeo.ecm.core.api.ClientException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;


public interface PageListLine {

    /**
     * Set a line with a EntriesLine
     * @param pLine
     * @throws ClientException
     */
    void setLine(EntriesLine pLine) throws ClientException;
    
    /**
     * Get a Entriesline
     * @return a Entriesline
     * @throws ClientException
     */
    EntriesLine getLine() throws ClientException;
    
    /**
     * Remove the current line
     * @throws ClientException
     */
    void removeLine() throws ClientException;
}
