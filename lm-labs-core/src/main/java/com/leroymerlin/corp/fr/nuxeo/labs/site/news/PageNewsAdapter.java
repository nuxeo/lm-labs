package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;

public class PageNewsAdapter extends AbstractPage implements PageNews {

    public PageNewsAdapter(DocumentModel doc) {
        this.doc = doc;
    }

}
