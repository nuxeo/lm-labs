package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;

@WebObject(type = "HtmlRow")
public class WebHtmlRow extends DocumentObject {

    private static final String FAILED_TO_DELETE_SECTION = "Failed to delete section ";
    private HtmlRow row;

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        assert args != null && args.length == 2;
        row = (HtmlRow) args[1];
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
        try {
            row.setCssClass(cssName);
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
            row.remove();

            saveDocument();
        } catch (Exception e) {
            throw WebException.wrap(
                    FAILED_TO_DELETE_SECTION + doc.getPathAsString(), e);
        }
        return redirect(prev.getPrevious().getPath());
    }

    private void saveDocument() throws ClientException {
        CoreSession session = ctx.getCoreSession();
        session.saveDocument(doc);
        session.save();
    }
}
