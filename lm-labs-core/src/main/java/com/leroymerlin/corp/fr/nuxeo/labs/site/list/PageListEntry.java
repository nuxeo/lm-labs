package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.list.dto.UrlType;


public interface PageListEntry {

    void setIdHeader(int idHeader) throws ClientException;

    int getIdHeader() throws ClientException;

    void setText(String text) throws ClientException;

    Object getText() throws ClientException;

    void setDate(Calendar cal) throws ClientException;

    Calendar getDate() throws ClientException;

    void setCheckbox(boolean checkbox) throws ClientException;

    boolean getCheckBox() throws ClientException;

    void setDataURL(UrlType url) throws ClientException;

    Object getDataURL() throws ClientException;
    
}
