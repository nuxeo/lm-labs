package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.rendering.api.RenderingEngine;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsGadgetManager.WidgetType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsOpensocialGadget;
import com.leroymerlin.corp.fr.nuxeo.labs.site.gadget.LabsWidget;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;

@WebObject(type = "HtmlContent")
public class WebHtmlContent extends DocumentObject {

    private static final String FAILED_TO_POST_HTML_CONTENT = "Failed to post html content\n";

    private HtmlContent content;

    private static final Log LOG = LogFactory.getLog(WebHtmlContent.class);

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        assert args != null && args.length == 2;
        content = (HtmlContent) args[1];
        RenderingEngine engine = ctx.getEngine()
                .getRendering();
        engine.setSharedVariable("content", content);
    }

    @POST
    @Override
    public Response doPost() {
        FormData form = ctx.getForm();
        try {
            content.setHtml(form.getString("content"));
            saveDocument();
            return Response.status(Status.OK).build();
        } catch (ClientException e) {
            LOG.error(FAILED_TO_POST_HTML_CONTENT + e.getMessage());
            return Response.serverError().status(Status.NOT_MODIFIED).entity(FAILED_TO_POST_HTML_CONTENT).build();
        }

    }

    @Override
    public Response doDelete() {
        // Do nothing, but don't delete the doc !
        return redirect(prev.getPrevious()
                .getPrevious()
                .getPath());
    }

    private void saveDocument() throws ClientException {
        CoreSession session = ctx.getCoreSession();
        session.saveDocument(doc);
        session.save();
    }

    //@Path("w/{index}") // For the moment only ONE widget is possible
    @Path("w")
    public Object doGetContent(/*@PathParam("index") String widgetIndex*/) throws ClientException {
        List<LabsWidget> widgets = content.getGadgets(getCoreSession());
        if (!widgets.isEmpty()) {
            LabsWidget widget = widgets.get(0);
            if (WidgetType.OPENSOCIAL.equals(widget.getType())) {
                DocumentRef ref = ((LabsOpensocialGadget)widget).getDoc().getRef();
                if (getCoreSession().exists(ref)) {
                    DocumentModel gadgetDoc = getCoreSession().getDocument(ref);
                    return newObject("HtmlWidget", gadgetDoc, doc, content, widget);
                }
                return Response.noContent().build();
            }
            return newObject("HtmlWidget", null, doc, content, widget);
        }
        return Response.noContent().build();
    }

    @POST
    @Path("@manage-widgets")
    public Response doSaveWidgetConfig() {
        FormData form = ctx.getForm();
        CoreSession session = getContext().getCoreSession();
        // TODO widget's config

        return Response.ok().build();
    }

    public List<LabsWidget> getGagdets() throws ClientException {
        return content.getGadgets(getCoreSession());
    }

}
