package com.leroymerlin.corp.fr.nuxeo.labs.site.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageClasseurCreationEventListener implements EventListener {

    private static final Log LOG = LogFactory.getLog(PageClasseurCreationEventListener.class);

    @Override
    public void handleEvent(Event evt) throws ClientException {
        if (!(evt.getContext() instanceof DocumentEventContext)) {
            return;
        }

        DocumentEventContext ctx = (DocumentEventContext) evt.getContext();
        DocumentModel site = ctx.getSourceDocument();
        if (site != null) {
            if (!Docs.SITE.type().equals(site.getType())) {
                return;
            }
            CoreSession session = ctx.getCoreSession();
            if (!session.exists(new PathRef(site.getPathAsString() + "/" + "pageclasseur"))) {
                LOG.debug("Creating Page Classeur in " + site.getPathAsString());
                DocumentModel classeur = session.createDocumentModel(site.getPathAsString(), "pageclasseur", Docs.PAGECLASSEUR.type());
                session.createDocument(classeur);
            }
        }

    }

}
