package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import static org.nuxeo.ecm.spaces.api.Constants.WC_OPEN_SOCIAL_USER_PREFS_PROPERTY;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.spaces.impl.docwrapper.DocGadgetImpl;

import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager;

public class OpensocialWidgetCreationEventListener implements EventListener {

    private static final Log LOG = LogFactory.getLog(OpensocialWidgetCreationEventListener.class);

    @Override
    public void handleEvent(Event evt) throws ClientException {
        if (!(evt.getContext() instanceof DocumentEventContext)) {
            return;
        }
        if (!DocumentEventTypes.BEFORE_DOC_UPDATE.equals(evt.getName())) {
            return;
        }
        DocumentEventContext ctx = (DocumentEventContext) evt.getContext();
        DocumentModel docGadget = ctx.getSourceDocument();
        if (docGadget != null && DocGadgetImpl.TYPE.equals(docGadget.getType())) {
            List<Map<String, Serializable>> tempSavedUserPrefs = (List<Map<String, Serializable>>) docGadget.getPropertyValue(WC_OPEN_SOCIAL_USER_PREFS_PROPERTY);
            boolean modified = false;
            for (Map<String, Serializable> preference : tempSavedUserPrefs) {
                String name = (String) preference.get("name");
                String value = (String) preference.get("value");
                if (LabsGadgetManager.GADGET_ID_PREFERENCE_NAME.equals(name)) {
                    if (!StringUtils.equals(docGadget.getId(), value)) {
                        preference.put("value", docGadget.getId());
                        modified = true;
                    }
                    break;
                }
            }
            if (modified) {
                docGadget.setPropertyValue(WC_OPEN_SOCIAL_USER_PREFS_PROPERTY, (Serializable) tempSavedUserPrefs);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("User property '" + LabsGadgetManager.GADGET_ID_PREFERENCE_NAME + "' updated for gadget " + docGadget.getPathAsString());
                }
            }
        }
    }
}
