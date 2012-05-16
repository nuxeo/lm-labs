package com.leroymerlin.corp.fr.nuxeo.labs.site.gadget;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.spaces.api.Constants;
import org.nuxeo.opensocial.container.server.webcontent.api.WebContentAdapter;
import org.nuxeo.opensocial.container.server.webcontent.gadgets.opensocial.OpenSocialAdapter;
import org.nuxeo.opensocial.container.shared.webcontent.UserPref;

import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager.WidgetType;

public class LabsOpensocialGadget implements LabsWidget {

    private static final Log LOG = LogFactory.getLog(LabsOpensocialGadget.class);

    public static final String DOC_TYPE = Constants.OPEN_SOCIAL_GADGET_DOCUMENT_TYPE;

    private DocumentModel doc;

    private String name;

    private WidgetType type = WidgetType.OPENSOCIAL;

    public LabsOpensocialGadget(DocumentModel doc) {
        this.doc = doc;
        if (this.name == null) {
            OpenSocialAdapter os = (OpenSocialAdapter) doc.getAdapter(WebContentAdapter.class);
            if (os != null) {
                try {
                    this.name = os.getData().getGadgetName();
                } catch (ClientException e) {
                    LOG.error("Unable to get Opensocial gadget name.", e);
                }
            } else {
                LOG.error("Unable to get Opensocial gadget name.");
            }
        }
    }

    public LabsOpensocialGadget(DocumentModel doc, String name) {
        this.doc = doc;
        this.name = name;
    }

    public DocumentModel getDoc() {
        return doc;
    }

    public List<UserPref> getUserPrefs() {
        try {
            return ((OpenSocialAdapter) doc.getAdapter(WebContentAdapter.class)).getData().getUserPrefs();
        } catch (ClientException e) {
            return new ArrayList<UserPref>();
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setType(WidgetType type) {
        this.type = type;
    }

    @Override
    public WidgetType getType() {
        return type;
    }

}
