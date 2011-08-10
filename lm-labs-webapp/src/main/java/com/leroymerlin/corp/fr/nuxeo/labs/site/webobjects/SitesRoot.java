package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.ArrayList;

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
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;

@WebObject(type = "sitesRoot")
@Produces("text/html; charset=UTF-8")
@Path("/labssites")
public class SitesRoot extends ModuleRoot {

    private static final Log log = LogFactory.getLog(SitesRoot.class);

    private static final String DEFAULT_VIEW = "index";

    private static final String HOMEPAGE_VIEW = "homePage";
    
    private static final String EDIT_VIEW = "views/sitesRoot/editLabsSite.ftl";
        
    private LabsSite currentLabsSite = null;

    @GET
    public Object doGetDefaultView() {
        String home = request.getParameter("homepage");
        if (!StringUtils.isEmpty(home)){
            if (home.equals("create")){
                return getTemplate(EDIT_VIEW);
            }
            else if(home.equals("display")){
                return getView(DEFAULT_VIEW);
            }
            else if(home.equals("load")){
                
            }
            return getView(HOMEPAGE_VIEW);
        }
        return getView(HOMEPAGE_VIEW);
    }

    @Path("{url}")
    public Object doGetSite(@PathParam("url") final String pURL)
            throws ClientException {
        CoreSession session = getContext().getCoreSession();
        
        DocumentModelList listDoc = session.query("SELECT * FROM Document where webc:url = '" + pURL +"' ");
        if (listDoc != null && !listDoc.isEmpty()){
            DocumentModel document = listDoc.get(0);
            if (session.exists(document.getRef())) {
                return newObject("LabsSite", document);
            } else {
                return Response.ok().status(404).build();
            }
        }
        return Response.ok().status(404).build();
    }
    
    public String escapeJS(String pString){
        if (StringUtils.isEmpty(pString)){
            return "";
        }
        return StringEscapeUtils.escapeJavaScript(pString);
    }
    
    public boolean isAuthorized(){
        try {
            return getContext().getCoreSession().hasPermission(LabsSiteUtils.getSitesRoot(getContext().getCoreSession()).getRef(), SecurityConstants.EVERYTHING);
        } catch (ClientException e) {
            return false;
        }
    }
    
    public String getPathForEdit(){
        return getPath();
    }
    
    public LabsSite getLabsSite() {
        return currentLabsSite;
    }
    
    public DocumentModel getDoc() throws ClientException{
        return LabsSiteUtils.getSitesRoot(ctx.getCoreSession());
    }
    
    public ArrayList<LabsSite> getLabsSites() throws ClientException {
        final String logPrefix = "<getLabsSites> ";
        ArrayList<LabsSite> result = new ArrayList<LabsSite>();
        DocumentModelList listDoc = new DocumentModelListImpl();
        Sorter sorter = null;
        Filter filter = null;
        boolean isAuthorized = isAuthorized();
        if (isAuthorized){
            listDoc = ctx.getCoreSession().getChildren(getDoc().getRef(), LabsSiteConstants.Docs.SITE.type(), null, null, sorter);
        }
        else{
            listDoc = ctx.getCoreSession().getChildren(getDoc().getRef(), LabsSiteConstants.Docs.SITE.type(), null, filter, sorter);
        }
        LabsSite labssite = null;
        for (DocumentModel doc1 : listDoc){
            log.debug(logPrefix + doc1.getName());
            labssite = doc1.getAdapter(LabsSite.class);
            result.add(labssite);
        }
        return result; 
    }

