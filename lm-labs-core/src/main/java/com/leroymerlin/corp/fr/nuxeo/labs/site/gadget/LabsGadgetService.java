package com.leroymerlin.corp.fr.nuxeo.labs.site.gadget;

import static org.nuxeo.ecm.spaces.api.Constants.OPEN_SOCIAL_GADGET_DOCUMENT_TYPE;

import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.opensocial.container.server.webcontent.api.WebContentAdapter;
import org.nuxeo.opensocial.container.server.webcontent.gadgets.opensocial.OpenSocialAdapter;
import org.nuxeo.opensocial.container.shared.webcontent.OpenSocialData;
import org.nuxeo.opensocial.container.shared.webcontent.UserPref;
import org.nuxeo.opensocial.gadgets.service.api.GadgetDeclaration;
import org.nuxeo.opensocial.gadgets.service.api.GadgetService;
import org.nuxeo.opensocial.helper.OpenSocialGadgetHelper;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.DefaultComponent;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;

public class LabsGadgetService extends DefaultComponent implements LabsGadgetManager {

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

    private static String getGadgetDefUrlFor(String name) {
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
