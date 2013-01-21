package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlContent;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlSection;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.GadgetUtils;

@WebObject(type = "HtmlRow")
public class WebHtmlRow extends MovableElementResource {

    //private static final Log LOG = LogFactory.getLog(WebHtmlRow.class);

    private static final String FAILED_TO_DELETE_ROW = "Failed to delete row ";
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
                    FAILED_TO_DELETE_ROW + doc.getPathAsString(), e);
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
                GadgetUtils.syncWidgetsConfig(content, widget, doc, getCoreSession());
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
}
