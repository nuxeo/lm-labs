package com.leroymerlin.corp.fr.nuxeo.labs.site.liste;

import org.nuxeo.ecm.core.api.ClientException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;

public interface PageListe extends Page {
    String SCHEMA_NAME = "page_liste";

    String CONTENT = "content";

    String getContent() throws ClientException;
}
