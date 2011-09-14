package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.model.Template;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;

public class Page extends DocumentObject {

    private static final String BROWSE_TREE_VIEW = "views/common/browse_tree.ftl";

    private static final Log LOG = LogFactory.getLog(Page.class);

    public com.leroymerlin.corp.fr.nuxeo.labs.site.Page getPage() {
        return doc.getAdapter(com.leroymerlin.corp.fr.nuxeo.labs.site.Page.class);
    }

    @POST
    @Path("updateCommentaire")
    public Object doSetCommentaire(
            @FormParam("content") final String commentaire) {
        try {
            doc.getAdapter(com.leroymerlin.corp.fr.nuxeo.labs.site.Page.class).setCommentaire(
                    commentaire);
            getCoreSession().saveDocument(doc);
            getCoreSession().save();
        } catch (PropertyException pe) {
            LOG.error("Unable to get property " + pe);
        } catch (ClientException ce) {
            LOG.error("Unable to get description " + ce);
        }

        return Response.ok().build();
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
        return (String) sd.getSite().getDocument().getPropertyValue(
                "webc:url");
    }
}
