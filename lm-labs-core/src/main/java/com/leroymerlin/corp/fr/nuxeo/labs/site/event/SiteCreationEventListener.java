package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.ISiteConstant;

public class SiteCreationEventListener implements EventListener {

    @Override
    public void handleEvent(Event evt) throws ClientException {
        if (!(evt.getContext() instanceof DocumentEventContext)) {
            return;
        }

        DocumentEventContext ctx = (DocumentEventContext) evt.getContext();
        DocumentModel doc = ctx.getSourceDocument();
        String documentType = doc.getType();

        if (!ISiteConstant.DocType.SITE.equals(documentType)) {
            return;
        }

        // create "tree base"
        CoreSession session = ctx.getCoreSession();
        DocumentModel tree = session.createDocumentModel(doc.getPathAsString(),
                ISiteConstant.DocPath.TREE, ISiteConstant.DocType.TREE);
        DocumentModel assets = session.createDocumentModel(
                doc.getPathAsString(), ISiteConstant.DocPath.ASSETS,
                ISiteConstant.DocType.ASSETS);
        DocumentModel welcome = session.createDocumentModel(
                doc.getPathAsString() + "/" + ISiteConstant.DocPath.TREE,
                ISiteConstant.DocPath.WELCOME, ISiteConstant.DocType.PAGE_BLOCS);
        session.createDocument(tree);
        session.createDocument(assets);
        session.createDocument(welcome);
        session.save();
    }

}
