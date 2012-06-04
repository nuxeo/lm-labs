package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.opensocial.container.server.webcontent.api.WebContentAdapter;
import org.nuxeo.opensocial.container.server.webcontent.gadgets.opensocial.OpenSocialAdapter;
import org.nuxeo.opensocial.container.shared.webcontent.OpenSocialData;
import org.nuxeo.opensocial.container.shared.webcontent.UserPref;
import org.nuxeo.opensocial.container.shared.webcontent.enume.DataType;

import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsWidget;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

@WebObject(type = "HtmlWidget")
public class WebHtmlWidget extends DocumentObject {

    private static final Log LOG = LogFactory.getLog(WebHtmlWidget.class);

    private DocumentModel htmlPage;
    private LabsWidget widget;
    private HtmlContent parentContent;
    private HashMap<String, String> prefsMap = new LinkedHashMap<String, String>();
    private HashMap<String, String> prefValuesMap = new LinkedHashMap<String, String>();

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        assert args != null && args.length == 4;
        //widgetDoc = (DocumentModel) args[0];
        htmlPage = (DocumentModel) args[1];
        parentContent = (HtmlContent) args[2];
        widget = (LabsWidget) args[3];
//        RenderingEngine engine = ctx.getEngine().getRendering();
//        engine.setSharedVariable("content", content);
        setUserPreferences();
    }

    @Override
    public Response doDelete() {
        CoreSession session = getCoreSession();
        parentContent.removeGadgets(); // For the moment only ONE widget is possible
        try {
            parentContent.setType(HtmlContent.Type.HTML.type());
            htmlPage = session.saveDocument(htmlPage);
        } catch (ClientException e) {
            LOG.error("Unable to reset content to HTML editor", e);
        }
        return redirect(ctx.getBasePath());
    }

    public LabsWidget getWidget() {
        return widget;
    }

    public Map<String, String> getUserPreferences() {
        return prefsMap;
    }

    public Map<String, String> getUserPreferenceValues() {
        return prefValuesMap;
    }

    @Override
    public Response doPut() {
        OpenSocialAdapter adapter = (OpenSocialAdapter) Tools.getAdapter(WebContentAdapter.class, doc, getCoreSession());
        FormData form = ctx.getForm();
        boolean modified = false;
        try {
            OpenSocialData data = adapter.getData();
            List<UserPref> userPrefs = data.getUserPrefs();
            for (UserPref pref : userPrefs) {
                if (pref.getDataType() == DataType.STRING || pref.getDataType() == DataType.ENUM || pref.getDataType() == DataType.NUMBER) {
                    String value = form.getString(pref.getName());
                    if (value != null && !value.equals(pref.getActualValue())) {
                        pref.setActualValue(value);
                        modified = true;
                    }
                } else {
                    // TODO other types
                }
            }
            if (modified) {
                data.setUserPrefs(userPrefs);
                adapter.feedFrom(data);
                saveWidgetDocument();
            }
        } catch (ClientException e) {
            LOG.error("Unable to get gadget's data", e);
            return Response.notModified().build();
        }
        if (modified) {
            return Response.ok().build();
        } else {
            return Response.notModified().build();
        }
    }

    private void setUserPreferences() {
        OpenSocialAdapter adapter = (OpenSocialAdapter) Tools.getAdapter(WebContentAdapter.class, doc, getCoreSession());
        try {
            OpenSocialData data = adapter.getData();
            for (UserPref pref : data.getUserPrefs()) {
                if (pref.getDataType() == DataType.BOOL) {
                    prefsMap.put(pref.getName(), "boolean");
                } else if (pref.getDataType() == DataType.STRING) {
                    prefsMap.put(pref.getName(), "string");
                } else if (pref.getDataType() == DataType.NUMBER) {
                    prefsMap.put(pref.getName(), "string");
                } else if (pref.getDataType() == DataType.ENUM) {
                    prefsMap.put(pref.getName(), "enum");
                } else {
                    // TODO other types
                }
                String value = pref.getActualValue();
                if (value == null) {
                    value = pref.getDefaultValue();
                }
                prefValuesMap.put(pref.getName(), value);
            }
        } catch (ClientException e) {
            LOG.error("Unable to get gadget's data", e);
        }
    }

    public UserPref getUserPrefByName(final String prefName) throws ClientException {
        OpenSocialAdapter adapter = (OpenSocialAdapter) Tools.getAdapter(WebContentAdapter.class, doc, getCoreSession());
        OpenSocialData data = adapter.getData();
        return data.getUserPrefByName(prefName);
    }

    private void saveWidgetDocument() throws ClientException {
        getCoreSession().saveDocument(doc);
    }
}
