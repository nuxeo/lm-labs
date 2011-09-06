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

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlSection;

@WebObject(type = "HtmlSection")
public class WebHtmlSection extends DocumentObject {

    private HtmlSection section;

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        assert args != null && args.length == 2;
        section = (HtmlSection) args[1];
    }

    @DELETE
    @Override
    public Response doDelete() {
        try {
            section.remove();

            saveDocument();
        } catch (Exception e) {
            throw WebException.wrap(
                    "Failed to delete section " + doc.getPathAsString(), e);
        }
        return redirect(prev.getPath() + "/@views/edit");
    }

    @Override
    @POST
    public Response doPost() {
        try {
            FormData form = ctx.getForm();
            String action = form.getString("action");

            if ("addrow".equals(action)) {
                HtmlRow row = section.addRow();
                String rowTemplate = form.getString("rowTemplate");
                RowTemplate.initRow(row, RowTemplate.fromString(rowTemplate));
            } else if ("editsection".equals(action)) {
                String title = form.getString("title");
                String description = form.getString("description");
                section.setTitle(title);
                section.setDescription(description);
            }

            saveDocument();
        } catch (ClientException e) {
            throw WebException.wrap(
                    "Failed to delete section " + doc.getPathAsString(), e);
        }
        return redirect(prev.getPath() + "/@views/edit");
    }

    @Path("r/{index}")
    public Object getRow(@PathParam("index") int rowIndex) {
        HtmlRow row = section.row(rowIndex);
        return newObject("HtmlRow", doc, row);
    }

    private void saveDocument() throws ClientException {
        CoreSession session = ctx.getCoreSession();
        session.saveDocument(doc);
        session.save();
    }
}
