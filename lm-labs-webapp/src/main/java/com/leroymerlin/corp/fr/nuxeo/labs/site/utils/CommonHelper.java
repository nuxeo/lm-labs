package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;

public final class CommonHelper {

    public CommonHelper() {}

    public static final SiteDocument siteDoc(DocumentModel doc) {
        return doc.getAdapter(SiteDocument.class);
    }

    public static final Page sitePage(DocumentModel doc) throws ClientException {
        return siteDoc(doc).getPage();
    }
}
