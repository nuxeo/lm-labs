package com.leroymerlin.corp.fr.nuxeo.labs.site.gadget;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.ecm.spaces.api.Constants;
import org.nuxeo.opensocial.container.server.webcontent.api.WebContentAdapter;
import org.nuxeo.opensocial.container.server.webcontent.gadgets.opensocial.OpenSocialAdapter;
import org.nuxeo.opensocial.container.shared.webcontent.UserPref;
import org.nuxeo.opensocial.gadgets.service.ExternalGadgetDescriptor;
import org.nuxeo.opensocial.gadgets.service.GadgetServiceImpl;
import org.nuxeo.opensocial.gadgets.service.api.GadgetDeclaration;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager.WidgetType;

public class LabsOpensocialGadget implements LabsWidget {

    private static final Log LOG = LogFactory.getLog(LabsOpensocialGadget.class);

    public static final String DOC_TYPE = Constants.OPEN_SOCIAL_GADGET_DOCUMENT_TYPE;

    private static final String GADGET_DIR_SCHEMA = "externalgadget";

    private static final String EXTERNAL_PROP_ID = "id";

    private static final String EXTERNAL_PROP_NAME = "label";

    private static final String EXTERNAL_PROP_CATEGORY = "category";

    private static final String EXTERNAL_PROP_ENABLED = "enabled";

    private static final String EXTERNAL_PROP_URL = "url";

    private static final String EXTERNAL_PROP_ICON_URL = "iconUrl";

    private DocumentModel doc;

    private String name;

    private WidgetType type = WidgetType.OPENSOCIAL;

	private String specUrl;

    public LabsOpensocialGadget(DocumentModel doc) {
        this.doc = doc;
        if (this.name == null) {
            OpenSocialAdapter os = (OpenSocialAdapter) doc.getAdapter(WebContentAdapter.class);
            if (os != null) {
                try {
                    this.name = os.getData().getGadgetName();
                    this.specUrl = os.getData().getGadgetDef();
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
        Map<String, GadgetDeclaration> externalGadgets = getExternalGadgets();
        if (externalGadgets.containsKey(name)) {
        	try {
				this.specUrl = externalGadgets.get(name).getGadgetDefinition().toString();
			} catch (MalformedURLException e) {
				LOG.error("Unable to get gadget URL for gadget " + name + ":" + e.getMessage());
			}
        } else {
        	this.specUrl = "http://localhost:8080/nuxeo/site/gadgets/" + name + "/" + name + ".xml";
        }
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

    public String getSpecUrl() {
    	return this.specUrl;
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

    private Map<String, GadgetDeclaration> getExternalGadgets() {
        HashMap<String, GadgetDeclaration> result = new HashMap<String, GadgetDeclaration>();
        try {
            Session session = null;
            try {
                DirectoryService dirService = Framework.getService(DirectoryService.class);
                session = dirService.open(GadgetServiceImpl.GADGET_DIRECTORY);
                for (DocumentModel model : session.getEntries()) {
                    String name = (String) model.getProperty(GADGET_DIR_SCHEMA,
                            EXTERNAL_PROP_NAME);
                    String id = (String) model.getProperty(GADGET_DIR_SCHEMA,
                            EXTERNAL_PROP_ID);
                    String category = (String) model.getProperty(
                            GADGET_DIR_SCHEMA, EXTERNAL_PROP_CATEGORY);
                    long enabled = (Long) model.getProperty(GADGET_DIR_SCHEMA,
                            EXTERNAL_PROP_ENABLED);
                    boolean disabled = enabled != 0 ? false : true;

                    String gadgetDefinition = (String) model.getProperty(
                            GADGET_DIR_SCHEMA, EXTERNAL_PROP_URL);
                    String iconURL = (String) model.getProperty(
                            GADGET_DIR_SCHEMA, EXTERNAL_PROP_ICON_URL);
                    ExternalGadgetDescriptor desc = new ExternalGadgetDescriptor(
                            category, disabled, new URL(gadgetDefinition),
                            iconURL, name);
                    if (!desc.getDisabled()) {
                        result.put(id, desc);
                    }
                }
            } finally {
                if (session != null) {
                    session.close();
                }
            }

        } catch (Exception e) {
            LOG.error("Unable to read external gadget directory!", e);

        }
        return result;
    	
    }
}
