package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import java.io.Serializable;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;

public class SiteThemeAdapter implements SiteTheme {

    private final DocumentModel doc;

    public SiteThemeAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    public DocumentModel getDocument() {
        return doc;
    }

    @Override
    public String getName() throws ClientException {
        return doc.getName();
    }

    @Override
    public Blob getBanner() throws ClientException {
        return (Blob) doc.getPropertyValue(Schemas.SITETHEME.prefix() + ":banner");
    }

    @Override
    public void setBanner(Blob blob) throws ClientException {
        doc.setPropertyValue(Schemas.SITETHEME.prefix() + ":banner", (Serializable) blob);
    }

}
