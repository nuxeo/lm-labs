package com.leroymerlin.corp.fr.nuxeo.labs.site.test;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public class HtmlPageRepositoryInit extends OfmRepositoryInit {

    @Override
    public void populate(CoreSession session) throws ClientException {
        super.populate(session);

        DocumentModel doc = session.createDocumentModel(ofm.getPathAsString()
                + "/" + LabsSiteConstants.Docs.TREE.docName(), "htmlTestPage",
                "HtmlPage");
        HtmlPage page = Tools.getAdapter(HtmlPage.class, doc, session);
        page.setTitle("HTML Test page");
        page.setDescription("Page HTML de test");
        session.createDocument(page.getDocument());

        session.save();
    }

}
