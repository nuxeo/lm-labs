package com.leroymerlin.corp.fr.nuxeo.labs.site.liste;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;

public class PageListeAdapter extends AbstractPage implements PageListe {
    private final DocumentModel doc;

    public PageListeAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    public String getContent() throws ClientException {
        return (String) doc.getProperty(PageListe.SCHEMA_NAME,
                PageListe.CONTENT);
    }
}
