package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.Sorter;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.platform.rendering.api.RenderingEngine;
import org.nuxeo.ecm.platform.rendering.fm.FreemarkerEngine;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.common.freemarker.BytesFormatTemplateMethod;
import com.leroymerlin.common.freemarker.DateInWordsMethod;
import com.leroymerlin.common.freemarker.UserFullNameTemplateMethod;
import com.leroymerlin.corp.fr.nuxeo.freemarker.BreadcrumbsArrayTemplateMethod;
import com.leroymerlin.corp.fr.nuxeo.freemarker.LatestUploadsPageProviderTemplateMethod;
import com.leroymerlin.corp.fr.nuxeo.freemarker.PageEndUrlTemplateMethod;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManagerException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSiteAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;

@WebObject(type = "sitesRoot")
@Produces("text/html; charset=UTF-8")
@Path("/labssites")
public class SitesRoot extends ModuleRoot {

    private static final Log log = LogFactory.getLog(SitesRoot.class);

    private static final String DEFAULT_VIEW = "index";

    private static final String HOMEPAGE_VIEW = "homePage";

    private static final String EDIT_VIEW = "views/sitesRoot/editLabsSite.ftl";

    private LabsSite currentLabsSite = null;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.nuxeo.ecm.webengine.model.impl.AbstractResource#initialize(java.lang
     * .Object[])
     */
    @Override
    protected void initialize(Object... args) {
        super.initialize(args);
        // Add global fm variables
        RenderingEngine rendering = getContext().getEngine()
                .getRendering();
        if (rendering instanceof FreemarkerEngine) {
            FreemarkerEngine fm = (FreemarkerEngine) rendering;
            fm.setSharedVariable("bytesFormat", new BytesFormatTemplateMethod());
            fm.setSharedVariable("breadcrumbsDocs",
                    new BreadcrumbsArrayTemplateMethod());
            fm.setSharedVariable("pageEndUrl", new PageEndUrlTemplateMethod());
            fm.setSharedVariable("latestUploadsPageProvider",
                    new LatestUploadsPageProviderTemplateMethod());
            fm.setSharedVariable("userFullName", new UserFullNameTemplateMethod());
            fm.setSharedVariable("dateInWords", new DateInWordsMethod());
        }
    }

    @GET
    public Object doGetDefaultView() {
        String home = request.getParameter("homepage");
        if (!StringUtils.isEmpty(home)) {
            if (home.equals("create")) {
                return getTemplate(EDIT_VIEW);
            } else if (home.equals("display")) {
                return getView(DEFAULT_VIEW);
            } else if (home.equals("load")) {

            }
            return getView(HOMEPAGE_VIEW);
        }
        return getView(HOMEPAGE_VIEW);
    }

    @Path("{url}")
    public Object doGetSite(@PathParam("url") final String pURL) {
        CoreSession session = getContext().getCoreSession();
        try {
            DocumentModelList listDoc = session.query("SELECT * FROM Document where webc:url = '"
                    + pURL + "' ");
            if (listDoc != null && !listDoc.isEmpty()) {
                DocumentModel document = listDoc.get(0);
                return newObject("LabsSite", document);

            } else {
                return Response.ok()
                        .status(404)
                        .build();
            }

        } catch (ClientException e) {
            return Response.ok()
                    .status(404)
                    .build();
        }

    }

    public String escapeJS(String pString) {
        if (StringUtils.isEmpty(pString)) {
            return "";
        }
        return StringEscapeUtils.escapeJavaScript(pString);
    }

    public boolean isAuthorized() {
        return true;
    }

    public String getPathForEdit() {
        return getPath();
    }

    public LabsSite getLabsSite() {
        return currentLabsSite;
    }

    public DocumentModel getDoc() throws ClientException {
        return LabsSiteAdapter.getDefaultRoot(ctx.getCoreSession());
    }

    public List<LabsSite> getLabsSites() throws ClientException {
        return getSiteManager().getAllSites(ctx.getCoreSession());
    }

    @POST
    @Path(value = "persistLabsSite")
    public Response doPost(@FormParam("labsSiteTitle") String pTitle,
            @FormParam("labsSiteURL") String pURL,
            @FormParam("labsSiteDescription") String pDescription,
            @FormParam("labssiteId") String pId) {
        CoreSession session = ctx.getCoreSession();
        boolean isNew = isNew(pId);
        try {
            if (isNew) {
                if (!existURL(pURL, session)) {
                    SiteManager sm = getSiteManager();
                    LabsSite labSite;
                    try {
                        labSite = sm.createSite(session, pTitle, pURL);
                    } catch (SiteManagerException e) {
                        throw new ClientException(e.getMessage());
                    }

                    labSite.setTitle(pTitle);
                    labSite.setDescription(pDescription);
                    labSite.setURL(pURL);
                    session.saveDocument(labSite.getDocument());
                    session.save();
                    return Response.status(Status.OK)
                            .entity(pTitle + " created.")
                            .build();
                }
            } else {
                //Should be managed on Site WebObject

            }
            return Response.status(Status.NOT_MODIFIED)
                    .build();
        } catch (ClientException e) {
            log.debug(e, e);
            return Response.status(Status.GONE)
                    .build();
        }

    }

    private SiteManager getSiteManager() {
        try {
            return Framework.getService(SiteManager.class);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean existURL(String pURL, CoreSession pSession)
            throws ClientException {
        DocumentModelList listDoc = pSession.query("SELECT * FROM Document where webc:url = '"
                + pURL + "' ");
        if (listDoc != null && !listDoc.isEmpty()) {
            return true;
        }
        return false;
    }

    @PUT
    @Path("edit/{idLabsSite}")
    public Object editLabsSite(@PathParam("idLabsSite") final String pIdLabsSite) {
        try {
            DocumentModel document = ctx.getCoreSession()
                    .getDocument(new IdRef(pIdLabsSite));
            currentLabsSite = document.getAdapter(LabsSite.class);
        } catch (ClientException e) {
            log.error(e, e);
        }
        Template template = getTemplate(EDIT_VIEW);
        return template;
    }



    /**
     * @param pId
     * @return
     */
    private boolean isNew(String pId) {
        boolean isNew = false;
        if (StringUtils.isEmpty(pId)) {
            isNew = true;
        } else {
            if ("-1".equals(pId)) {
                isNew = true;
            } else {
                isNew = false;
            }
        }
        return isNew;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.nuxeo.ecm.webengine.model.impl.ModuleRoot#handleError(javax.ws.rs
     * .WebApplicationException)
     */
    @Override
    public Object handleError(WebApplicationException e) {
        if (e instanceof WebResourceNotFoundException) {
            String fileName = "error/error_404.ftl";
            log.debug(fileName);
            return Response.status(404)
                    .entity(getTemplate(fileName))
                    .build();
        } else {
            log.info("No error handling for class " + e.getClass()
                    .getName());
            log.error(e.getMessage(), e);
            return super.handleError(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.nuxeo.ecm.webengine.model.impl.ModuleRoot#getLink(org.nuxeo.ecm.core
     * .api.DocumentModel)
     */
    @Override
    public String getLink(DocumentModel doc) {
        try {
            SiteDocument sd = doc.getAdapter(SiteDocument.class);
            return new StringBuilder().append(getPath())
                    .append("/")
                    .append(sd.getSite().getDocument()
                            .getAdapter(LabsSite.class)
                            .getURL())
                    .append(LabsSiteWebAppUtils.buildEndUrl(doc))
                    .toString();
        } catch (UnsupportedOperationException e) {
            throw WebException.wrap(e);
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }
}
