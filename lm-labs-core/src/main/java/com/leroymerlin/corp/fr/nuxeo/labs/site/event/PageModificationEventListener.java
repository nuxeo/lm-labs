package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import com.leroymerlin.corp.fr.nuxeo.labs.site.notification.MailNotification;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.State;

public class PageModificationEventListener implements EventListener {

    private static final Log LOG = LogFactory.getLog(PageModificationEventListener.class);

    @Override
    public void handleEvent(Event evt) throws ClientException {
        if (!(evt.getContext() instanceof DocumentEventContext)) {
            return;
        }
        String eventName = evt.getName();
        LOG.debug("event: " + eventName);
        if (!DocumentEventTypes.DOCUMENT_UPDATED.equals(eventName)) {
            return;
        }
        DocumentEventContext ctx = (DocumentEventContext) evt.getContext();
        DocumentModel doc = ctx.getSourceDocument();
        if (doc != null) {
            if (!Docs.notifiableDocs().contains(Docs.fromString(doc.getType()))) {
                return;
            }
            if (!State.PUBLISH.getState().equals(doc.getCurrentLifeCycleState())) {
                return;
            }
            if (!doc.getAdapter(MailNotification.class).isToBeNotified()) {
                return;
            }
            try {
                doc.getAdapter(MailNotification.class).fireNotificationEvent();
            } catch (Exception e) {
                // TODO
                LOG.error(e, e);
            }
        }

    }

}
