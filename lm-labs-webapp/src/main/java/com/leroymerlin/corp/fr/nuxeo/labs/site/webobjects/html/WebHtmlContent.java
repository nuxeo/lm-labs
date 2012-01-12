package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.rendering.api.RenderingEngine;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

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

}
