package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlSection;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.MovableElement;

@WebObject(type = "HtmlSection")
public class WebHtmlSection extends MovableElementResource {

    private static final String FAILED_TO_POST_SECTION = "Failed to post html section ";
    private static final String FAILED_TO_DELETE_SECTION = "Failed to delete html section ";
    private HtmlSection section;

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        assert args != null && args.length >= 3;
        section = (HtmlSection) args[1];
        index = (Integer) args[2];
        element = (MovableElement) args[3];
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
            String userClassStr = form.getString("userClass");
            String strRowNumber = form.getString("rowNumber");
            List<String> userClass = null;
            if(StringUtils.isNotEmpty(userClassStr)){
                String[] split = userClassStr.split(",");
                if (split.length ==1 && StringUtils.isEmpty(split[0])){
                    split = new String[0];
                }
                userClass = new ArrayList<String>(Arrays.asList(split));
            }
            int  rowNumber = 1;
            HtmlRow row = null;
            if (StringUtils.isNumeric(strRowNumber)){
                rowNumber = (new Integer(strRowNumber)).intValue();
                for (int i=0;i<rowNumber;i++){
                    row = section.addRow(cssClass, userClass);
                    row.initTemplate(form.getString("rowTemplate"));
                }
            }
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
