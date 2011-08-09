package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.portal.security.SecurityData;
import com.leroymerlin.corp.fr.nuxeo.portal.security.SecurityDataHelper;

public class SitePermissionsEventListener implements EventListener {

    private static final Log LOG = LogFactory.getLog(SitePermissionsEventListener.class);

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
            LOG.debug("Setting permissions for site '" + doc.getName() + "' ...");
            SecurityData data = SecurityDataHelper.buildSecurityData(doc);
            data.setBlockRightInheritance(true, null);
            data.addModifiablePrivilege((String) doc.getPropertyValue("dc:creator"), SecurityConstants.EVERYTHING, true);
            SecurityDataHelper.updateSecurityOnDocument(doc, data);
            ctx.getCoreSession().saveDocument(doc);
        }
    }

}
