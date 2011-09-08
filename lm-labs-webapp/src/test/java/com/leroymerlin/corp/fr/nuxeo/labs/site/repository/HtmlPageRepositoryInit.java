package com.leroymerlin.corp.fr.nuxeo.labs.site.repository;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.RepositoryInit;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;

public class HtmlPageRepositoryInit extends OfmRepositoryInit {

    @Override
    public void populate(CoreSession session) throws ClientException {
        super.populate(session);

        DocumentModel doc = session.createDocumentModel(ofm.getPathAsString()
                + "/" + LabsSiteConstants.Docs.TREE.docName(), "htmlTestPage",
                "HtmlPage");
        HtmlPage page = doc.getAdapter(HtmlPage.class);
        page.setTitle("HTML Test page");
        page.setDescription("Page HTML de test");
        session.createDocument(page.getDocument());

        session.save();
    }

}
