package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.Sorter;
import org.nuxeo.ecm.core.rest.DocumentHelper;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.blocs.ExternalURL;
import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.SiteManagerException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.sort.ExternalURLSorter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteThemeManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.AbstractDocumentTree;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site.AdminSiteTree;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site.AdminSiteTreeAsset;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site.SiteDocumentTree;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

@WebObject(type = "LabsSite", superType = "LabsPage")
@Produces("text/html; charset=UTF-8")
public class Site extends PageResource {

    private LabsSite site;

    @Override
    public Object doGet() {
        try {
            return redirect(getPath()
                    + '/'
                    + URIUtils.quoteURIPathComponent(
                            (new org.nuxeo.common.utils.Path(
                                    site.getIndexDocument().getAdapter(
                                            SiteDocument.class).getResourcePath()).removeFirstSegments(1)).toString(),
                            false));
        } catch (ClientException e) {
            return super.doGet();
        }
    }

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        site = doc.getAdapter(LabsSite.class);

        ctx.getEngine().getRendering().setSharedVariable("site", site);
        ctx.setProperty("site", site);
    }

    @POST
    @Path("@addContent")
    @Override
    public Response addContent() {
        try {
            DocumentModel tree = site.getTree();

            String name = ctx.getForm().getString("name");
            DocumentModel newDoc = DocumentHelper.createDocument(ctx, tree,
                    name);
            String pathSegment = URIUtils.quoteURIPathComponent(
                    newDoc.getName(), true);
            return redirect(getPath() + '/' + pathSegment);
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }

    }

    @POST
    @Override
    public Response doPost() {
        FormData form = ctx.getForm();

        if ("edit".equals(form.getString("action"))) {
            return updateSite(form);
        }

        try {
            DocumentModel tree = site.getTree();

            String name = ctx.getForm().getString("name");
            DocumentModel newDoc = DocumentHelper.createDocument(ctx, tree,
                    name);
            String pathSegment = URIUtils.quoteURIPathComponent(
                    newDoc.getName(), true);
            return redirect(getPath() + '/' + pathSegment);
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }

    private Response updateSite(FormData form) {
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

    @Path("@theme/{themeName}")
    public Object doGetTheme(@PathParam("themeName") String themeName) {
        try {
            SiteThemeManager tm = site.getThemeManager();
            SiteTheme theme = tm.getTheme(themeName);
            if (theme == null) {
                // This creates the default theme if not found
                theme = tm.getTheme();
            }

            return newObject(Docs.SITETHEME.type(), site, theme);
        } catch (ClientException e) {
            throw new WebResourceNotFoundException("Theme not found", e);
        }
    }

    private SiteManager getSiteManager() {
        try {
            return Framework.getService(SiteManager.class);
        } catch (Exception e) {
            return null;
        }

    }

    @GET
    @Path("@treeview")
    @Produces(MediaType.APPLICATION_JSON)
    public Response doTreeview(@QueryParam("root") String root,
            @QueryParam("view") String view, @QueryParam("id") String id)
            throws ClientException {

        LabsSite site = (LabsSite) ctx.getProperty("site");

        if (site != null) {
            DocumentModel tree = null;
            AbstractDocumentTree siteTree;
            if ("admin".equals(view)) {
                tree = site.getTree();
                siteTree = new AdminSiteTree(ctx, tree);
            } else if ("admin_asset".equals(view)) {
                tree = "0".equals(id) ? site.getAssetsDoc()
                        : getCoreSession().getDocument(new IdRef(id));
                siteTree = new AdminSiteTreeAsset(ctx, tree);
            } else {
                tree = site.getTree();
                siteTree = new SiteDocumentTree(ctx, tree);

            }
            String result = "";
            if (root == null || "source".equals(root)) {
                if (id != null && !"0".equals(id)) {
                    DocumentModel document = tree.getCoreSession().getDocument(
                            new IdRef(id));
                    String entryPoint = StringUtils.substringAfter(
                            document.getPathAsString(),
                            site.getDocument().getPathAsString() + "/"
                                    + Docs.TREE.docName());
                    result = siteTree.enter(ctx, entryPoint);
                } else {
                    siteTree.enter(ctx, "");
                    result = siteTree.getTreeAsJSONArray(ctx);
                }
            } else {
                result = siteTree.enter(ctx, root);
            }
            return Response.ok().entity(result).type(MediaType.APPLICATION_JSON).build();
        }
        return null;
    }

    @Override
    public DocumentObject newDocument(String path) {
        try {

            PathRef pathRef = new PathRef(site.getTree().getPathAsString()
                    + "/" + path);
            DocumentModel doc = ctx.getCoreSession().getDocument(pathRef);
            return (DocumentObject) ctx.newObject(doc.getType(), doc);
        } catch (Exception e) {
            throw new WebResourceNotFoundException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A> A getAdapter(Class<A> adapter) {
        if (adapter.equals(Page.class)) {
            try {
                return (A) site.getIndexDocument().getAdapter(Page.class);
            } catch (ClientException e) {

            }
        }
        return super.getAdapter(adapter);
    }

    public List<Page> getDeletedPage() throws ClientException {
        return doc.getAdapter(LabsSite.class).getAllDeletedPages();
    }

    // /////////////// ALL CODE BELOW IS TO BE REFACTORED /////////////////

    public List<LabsNews> getNews(String pRef) throws ClientException {
        CoreSession session = getCoreSession();
        DocumentModel pageNews = session.getDocument(new IdRef(pRef));
        return pageNews.getAdapter(PageNews.class).getAllNews();

    }

    public ArrayList<ExternalURL> getExternalURLs() throws ClientException {
        ArrayList<ExternalURL> listExtURL = new ArrayList<ExternalURL>();
        DocumentModelList listDoc = null;
        Sorter extURLSorter = new ExternalURLSorter();
        listDoc = getCoreSession().getChildren(doc.getRef(),
                LabsSiteConstants.Docs.EXTERNAL_URL.type(), null, null,
                extURLSorter);
        for (DocumentModel doc : listDoc) {
            ExternalURL extURL = doc.getAdapter(ExternalURL.class);
            listExtURL.add(extURL);
        }
        return listExtURL;
    }

    @POST
    @Path(value = "persistExternalURL")
    public Object addExternalURL(@FormParam("extUrlName") String pName,
            @FormParam("extURLURL") String pURL,
            @FormParam("extURLOrder") int pOrder) {
        CoreSession session = ctx.getCoreSession();
        try {
            DocumentModel docExtURL = session.createDocumentModel(
                    doc.getPathAsString(), pName,
                    LabsSiteConstants.Docs.EXTERNAL_URL.type());
            ExternalURL extURL = docExtURL.getAdapter(ExternalURL.class);
            extURL.setName(pName);
            extURL.setURL(pURL);
            extURL.setOrder(pOrder);
            session.createDocument(docExtURL);
            session.save();
            return Response.status(Status.OK).build();
        } catch (ClientException e) {
            return Response.status(Status.GONE).build();
        }
    }

    @POST
    @Path(value = "persistExternalURL/{idExt}")
    public Object modifyExternalURL(@FormParam("extUrlName") String pName,
            @FormParam("extURLURL") String pURL,
            @FormParam("extURLOrder") int pOrder,
            @PathParam("idExt") final String pId) {
        CoreSession session = ctx.getCoreSession();

        try {
            DocumentModel docExtURL = session.getDocument(new IdRef(pId));
            ExternalURL extURL = docExtURL.getAdapter(ExternalURL.class);
            extURL.setName(pName);
            extURL.setURL(pURL);
            extURL.setOrder(pOrder);
            session.saveDocument(docExtURL);
            session.save();
            return Response.status(Status.OK).build();
        } catch (ClientException e) {
            return Response.status(Status.GONE).build();
        }
    }

    @DELETE
    @Path("deleteExternalURL/{idExt}")
    public Object doDeleteNews(@PathParam("idExt") final String pId)
            throws ClientException {
        CoreSession session = ctx.getCoreSession();
        DocumentModel document = session.getDocument(new IdRef(pId));
        try {
            session.removeDocument(document.getRef());
            session.save();
        } catch (ClientException e) {
            return Response.status(Status.GONE).build();
        }
        return Response.noContent().build();
    }

}
