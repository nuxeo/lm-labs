package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;

@WebObject(type = "LabsSite")
@Produces("text/html; charset=UTF-8")
public class Site extends DocumentObject {

    public static final String SITEMAP_VIEW = "sitemap";

    public static final String SITEMAP_AS_LIST_VIEW = "sitemap_as_list";

    private static final Log LOG = LogFactory.getLog(Site.class);

    @Path("id/{idPage}")
    public Object doGetPageId(@PathParam("idPage") final String idPage) {
        DocumentRef docRef = new IdRef(idPage);
        try {
            DocumentModel destDoc = ctx.getCoreSession().getDocument(docRef);
            if (Docs.fromString(destDoc.getType()) != null) {
                return newObject(destDoc.getType(), destDoc);
            } else {
                throw new WebException("Unsupported document type "
                        + destDoc.getType());
            }
        } catch (ClientException e) {
            throw WebException.wrap("The document id='" + idPage
                    + "' not exists", e);
        }
    }

    @GET
    @Path("siteMap")
    public Template doGoSiteMap() {
        return getView(SITEMAP_VIEW);
    }

    @GET
    @Path("siteMapAsList")
    public Template doGoSiteMapAsList() {
        try {
            return getView(SITEMAP_AS_LIST_VIEW).arg(
                    "allDoc",
                    LabsSiteUtils.getAllDoc(LabsSiteUtils.getSiteTree(doc)));
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
    }

    @POST
    @Path("treeview")
    public String doTreeview() {
        try {
            return LabsSiteWebAppUtils.getTreeview(LabsSiteUtils.getSiteTree(doc), this);
        } catch (Exception e) {
            LOG.error(e, e);
            throw WebException.wrap(e);
        }
    }
    
    @POST
    @Path("browseTree")
    public String doBrowseTree() {
        try {
            return LabsSiteWebAppUtils.getTreeview(LabsSiteUtils.getSiteTree(doc), this, false);
        } catch (Exception e) {
            LOG.error(e, e);
            throw WebException.wrap(e);
        }
    }
    

    public String getCreatorUsername(final String docRef) {
        try {
            return LabsSiteUtils.getCreatorUsername(getCoreSession().getDocument(
                    new IdRef(docRef)));
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }

    public String getLastModifierUsername(final String docRef) {
        try {
            return LabsSiteUtils.getLastModifierUsername(getCoreSession().getDocument(
                    new IdRef(docRef)));
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }

    public String getCreated(final String docRef) {
        try {
            return LabsSiteUtils.getCreated(getCoreSession().getDocument(
                    new IdRef(docRef)));
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }

    public String getLastModified(final String docRef) {
        try {
            return LabsSiteUtils.getLastModified(getCoreSession().getDocument(
                    new IdRef(docRef)));
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }

    @Override
    public DocumentObject newDocument(String path) {
        try {
            PathRef pathRef = new PathRef(LabsSiteUtils.getSiteTreePath(doc)
                    + "/" + path);
            DocumentModel doc = ctx.getCoreSession().getDocument(pathRef);
            if (Docs.fromString(doc.getType()) == null) {
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
        return redirect(getPath() + "/" + Docs.WELCOME.docName());
    }

    public BlobHolder getBlobHolder(final DocumentModel document) {
        return document.getAdapter(BlobHolder.class);
    }
    
    public DocumentModel getClosestPage(DocumentModel document) {
        return LabsSiteUtils.getClosestPage(document);
    }
    
    public String getPageEndUrl(DocumentModel document) throws ClientException {
        return LabsSiteWebAppUtils.buildEndUrl(document);
    }
    
}
