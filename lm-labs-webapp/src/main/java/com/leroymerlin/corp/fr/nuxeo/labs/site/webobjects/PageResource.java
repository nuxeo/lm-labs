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
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;

@WebObject(type = "LabsPage")
public class PageResource extends DocumentObject {

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

    public String getSiteUrlProp() throws PropertyException, ClientException {
        SiteDocument sd = doc.getAdapter(SiteDocument.class);
        return (String) sd.getSite()
                .getDocument()
                .getPropertyValue("webc:url");
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
