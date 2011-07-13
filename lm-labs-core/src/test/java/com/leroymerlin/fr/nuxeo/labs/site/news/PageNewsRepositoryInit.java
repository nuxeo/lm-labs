package com.leroymerlin.fr.nuxeo.labs.site.news;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.annotations.RepositoryInit;

public class PageNewsRepositoryInit implements RepositoryInit {

    public void populate(CoreSession session) throws ClientException {
        DocumentModel doc = session.createDocumentModel("/","page_news","PageNews");
        doc = session.createDocument(doc);
        session.save();
    }
}
