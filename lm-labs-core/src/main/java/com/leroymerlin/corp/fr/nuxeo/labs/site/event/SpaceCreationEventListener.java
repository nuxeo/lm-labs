package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.FacetNames;

public class SpaceCreationEventListener implements EventListener {

    private static final Log LOG = LogFactory.getLog(SpaceCreationEventListener.class);

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
            if (!Docs.DASHBOARD.type().equals(documentType)) {
                return;
            }
            LOG.debug("event: " + eventName);
            if (!DocumentEventTypes.DOCUMENT_CREATED.equals(eventName)) {
                return;
            }
            doc.addFacet(FacetNames.LABSPAGE);
        }
    }

}
