package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class SiteCreationEventListener implements EventListener {

    private static final Log LOG = LogFactory.getLog(SiteCreationEventListener.class);

    @Override
    public void handleEvent(Event evt) throws ClientException {
        if (!(evt.getContext() instanceof DocumentEventContext)) {
            return;
        }
        String eventName = evt.getName();
        DocumentEventContext ctx = (DocumentEventContext) evt.getContext();
        DocumentModel doc = ctx.getSourceDocument();
        if (doc != null) {
            String documentType = doc.getType();
            if (!LabsSiteConstants.Docs.SITE.type().equals(documentType)) {
                return;
            }
            LOG.debug("event: " + eventName);
            if (!DocumentEventTypes.DOCUMENT_CREATED.equals(eventName)) {
                return;
            }
            LOG.debug("Creating site '" + doc.getName() + "'");
            // create "tree base"
            LOG.debug("Creating tree ...");
            CoreSession session = ctx.getCoreSession();
            DocumentModel tree = session.createDocumentModel(doc.getPathAsString(),
                    LabsSiteConstants.Docs.TREE.docName(), LabsSiteConstants.Docs.TREE.type());
            LOG.debug("Creating assets ...");
            DocumentModel assets = session.createDocumentModel(
                    doc.getPathAsString(), LabsSiteConstants.Docs.ASSETS.docName(),
                    LabsSiteConstants.Docs.ASSETS.type());
            
            tree.setPropertyValue("dc:title", StringUtils.capitalize(LabsSiteConstants.Docs.TREE.docName()));
            assets.setPropertyValue("dc:title", StringUtils.capitalize(LabsSiteConstants.Docs.ASSETS.docName()));
            
            session.createDocument(tree);
            session.createDocument(assets);
            createWelcomePage(doc, session);
        }
    }

    /**
     * @param doc site's {@link DocumentModel}
     * @param session Core session
     * @return welcome page's {@link DocumentModel}
     * @throws ClientException
     */
    private DocumentModel createWelcomePage(DocumentModel doc, CoreSession session) throws ClientException {
        LOG.debug("Creating welcome page ...");
        DocumentModel welcome = session.createDocumentModel(
                doc.getPathAsString() + "/" + LabsSiteConstants.Docs.TREE.docName(),
                LabsSiteConstants.Docs.WELCOME.docName(), LabsSiteConstants.Docs.WELCOME.type());
        welcome.setPropertyValue("dc:title", StringUtils.capitalize(LabsSiteConstants.Docs.WELCOME.docName()));
        welcome = session.createDocument(welcome);
        LabsSite site = doc.getAdapter(LabsSite.class);
        site.setHomePageRef(welcome.getAdapter(Page.class).getDocument().getId());
        return welcome;
    }

}
