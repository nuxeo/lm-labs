package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;

@WebObject(type = "HtmlRow")
public class WebHtmlRow extends DocumentObject {

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

    @DELETE
    @Override
    public Response doDelete() {
        try {
            row.remove();

            saveDocument();
        } catch (Exception e) {
            throw WebException.wrap(
                    "Failed to delete section " + doc.getPathAsString(), e);
        }
        return redirect(prev.getPrevious()
                .getPath() + "/@views/edit");
    }

    private void saveDocument() throws ClientException {
        CoreSession session = ctx.getCoreSession();
        session.saveDocument(doc);
        session.save();
    }
}
