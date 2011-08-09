package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;

@WebObject(type = "LabsSite")
@Produces("text/html; charset=UTF-8")
public class Site extends DocumentObject {

    public static final String SITEMAP_VIEW = "sitemap";

    @Path("id/{idPage}")
    public Object doGetPageId(@PathParam("idPage") final String idPage) {
        DocumentRef docRef = new IdRef(idPage);
        try {
            DocumentModel destDoc = ctx.getCoreSession().getDocument(docRef);
            String type = null;
            if (LabsSiteConstants.Docs.fromString(doc.getType()) != null) {
                return newObject(type, destDoc);
            } else {
                throw new WebException("Unsupported document type "
                        + doc.getType());
            }
        } catch (ClientException e) {
            throw WebException.wrap("The document id='" + idPage
                    + "' not exists", e);
        }
    }

    @GET
    @Path("siteMap")
    public Template doGoSiteMap() {
        try {
            return getView(SITEMAP_VIEW).arg(
                    "treeviewRootParent",
                    getCoreSession().getDocument(
                            new PathRef(this.doc.getPathAsString() + "/"
                                    + LabsSiteConstants.Docs.TREE.docName())));
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }

    @POST
    @Path("treeviewChildren/{treenodeId}")
    public String doTreeviewChildren(
            @PathParam("treenodeId") final String treenodeId) {
        if (StringUtils.isBlank(treenodeId)) {
            return null;
        }

        return "[{\"text\": \"1. Review of existing structures\",\"expanded\": true,\"children\": [{\"text\": \"1.1 jQuery core\"}, {\"text\": \"1.2 metaplugins\"}]},"
                + "{\"text\": \"<a href='www.google.fr'>Google de ouf</a>\"},"
                + "{\"text\": \"3. Summary\"},"
                + "{\"text\": \"4. Questions and answers\"}]";
    }

    @Override
    public DocumentObject newDocument(String path) {
        try {
            PathRef pathRef = new PathRef(LabsSiteUtils.getSiteTreePath(doc) + "/" + path);
            DocumentModel doc = ctx.getCoreSession().getDocument(pathRef);
            if (LabsSiteConstants.Docs.fromString(doc.getType()) == null) {
                throw new WebException("Unsupported document type "
                        + doc.getType());
            }
            return (DocumentObject) ctx.newObject(doc.getType(), doc);
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.nuxeo.ecm.core.rest.DocumentObject#doGet()
     */
    @Override
    public Object doGet() {
        return redirect(getPath() + "/"
                + LabsSiteConstants.Docs.WELCOME.docName());
    }

}
