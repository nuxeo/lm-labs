package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import org.nuxeo.ecm.core.api.ClientException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;


public interface PageListLine {

    void setLine(EntriesLine pLine) throws ClientException;
    
    EntriesLine getLine() throws ClientException;
}
