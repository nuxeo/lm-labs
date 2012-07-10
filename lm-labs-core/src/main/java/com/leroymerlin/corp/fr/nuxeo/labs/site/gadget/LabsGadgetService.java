package com.leroymerlin.corp.fr.nuxeo.labs.site.gadget;

import static org.nuxeo.ecm.spaces.api.Constants.OPEN_SOCIAL_GADGET_DOCUMENT_TYPE;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.opensocial.container.server.webcontent.api.WebContentAdapter;
import org.nuxeo.opensocial.container.server.webcontent.gadgets.opensocial.OpenSocialAdapter;
import org.nuxeo.opensocial.container.shared.webcontent.OpenSocialData;
import org.nuxeo.opensocial.container.shared.webcontent.UserPref;
import org.nuxeo.opensocial.gadgets.service.ExternalGadgetDescriptor;
import org.nuxeo.opensocial.gadgets.service.GadgetServiceImpl;
import org.nuxeo.opensocial.gadgets.service.api.GadgetDeclaration;
import org.nuxeo.opensocial.gadgets.service.api.GadgetService;
import org.nuxeo.opensocial.helper.OpenSocialGadgetHelper;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.DefaultComponent;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;

public class LabsGadgetService extends DefaultComponent implements LabsGadgetManager {

    private static final Log LOG = LogFactory.getLog(LabsGadgetService.class);

    private static final String GADGET_DIR_SCHEMA = "externalgadget";

    private static final String EXTERNAL_PROP_ID = "id";

    private static final String EXTERNAL_PROP_NAME = "label";

    private static final String EXTERNAL_PROP_CATEGORY = "category";

    private static final String EXTERNAL_PROP_ENABLED = "enabled";

    private static final String EXTERNAL_PROP_URL = "url";

    private static final String EXTERNAL_PROP_ICON_URL = "iconUrl";

//    private static final Log LOG = LogFactory.getLog(LabsGadgetService.class);

