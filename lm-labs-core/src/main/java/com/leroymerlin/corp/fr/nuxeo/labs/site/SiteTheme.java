package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface SiteTheme {

    String getName() throws ClientException;


    Blob getBanner() throws ClientException;
    void setBanner(Blob blob) throws ClientException;


    DocumentModel getDocument();
}
