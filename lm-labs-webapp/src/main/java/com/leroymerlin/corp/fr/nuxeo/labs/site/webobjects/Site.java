package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.Sorter;
import org.nuxeo.ecm.core.rest.DocumentHelper;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.core.schema.FacetNames;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManagerException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteTheme;
import com.leroymerlin.corp.fr.nuxeo.labs.site.blocs.ExternalURL;
import com.leroymerlin.corp.fr.nuxeo.labs.site.filter.PageNewsFilter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.SiteThemeManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.sort.ExternalURLSorter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.sort.PageNewsSorter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;

@WebObject(type = "LabsSite", superType = "LabsPage")
@Produces("text/html; charset=UTF-8")
public class Site extends PageResource {

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
        ctx.setProperty("site", site);
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

            String name = ctx.getForm()
                    .getString("name");
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

    @Path("theme/{themeName}")
    public Object doGetTheme(@PathParam("themeName") String themeName) {
        try {
            SiteThemeManager tm = site.getSiteThemeManager();
            SiteTheme theme = tm.getTheme(themeName);
            if (theme == null) {
                // This creates the default theme if not found
                theme = tm.getTheme();
            }

            return newObject("SiteTheme", site, theme);
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

    @POST
    @Path("treeview")
    public String doTreeview() {
        try {

            return LabsSiteWebAppUtils.getTreeview(site.getTree(), this, true,
                    new Filter() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public boolean accept(DocumentModel document) {
                            return Docs.pageDocs()
                                    .contains(
                                            Docs.fromString(document.getType()));
                        }
                    });
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
            return LabsSiteWebAppUtils.getTreeview(assetDoc, this, true,
                    new Filter() {
                        @Override
                        public boolean accept(DocumentModel document) {
                            return document.getFacets()
                                    .contains(FacetNames.FOLDERISH);
                        }
                    });
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
            throw new WebResourceNotFoundException(e.getMessage(), e);
        }
    }

    // /////////////// ALL CODE BELOW IS TO BE REFACTORED /////////////////

    public ArrayList<com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews> getNews(
            String pRef) throws ClientException {
        ArrayList<com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews> listNews = new ArrayList<com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews>();
        CoreSession session = getCoreSession();
        DocumentModelList listDoc = session.getChildren(new IdRef(pRef),
                LabsSiteConstants.Docs.LABSNEWS.type(), null,
                new PageNewsFilter(Calendar.getInstance()),
                new PageNewsSorter());
        for (DocumentModel doc : listDoc) {
            com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews news = doc.getAdapter(com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews.class);
            listNews.add(news);
        }
        return listNews;
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
            return Response.status(Status.OK)
                    .build();
        } catch (ClientException e) {
            return Response.status(Status.GONE)
                    .build();
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
            return Response.status(Status.OK)
                    .build();
        } catch (ClientException e) {
            return Response.status(Status.GONE)
                    .build();
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
            return Response.status(Status.GONE)
                    .build();
        }
        return Response.noContent()
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A> A getAdapter(Class<A> adapter) {
        if (adapter.equals(Page.class)) {
            try {
                return (A) site.getIndexDocument()
                        .getAdapter(Page.class);
            } catch (ClientException e) {

            }
        }
        return super.getAdapter(adapter);
    }

}
