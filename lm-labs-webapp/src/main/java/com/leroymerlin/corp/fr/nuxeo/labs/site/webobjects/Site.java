package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.PathRef;
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
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteThemeManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.AbstractDocumentTree;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site.AdminSiteTree;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site.AdminSiteTreeAsset;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site.SiteDocumentTree;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

@WebObject(type = "LabsSite", superType = "LabsPage")
@Produces("text/html; charset=UTF-8")
public class Site extends NotifiablePageResource {

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

        ctx.setProperty("site", site);
    }

    @POST
    @Path("@addContent")
    @Override
    public Response doAddContent() {
        String name = ctx.getForm().getString("name");
        boolean overwrite = BooleanUtils.toBoolean(ctx.getForm().getString(
                "overwritePage"));
        return addContent(name, PageCreationLocation.TOP, overwrite);
    }

    @Override
    public Response getPut() {
        FormData form = ctx.getForm();
        return updateSite(form);
    }

    private Response updateSite(FormData form) {
        String title = form.getString("dc:title");
        String url = form.getString("webc:url");
        String description = form.getString("dc:description");
        String piwikId = form.getString("piwik:piwikId");
        String siteTemplateStr = form.getString("labssite:siteTemplate");
        boolean modified = false;
        try {
            if (!StringUtils.isEmpty(title)) {
                site.setTitle(title);
                modified = true;
            }
            if (!StringUtils.isEmpty(description)) {
                site.setDescription(description);
                modified = true;
            }
            String oldUrl = site.getURL();
            url = StringUtils.trim(url);
            if (!StringUtils.isEmpty(url) && !url.equals(oldUrl)) {
                site.setURL(url);
                modified = true;
            }
            String oldPiwikId = site.getPiwikId();
            piwikId = StringUtils.trim(piwikId);
            if (!StringUtils.equals(piwikId, oldPiwikId)) {
                site.setPiwikId(piwikId);
                modified = true;
            }
            boolean isSiteTemplate = BooleanUtils.toBoolean(siteTemplateStr);
            if (site.isSiteTemplate() != isSiteTemplate) {
                site.setSiteTemplate(isSiteTemplate);
                modified = true;
            }
            if (isSiteTemplate) {
                if (form.isMultipartContent()) {
                    Blob preview = form.getBlob("labssite:siteTemplatePreview");
                    if (preview != null
                            && !StringUtils.isEmpty(preview.getFilename())) {
                        site.setSiteTemplatePreview(preview);
                        modified = true;
                    }
                }
            } else {
                Blob siteTemplatePreview = null;
                try {
                    siteTemplatePreview = site.getSiteTemplatePreview();
                } catch (ClientException e) {
                    throw WebException.wrap(e);
                }
                if (siteTemplatePreview != null) {
                    site.setSiteTemplatePreview(null);
                    modified = true;
                }
            }
            String msgLabel = "label.labssites.edit.noop";
            if (modified) {
                CoreSession session = ctx.getCoreSession();
                getSiteManager().updateSite(session, site);
                session.save();
                msgLabel = "label.labssites.edit.site.updated";
            }
            return redirect(ctx.getModulePath() + "/" + site.getURL()
                    + "/@views/edit?message_success=" + msgLabel);
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

    public List<DocumentModel> getDeletedDocs() throws ClientException {
        return doc.getAdapter(LabsSite.class).getAllDeletedDocs();
    }

    @Path("@externalURL/{idExt}")
    public Object doExternalUrl(@PathParam("idExt") final String pId) {
        try {
            DocumentModel docExtURL = getCoreSession().getDocument(
                    new IdRef(pId));
            return newObject("ExternalUrl", docExtURL);
        } catch (ClientException e) {
            throw new WebResourceNotFoundException("External URL not found", e);
        }

    }

    @POST
    @Path(value = "@externalURL")
    public Object addExternalURL() {
        String pName = ctx.getForm().getString("exturl:name");
        String pURL = ctx.getForm().getString("exturl:url");
        CoreSession session = ctx.getCoreSession();
        try {
            ExternalURL extURL = site.createExternalURL(pName);
            extURL.setURL(pURL);
            session.saveDocument(extURL.getDocument());
            session.save();
            return Response.status(Status.OK).build();
        } catch (ClientException e) {
            return Response.status(Status.GONE).build();
        }
    }

    // /////////////// ALL CODE BELOW IS TO BE REFACTORED /////////////////

    public List<LabsNews> getNews(String pRef) throws ClientException {
        CoreSession session = getCoreSession();
        DocumentModel pageNews = session.getDocument(new IdRef(pRef));
        return pageNews.getAdapter(PageNews.class).getAllNews();
    }

}
