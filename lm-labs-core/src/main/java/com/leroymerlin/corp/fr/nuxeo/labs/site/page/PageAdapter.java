package com.leroymerlin.corp.fr.nuxeo.labs.site.page;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;


public class PageAdapter extends AbstractPage implements Page {

    public PageAdapter(DocumentModel doc) {
        super(doc);
    }

}
