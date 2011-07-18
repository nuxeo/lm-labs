package com.leroymerlin.corp.fr.nuxeo.labs.site.blocs;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.annotations.RepositoryInit;

public class PageBlocsRepositoryInit implements RepositoryInit {

    public void populate(CoreSession session) throws ClientException {
        DocumentModel doc = session.createDocumentModel("/","page_blocs","PageBlocs");
        doc = session.createDocument(doc);
        session.save();
    }
}
