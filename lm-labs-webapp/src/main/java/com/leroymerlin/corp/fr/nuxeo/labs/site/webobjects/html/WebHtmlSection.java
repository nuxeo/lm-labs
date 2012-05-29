package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import javax.ws.rs.DELETE;
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

    private static final String FAILED_TO_POST_SECTION = "Failed to post html section ";
    private static final String FAILED_TO_DELETE_SECTION = "Failed to delete html section ";
    private HtmlSection section;
    private int sectionIndex = -1;

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        assert args != null && args.length >= 2;
        section = (HtmlSection) args[1];
        if (args.length > 2) {
            sectionIndex = (Integer) args[2];
        }
    }

    @DELETE
    @Override
    public Response doDelete() {
        try {
            section.remove();

            saveDocument();
        } catch (Exception e) {
            throw WebException.wrap(
                    FAILED_TO_DELETE_SECTION + doc.getPathAsString(), e);
        }
        return redirect(prev.getPath());
    }

    @Override
    public Response doPost() {
        FormData form = ctx.getForm();
        CoreSession session = ctx.getCoreSession();
        try {
            String cssClass = form.getString("cssClass");
            HtmlRow row = section.addRow(cssClass, session);
            String rowTemplate = form.getString("rowTemplate");
            row.initTemplate(rowTemplate, session);

            saveDocument();
        } catch (ClientException e) {
            throw WebException.wrap(
                    FAILED_TO_POST_SECTION + doc.getPathAsString(), e);
        }
        return redirect(prev.getPath() + "#row_s" + sectionIndex + "_r" + (section.getRows(session).size() - 1));
    }

    @Override
    public Response doPut() {
        FormData form = ctx.getForm();
        String title = form.getString("title");
        String description = form.getString("description");
        try {
            CoreSession session = ctx.getCoreSession();
            section.setTitle(title, session);
            section.setDescription(description, session);
            session.saveDocument(doc);
        } catch (ClientException e) {
            throw WebException.wrap(
                    FAILED_TO_POST_SECTION + doc.getPathAsString(), e);
        }

        return redirect(prev.getPath() + "#section_" + sectionIndex);
    }

    @Path("r/{index}")
    public Object getRow(@PathParam("index") int rowIndex) {
        HtmlRow row = section.row(rowIndex, ctx.getCoreSession());
        return newObject("HtmlRow", doc, row);
    }

    private void saveDocument() throws ClientException {
        CoreSession session = ctx.getCoreSession();
        session.saveDocument(doc);
        session.save();
    }
}
