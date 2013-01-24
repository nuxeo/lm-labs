package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.opensocial.container.shared.webcontent.UserPref;
import org.nuxeo.runtime.api.Framework;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager.WidgetType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsHtmlWidget;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsOpensocialGadget;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsWidget;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;

public class GadgetUtils {

    private static final Log LOG = LogFactory.getLog(GadgetUtils.class);

    public static String encode(List<UserPref> prefs) {

        Joiner joiner = Joiner.on(",");

        return "{"
                + joiner.join(Lists.transform(prefs,
                        new Function<UserPref, String>() {

                            @Override
                            public String apply(UserPref pref) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("\"")
                                        .append(pref.getName())
                                        .append("\":{");
                                sb.append("\"name\":\"")
                                        .append(appendGadgetValueJson(pref.getName()))
                                        .append("\",");
                                String actualValue = appendGadgetValueJson(pref.getActualValue());
                                sb.append("\"value\":\"")
                                        .append(actualValue)
                                        .append("\",");

                                String defaultValue = appendGadgetValueJson(pref.getDefaultValue());
                                sb.append("\"default\":\"")
                                        .append(defaultValue)
                                        .append("\"");
                                return sb.append("}")
                                        .toString();
                            }

                            private String appendGadgetValueJson(String name) {
                                if (StringUtils.isEmpty(name)) {
                                    return "";
                                }
                                return name.replaceAll("\"", "\\\\\"");
                            }

                        })) + "}";

    }
    
    public static void syncWidgetsConfig(HtmlContent content, String widget, DocumentModel doc, CoreSession session) throws ClientException {
        syncWidgetsConfig(content, Arrays.asList(new String[] {widget}), doc, session);
    }
    
    public static void syncWidgetsConfig(HtmlContent content, List<String> widgets, DocumentModel doc, CoreSession session) throws ClientException {
        List<LabsWidget> gadgets = content.getGadgets(session);
        if (widgets.isEmpty())
        {
            return /*false*/;
        }
        String widget = widgets.get(0);
        if (!widget.startsWith(WidgetType.OPENSOCIAL.type() + "/")) {
            // "html/"
            String widgetName = widget.split("/")[1];
            if ("editor".equals(widgetName)) {
                // "html/editor"
                try {
                    if (!gadgets.isEmpty()) {
                        content.removeGadgets(session);
                        content.setHtml("");
                    }
                    content.setType(HtmlContent.Type.HTML.type());
                    saveDocument(doc, session);
                } catch (ClientException e) {
                    LOG.error(e, e);
                }
            } else {
                // "html/"
                try {
                    if (!gadgets.isEmpty() && widget.equals(WidgetType.HTML.type() + "/" + gadgets.get(0).getName())) {
                        // same gadget, do nothing.
                    } else {
                        if (!gadgets.isEmpty()) {
                            content.removeGadgets(session);
                            saveDocument(doc, session);
                        }
                        LabsWidget gadget = new LabsHtmlWidget(widgetName);
                        LabsGadgetManager service = Framework.getService(LabsGadgetManager.class);
                        String gadgetDocRef = service.addWidgetToHtmlContent(content, doc, gadget, session);
                        saveDocument(doc, session);
                    }
                } catch (ClientException e) {
                    LOG.error(e, e);
                } catch (Exception e) {
                    LOG.error(e, e);
                }
            }
        } else {
            // "opensocial/"
            try {
                if (!gadgets.isEmpty() && widget.equals(WidgetType.OPENSOCIAL.type() + "/" + gadgets.get(0).getName())) {
                    // same gadget, do nothing.
                } else {
                    if (!gadgets.isEmpty()) {
                        content.removeGadgets(session);
                        saveDocument(doc, session);
                    }
                    DocumentModel gadgetDoc = session.createDocumentModel(LabsOpensocialGadget.DOC_TYPE);
                    LabsWidget gadget = new LabsOpensocialGadget(gadgetDoc, widget.split("/")[1]);
                    LabsGadgetManager service = Framework.getService(LabsGadgetManager.class);
                    String gadgetDocRef = service.addWidgetToHtmlContent(content, doc, gadget, session);
                    saveDocument(doc, session);
//                    if ("rss".equals(gadget.getName())) {
//                        gadgetDoc = getCoreSession().getDocument(new IdRef(gadgetDocRef));
//                        OpenSocialAdapter adapter = (OpenSocialAdapter) Tools.getAdapter(WebContentAdapter.class, gadgetDoc, getCoreSession());
//                        OpenSocialData data = adapter.getData();
//                        List<UserPref> userPrefs = new ArrayList<UserPref>();
//                        UserPref userPref = new UserPref("rssUrl1", DataType.STRING);
//                        userPref.setActualValue("http://www.7sur7.be/rss.xml");
//                        userPrefs.add(userPref);
//                        userPref = new UserPref("rssUrl2", DataType.STRING);
//                        userPref.setActualValue("http://intralm2.fr.corp.leroymerlin.com/site/site-actualites/@rss");
//                        userPrefs.add(userPref);
//
//                        data.setUserPrefs(userPrefs);
//                        adapter.feedFrom(data);
//                        gadgetDoc = getCoreSession().saveDocument(gadgetDoc);
//                    }
                }
            } catch (ClientException e) {
                LOG.error(e, e);
            } catch (Exception e) {
                LOG.error(e, e);
            }
        }
    }
    
    private static void saveDocument(DocumentModel doc, CoreSession session) throws ClientException {
        session.saveDocument(doc);
        session.save();
    }

}