    /* (non-Javadoc)
     * @see com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager#addWidgetToHtmlContent(com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent, org.nuxeo.ecm.core.api.DocumentModel, com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsWidget, org.nuxeo.ecm.core.api.CoreSession)
     */
    @Override
    public String addWidgetToHtmlContent(HtmlContent content, DocumentModel htmlPageDoc, LabsWidget widget, CoreSession session) throws ClientException {
        if (WidgetType.OPENSOCIAL.equals(widget.getType())) {
            DocumentModel docGadget = session.createDocumentModel(
                    htmlPageDoc.getPathAsString(), widget.getName(),
                    OPEN_SOCIAL_GADGET_DOCUMENT_TYPE);
            OpenSocialAdapter os = (OpenSocialAdapter) docGadget.getAdapter(WebContentAdapter.class);
            os.setGadgetDefUrl(OpenSocialGadgetHelper.computeGadgetDefUrlBeforeSave(getGadgetDefUrlFor(widget.getName())));
            os.setGadgetName(widget.getName());
            os.setPosition(0);
            OpenSocialData data = os.getData();
            boolean modified = false;
            List<UserPref> userPrefs = data.getUserPrefs();
            for (UserPref pref : userPrefs) {
                String defaultValue = pref.getDefaultValue();
                if (!StringUtils.isBlank(defaultValue)) {
                    pref.setActualValue(defaultValue);
                    modified = true;
                }
            }
            if (modified) {
                data.setUserPrefs(userPrefs);
                os.feedFrom(data);
                modified = false;
            }
            docGadget = session.createDocument(docGadget);
            os = (OpenSocialAdapter) docGadget.getAdapter(WebContentAdapter.class);
            data = os.getData();
            UserPref nxidGadgetPref = data.getUserPrefByName(GADGET_ID_PREFERENCE_NAME);
            if (nxidGadgetPref != null) {
                userPrefs = data.getUserPrefs();
                for (UserPref pref : userPrefs) {
                    if (GADGET_ID_PREFERENCE_NAME.equals(pref.getName())) {
                        pref.setActualValue(docGadget.getId());
                        data.setUserPrefs(userPrefs);
                        os.feedFrom(data);
                        docGadget = session.saveDocument(docGadget);
                    }
                }
            }
            session.save();
            os = (OpenSocialAdapter) docGadget.getAdapter(WebContentAdapter.class);
            data = os.getData();
            nxidGadgetPref = data.getUserPrefByName(GADGET_ID_PREFERENCE_NAME);
            content.setType("widgetcontainer");
            content.addWidgetRef(docGadget.getRef().toString());
            return docGadget.getRef().toString();
        } else if (WidgetType.HTML.equals(widget.getType())) {
            content.setType("widgetcontainer");
            content.setHtml("");
            content.addWidgetRef(widget.getName());
            return widget.getName();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager#removeAllGadgetsOfHtmlContent(com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent, org.nuxeo.ecm.core.api.CoreSession)
     */
    @Override
    public boolean removeAllGadgetsOfHtmlContent(HtmlContent content, CoreSession session) throws ClientException {
        boolean removed = false;
        if ("widgetcontainer".equals(content.getType())) {
            List<LabsWidget> widgets = content.getGadgets(session);
            for (LabsWidget widget : widgets) {
                if (widget instanceof LabsOpensocialGadget) {
                    removed = true;
                    String ref = ((LabsOpensocialGadget) widget).getDoc().getRef().toString();
                    session.removeDocument(((LabsOpensocialGadget) widget).getDoc().getRef());
                    content.removeWidgetRef(ref);
                } else if (widget instanceof LabsHtmlWidget) {
                    removed = true;
                    content.removeWidgetRef(widget.getName());
                }
                if (removed) {
                }
            }
            if (removed) {
                session.save();
            }
        }
        return removed;
    }

    public Map<String, GadgetDeclaration> getExternalGadgets() {
        HashMap<String, GadgetDeclaration> result = new HashMap<String, GadgetDeclaration>();
        try {
            Session session = null;
            try {
                DirectoryService dirService = Framework.getService(DirectoryService.class);
                session = dirService.open(GadgetServiceImpl.GADGET_DIRECTORY);
                for (DocumentModel model : session.getEntries()) {
                    String label = (String) model.getProperty(GADGET_DIR_SCHEMA, EXTERNAL_PROP_NAME);
                    String id = (String) model.getProperty(GADGET_DIR_SCHEMA, EXTERNAL_PROP_ID);
                    String category = (String) model.getProperty( GADGET_DIR_SCHEMA, EXTERNAL_PROP_CATEGORY);
                    long enabled = (Long) model.getProperty(GADGET_DIR_SCHEMA, EXTERNAL_PROP_ENABLED);
                    boolean disabled = enabled != 0 ? false : true;

                    String gadgetDefinition = (String) model.getProperty( GADGET_DIR_SCHEMA, EXTERNAL_PROP_URL);
                    String iconURL = (String) model.getProperty( GADGET_DIR_SCHEMA, EXTERNAL_PROP_ICON_URL);
                    ExternalGadgetDescriptor desc = new ExternalGadgetDescriptor(
                            category, disabled, new URL(gadgetDefinition),
                            iconURL, label);
                    if (!desc.getDisabled()) {
                    	result.put(label, desc);
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

    private String getGadgetDefUrlFor(String name) {
        GadgetService gs;
        try {
            gs = Framework.getService(GadgetService.class);
        } catch (Exception e) {
            return null;
        }
        for (GadgetDeclaration gadgetDef : gs.getGadgetList()) {
            if (gadgetDef.getName().equals(name)) {
                try {
                    return gadgetDef.getGadgetDefinition().toString();
                } catch (MalformedURLException e) {
                    return null;
                }
            }
        }

        return null;
    }

}
