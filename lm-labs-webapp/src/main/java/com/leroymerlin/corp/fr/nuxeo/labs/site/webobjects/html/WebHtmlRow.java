package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager.WidgetType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsHtmlWidget;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsOpensocialGadget;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsWidget;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlSection;

@WebObject(type = "HtmlRow")
public class WebHtmlRow extends MovableElementResource {

    private static final Log LOG = LogFactory.getLog(WebHtmlRow.class);

    private static final String FAILED_TO_DELETE_SECTION = "Failed to delete section ";
    private HtmlRow row;

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        assert args != null && args.length == 4;
        row = (HtmlRow) args[1];
        row.setSession(getCoreSession());
        element = (HtmlSection) args[2];
        index = (Integer) args[3];
        
    }

    @Path("c/{index}")
    public Object doGetContent(@PathParam("index") int contentIndex) {
        HtmlContent content = row.content(contentIndex);
        return newObject("HtmlContent", doc, content);
    }

    @POST
    @Path("@modifyCSS")
    public Object modifyCSS() {
        FormData form = ctx.getForm();
        String cssName = form.getString("cssName");
        String userClassStr = form.getString("userClass");
        String[] split = userClassStr.split(",");
        if (split.length ==1 && StringUtils.isEmpty(split[0])){
            split = new String[0];
        }
        List<String> userClass = new ArrayList<String>(Arrays.asList(split));
        try {
            row.setCssClass(cssName);
            row.setUserClass(userClass);
            saveDocument();
        } catch (ClientException e) {
            throw WebException.wrap(
                    "Failed to change cssName on the row " + doc.getPathAsString(), e);
        }
        return redirect(this.getPrevious().getPrevious().getPath());
    }

    @DELETE
    @Override
    public Response doDelete() {
        try {
            row.remove(getCoreSession());
            saveDocument();
        } catch (Exception e) {
            throw WebException.wrap(
                    FAILED_TO_DELETE_SECTION + doc.getPathAsString(), e);
        }
        return redirect(prev.getPrevious().getPath());
    }

    /**
     * Only one widget per column supported for the moment.
     */
    @POST
    @Path("@manage-widgets")
    public Response doPostManageWidgets() {
        FormData form = ctx.getForm();
        int colNbr = 0;
        for (HtmlContent content : row.getContents()) {
            String widget = form.getString("column" + colNbr);
            colNbr++;
            try {
                syncWidgetsConfig(content, widget);
            } catch (ClientException e) {
                throw WebException.wrap("Problème lors de la sauvegarde des widgets", e);
            }
        }
        // TODO row's widget config

        return Response.ok().build();
    }

    private void saveDocument() throws ClientException {
        CoreSession session = ctx.getCoreSession();
        session.saveDocument(doc);
        session.save();
    }

    public HtmlRow getRow() {
        return row;
    }

    private void syncWidgetsConfig(HtmlContent content, String widget) throws ClientException {
        syncWidgetsConfig(content, Arrays.asList(new String[] {widget}));
    }

    private void syncWidgetsConfig(HtmlContent content, List<String> widgets) throws ClientException {
        List<LabsWidget> gadgets = content.getGadgets(getCoreSession());
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
                        content.removeGadgets(getCoreSession());
                        content.setHtml("");
                    }
                    content.setType(HtmlContent.Type.HTML.type());
                    saveDocument();
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
                            content.removeGadgets(getCoreSession());
                            saveDocument();
                        }
                        LabsWidget gadget = new LabsHtmlWidget(widgetName);
                        LabsGadgetManager service = Framework.getService(LabsGadgetManager.class);
                        String gadgetDocRef = service.addWidgetToHtmlContent(content, doc, gadget, getCoreSession());
                        saveDocument();
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
                        content.removeGadgets(getCoreSession());
                        saveDocument();
                    }
                    DocumentModel gadgetDoc = getCoreSession().createDocumentModel(LabsOpensocialGadget.DOC_TYPE);
                    LabsWidget gadget = new LabsOpensocialGadget(gadgetDoc, widget.split("/")[1]);
                    LabsGadgetManager service = Framework.getService(LabsGadgetManager.class);
                    String gadgetDocRef = service.addWidgetToHtmlContent(content, doc, gadget, getCoreSession());
                    saveDocument();
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
}
