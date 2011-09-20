package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.core.schema.FacetNames;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManagerException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;

@WebObject(type = "LabsSite")
@Produces("text/html; charset=UTF-8")
public class Site extends Page {

    private static final Log LOG = LogFactory.getLog(Site.class);

    private interface Query {
        String TAG_VALUE = "%VALUE%";

        String SELECT_PICTURE = "select * from Picture where ecm:parentId = '"
                + TAG_VALUE + "'";
    }

    private LabsSite site;

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        site = doc.getAdapter(LabsSite.class);

        ctx.getEngine()
                .getRendering()
                .setSharedVariable("site", site);
    }

    @POST
    @Override
    public Response doPost() {
        FormData form = ctx.getForm();
        String title = form.getString("title");
        String url = form.getString("URL");
        String description = form.getString("description");

        try {
            site.setTitle(title);
            site.setDescription(description);
            site.setURL(url);

            CoreSession session = ctx.getCoreSession();
            getSiteManager().updateSite(session, site);
            session.save();
            return redirect(ctx.getModulePath()
                    + "/"
                    + site.getURL()
                    + "/@views/edit?message_success=label.site.edit.site_updated");
        } catch (SiteManagerException e) {
            return redirect(getPath() + "/@views/edit?message_error="
                    + e.getMessage());
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }

    }

    @Path("theme/{themeName}")
    public Object doGetTheme(@PathParam("themeName") String themeName) {
        try {
            SiteTheme theme = site.getSiteThemeManager().getTheme(themeName);
            return newObject("SiteTheme", site, theme);
        } catch (ClientException e) {
            throw new WebResourceNotFoundException("Theme not found",e);
        }
    }

    private SiteManager getSiteManager() {
        try {
            return Framework.getService(SiteManager.class);
        } catch (Exception e) {
            return null;
        }

    }

    @SuppressWarnings("serial")
    @POST
    @Path("treeview")
    public String doTreeview() {
        try {

            return LabsSiteWebAppUtils.getTreeview(site.getTree(), this, true,
                    new Filter() {
                        @Override
                        public boolean accept(DocumentModel document) {
                            return Docs.pageDocs().contains(Docs.fromString(document.getType()));
                        }});
        } catch (Exception e) {
            LOG.error(e, e);
            throw WebException.wrap(e);
        }
    }

    @SuppressWarnings("serial")
    @POST
    @Path("browseTree")
    public String doBrowseTree() {
        try {
            DocumentModel assetDoc = getCoreSession().getDocument(
                    new PathRef("/default-domain/sites/" + doc.getName() + "/"
                            + LabsSiteConstants.Docs.ASSETS.docName()));
            return LabsSiteWebAppUtils.getTreeview(assetDoc, this, true, new Filter() {
                @Override
                public boolean accept(DocumentModel document) {
                    return document.getFacets().contains(FacetNames.FOLDERISH);
                }});
        } catch (Exception e) {
            LOG.error(e, e);
            throw WebException.wrap(e);
        }
    }

    @POST
    @Path("getPictures")
    public Object doGetPictures(@FormParam("docId") final String docId) {
        String query = Query.SELECT_PICTURE.replace(Query.TAG_VALUE, docId);
        DocumentModelList pictures = null;
        StringBuilder result = null;

        try {
            pictures = getCoreSession().query(query);
        } catch (ClientException e) {
            LOG.error(e, e);
            throw WebException.wrap(e);
        }

        if (pictures != null) {
            result = new StringBuilder();
            for (DocumentModel doc : pictures) {
                StringBuilder imgSrc = new StringBuilder(
                        "/nuxeo/nxpicsfile/default/");
                imgSrc.append(doc.getRef()
                        .toString());
                imgSrc.append("/Thumbnail:content/");
                imgSrc.append(new Date().getTime());

                result.append("<div class='resourcesForCKEditor'><a id='");
                result.append(docId);
                result.append("' href='");
                result.append(imgSrc.toString()
                        .replace("/Thumbnail", "/Original"));
                result.append("' onclick='sendToCKEditor(this.href);return false;'><img src='");
                result.append(imgSrc);
                result.append("' /></a></div>");
            }
        } else {
            return Response.status(Status.NOT_FOUND)
                    .build();
        }

        return Response.status(Status.OK)
                .entity(result.toString())
                .build();
    }

    @Override
    public DocumentObject newDocument(String path) {
        try {

            PathRef pathRef = new PathRef(site.getTree()
                    .getPathAsString() + "/" + path);
            DocumentModel doc = ctx.getCoreSession()
                    .getDocument(pathRef);
            return (DocumentObject) ctx.newObject(doc.getType(), doc);
        } catch (Exception e) {
            throw new WebResourceNotFoundException(e.getMessage(),e);
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

    public DocumentModel getClosestPage(DocumentModel document)
            throws ClientException {
        return document.getAdapter(SiteDocument.class)
                .getPage()
                .getDocument();
    }

    public String getPageEndUrl(DocumentModel document) throws ClientException {
        return LabsSiteWebAppUtils.buildEndUrl(document);
    }

}