    @POST
    @Path(value="persistLabsSite")
    public Response doPost(
            @FormParam("labsSiteTitle") String pTitle,
            @FormParam("labsSiteURL") String pURL,
            @FormParam("labsSiteDescription") String pDescription,
            @FormParam("labssiteId") String pId){
        CoreSession session = ctx.getCoreSession();
        boolean isNew = isNew(pId);
        try {
            if (isNew){
                if (!existURL(pURL, session)){
                    DocumentModel docLabsSite = session.createDocumentModel(LabsSiteUtils.getSitesRootPath(), pTitle, LabsSiteConstants.Docs.SITE.type());
                    LabsSite labSite = docLabsSite.getAdapter(LabsSite.class);
                    labSite.setTitle(pTitle);
                    labSite.setDescription(pDescription);
                    labSite.setURL(pURL);
                    docLabsSite = session.createDocument(docLabsSite);
                    session.save();
                    return Response.status(Status.OK).entity(pTitle + " created.").build();
                }
            }
            else{
                DocumentModel docLabsSite = getDocument(pTitle, pId, session, isNew);   
                LabsSite labSite = docLabsSite.getAdapter(LabsSite.class);
                labSite.setTitle(pTitle);
                labSite.setDescription(pDescription);
                if(!labSite.getURL().equals(pURL)){
                    if (!existURL(pURL, session)){
                       labSite.setURL(pURL);
                        saveDocument(session, isNew, docLabsSite);
                        return Response.status(Status.OK).build();
                    }
                }
                else{
                    saveDocument(session, isNew, docLabsSite);
                    return Response.status(Status.OK).build();
                }
            }
            return Response.status(Status.NOT_MODIFIED).build();
        } catch (ClientException e) {
            log.debug(e, e);
            return Response.status(Status.GONE).build();
        }

    }

    private boolean existURL(String pURL, CoreSession pSession) throws ClientException {
        DocumentModelList listDoc = pSession.query("SELECT * FROM Document where webc:url = '" + pURL +"' ");
        if (listDoc != null && !listDoc.isEmpty()){
            return true;
        }
        return false;
    }

    @PUT
    @Path("edit/{idLabsSite}")
    public Object editLabsSite(@PathParam("idLabsSite") final String pIdLabsSite){
        try {
            DocumentModel document = ctx.getCoreSession().getDocument(new IdRef(pIdLabsSite));
            currentLabsSite = document.getAdapter(LabsSite.class);
        } catch (ClientException e) {
            log.error(e, e);
        }
        Template template = getTemplate(EDIT_VIEW);
        return template;
    }

    /**
     * @param pSession
     * @param pIsNew
     * @param pDocLabsSite
     * @throws ClientException
     */
    private void saveDocument(CoreSession pSession, boolean pIsNew,
            DocumentModel pDocLabsSite) throws ClientException {
        if (pIsNew){
            pDocLabsSite = pSession.createDocument(pDocLabsSite);
        }
        else{
            pDocLabsSite = pSession.saveDocument(pDocLabsSite);
        }
        pSession.save();
    }

    /**
     * @param pName
     * @param pId
     * @param pSession
     * @param pIsNew
     * @return
     * @throws ClientException
     */
    private DocumentModel getDocument(String pName, String pId,
            CoreSession pSession, boolean pIsNew) throws ClientException {
        DocumentModel docLabsSite;
        if (pIsNew){
            docLabsSite = pSession.createDocumentModel(LabsSiteUtils.getSitesRootPath(), pName,LabsSiteConstants.Docs.SITE.type());
        }
        else{
            docLabsSite = pSession.getDocument(new IdRef(pId));
        }
        return docLabsSite;
    }

    /**
     * @param pId
     * @return
     */
    private boolean isNew(String pId) {
        boolean isNew = false;
        if (StringUtils.isEmpty(pId)){
            isNew = true;
        }
        else {
            if ("-1".equals(pId)){
                isNew = true;
            }
            else{
                isNew = false;
            }
        }
        return isNew;
    }

    /* (non-Javadoc)
     * @see org.nuxeo.ecm.webengine.model.impl.ModuleRoot#handleError(javax.ws.rs.WebApplicationException)
     */
    @Override
    public Object handleError(WebApplicationException e) {
        if (e instanceof WebResourceNotFoundException) {
            String fileName = "error/error_404.ftl";
            log.debug(fileName);
            return Response.status(404).entity(getTemplate(fileName)).build();
        } else {
            log.info("No error handling for class " + e.getClass().getName());
            log.error(e.getMessage(), e);
            return super.handleError(e);
        }
    }
}
