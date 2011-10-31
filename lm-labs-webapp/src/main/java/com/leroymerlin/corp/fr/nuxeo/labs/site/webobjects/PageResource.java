package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;

@WebObject(type = "LabsPage")
public class PageResource extends DocumentObject {
    public static final String COPYOF_PREFIX = "Copie de ";

    private static final String BROWSE_TREE_VIEW = "views/common/browse_tree.ftl";

    private static final Log LOG = LogFactory.getLog(PageResource.class);

    private static final String[] MESSAGES_TYPE = new String[] { "error",
            "info", "success", "warning" };


    @POST
    @Path("updateCommentaire")
    public Object doSetCommentaire(
            @FormParam("content") final String commentaire) {
        try {
            Page page = getAdapter(Page.class);
            page.setCommentaire(
                    commentaire);
            getCoreSession().saveDocument(page.getDocument());
            getCoreSession().save();
        } catch (PropertyException pe) {
            LOG.error("Unable to get property " + pe);
        } catch (ClientException ce) {
            LOG.error("Unable to get description " + ce);
        }

        return Response.ok()
                .build();
    }

    public Page getPage() {
        return doc.getAdapter(Page.class);
    }

    @Override
    public <A> A getAdapter(Class<A> adapter) {
        return doc.getAdapter(adapter) != null ? doc.getAdapter(adapter)
                : super.getAdapter(adapter);
    }

    public String escapeJS(String pString) {
        if (StringUtils.isEmpty(pString)) {
            return "";
        }
        return StringEscapeUtils.escapeJavaScript(pString);
    }

    @GET
    @Path("displayBrowseTree")
    public Object doBrowseTree() {
        Template template = getTemplate(BROWSE_TREE_VIEW);
        return template;
    }

    @POST
    @Path("@move")
    public Response doMove(@FormParam("destination") String destinationId, @FormParam("view") String view, @FormParam("redirect") String redirect) throws ClientException {
        DocumentModel destination = doc.getCoreSession().getDocument(new IdRef(destinationId));
        String viewUrl = "";
        if (!StringUtils.isEmpty(view)) {
            viewUrl = "/@views/" + view;
        }
        if (!destination.isFolder()) {
            return redirect(getPath() + viewUrl + "?message_error=label.admin.page.move.destinationNotFolder");
        }
        try {
            doc.getCoreSession().move(doc.getRef(), destination.getRef(), null);
            doc.getCoreSession().save();
        } catch (Exception e) {
            return redirect(getPath() + viewUrl + "?message_error=" + e.getMessage());
        }
        return redirect(getPath() + viewUrl + "?message_success=label.admin.page.moved");
    }

    @POST
    @Path("@copy")
    public Response doCopy(@FormParam("destination") String destinationId, @FormParam("view") String view, @FormParam("redirect") String redirect) throws ClientException {
        DocumentModel destination = doc.getCoreSession().getDocument(new IdRef(destinationId));
        String viewUrl = "";
        if (!StringUtils.isEmpty(view)) {
            viewUrl = "/@views/" + view;
        }
        if (!destination.isFolder()) {
            return redirect(getPath() + viewUrl + "?message_error=label.admin.page.copy.destinationNotFolder");
        }
        try {
            DocumentModel copy = doc.getCoreSession().copy(doc.getRef(), destination.getRef(), null);
            Page page = copy.getAdapter(Page.class);
            page.setTitle(COPYOF_PREFIX + page.getTitle());
            doc.getCoreSession().saveDocument(page.getDocument());
            doc.getCoreSession().save();
        } catch (Exception e) {
            return redirect(getPath() + viewUrl + "?message_error=" + e.getMessage());
        }
        return redirect(getPath() + viewUrl + "?message_success=label.admin.page.copied");
    }

    /**
     * Returns a Map containing all "flash" messages
     *
     * @return
     */
    public Map<String, String> getMessages() {
        Map<String, String> messages = new HashMap<String, String>();
        FormData form = ctx.getForm();
        for (String type : MESSAGES_TYPE) {
            String message = form.getString("message_" + type);
            if (StringUtils.isNotBlank(message)) {
                messages.put(type, message);
            }
        }
        return messages;

    }
}
