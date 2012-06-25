package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlSection;

@WebObject(type = "HtmlSection")
public class WebHtmlSection extends MovableElementResource {

    private static final String FAILED_TO_POST_SECTION = "Failed to post html section ";
    private static final String FAILED_TO_DELETE_SECTION = "Failed to delete html section ";
    private HtmlSection section;
    private HtmlPage htmlPage;

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        assert args != null && args.length >= 3;
        section = (HtmlSection) args[1];
        index = (Integer) args[2];
        htmlPage = (HtmlPage) args[3];
        element = htmlPage;
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
        try {
            String cssClass = form.getString("cssClass");
            HtmlRow row = section.addRow(cssClass);
            String rowTemplate = form.getString("rowTemplate");
            row.initTemplate(rowTemplate);

            saveDocument();
        } catch (ClientException e) {
            throw WebException.wrap(
                    FAILED_TO_POST_SECTION + doc.getPathAsString(), e);
        }
        return redirect(prev.getPath() + "#row_s" + index + "_r" + (section.getRows().size() - 1));
    }

    @Override
    public Response doPut() {
        FormData form = ctx.getForm();
        String title = form.getString("title");
        String description = form.getString("description");
        try {
            CoreSession session = ctx.getCoreSession();
            section.setTitle(title);
            section.setDescription(description);
            session.saveDocument(doc);
        } catch (ClientException e) {
            throw WebException.wrap(
                    FAILED_TO_POST_SECTION + doc.getPathAsString(), e);
        }

        return redirect(prev.getPath() + "#section_" + index);
    }

    @Path("r/{index}")
    public Object getRow(@PathParam("index") int rowIndex) {
        HtmlRow row = section.row(rowIndex);
        row.setSession(getCoreSession());
        return newObject("HtmlRow", doc, row, section, rowIndex);
    }

    private void saveDocument() throws ClientException {
        CoreSession session = ctx.getCoreSession();
        session.saveDocument(doc);
        session.save();
    }
}
