package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class SiteCreationEventListener implements EventListener {

    @Override
    public void handleEvent(Event evt) throws ClientException {
        if (!(evt.getContext() instanceof DocumentEventContext)) {
            return;
        }

        DocumentEventContext ctx = (DocumentEventContext) evt.getContext();
        DocumentModel doc = ctx.getSourceDocument();
        String documentType = doc.getType();

        if (!LabsSiteConstants.Docs.SITE.type().equals(documentType)) {
            return;
        }

        // create "tree base"
        CoreSession session = ctx.getCoreSession();
        DocumentModel tree = session.createDocumentModel(doc.getPathAsString(),
                LabsSiteConstants.Docs.TREE.docName(), LabsSiteConstants.Docs.TREE.type());
        DocumentModel assets = session.createDocumentModel(
                doc.getPathAsString(), LabsSiteConstants.Docs.ASSETS.docName(),
                LabsSiteConstants.Docs.ASSETS.type());
        DocumentModel welcome = session.createDocumentModel(
                doc.getPathAsString() + "/" + LabsSiteConstants.Docs.TREE.docName(),
                LabsSiteConstants.Docs.WELCOME.docName(), LabsSiteConstants.Docs.WELCOME.type());
        
        tree.setPropertyValue("dc:title", StringUtils.capitalize(LabsSiteConstants.Docs.TREE.docName()));
        assets.setPropertyValue("dc:title", StringUtils.capitalize(LabsSiteConstants.Docs.ASSETS.docName()));
        welcome.setPropertyValue("dc:title", StringUtils.capitalize(LabsSiteConstants.Docs.WELCOME.docName()));
        
        session.createDocument(tree);
        session.createDocument(assets);
        session.createDocument(welcome);
    }

}
