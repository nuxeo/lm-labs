package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.ArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.Sorter;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;
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
    
    private static final String EDIT_VIEW = "views/sitesRoot/editLabsSite.ftl";
    
    /**
     * use getDoc()
     */
    @Deprecated
    private DocumentModel doc = null;
    
    private LabsSite currentLabsSite = null;
    
    private boolean existURL = false;

    public boolean isExistURL() {
        return existURL;
    }

    @GET
    public Object doGetDefaultView() {
        return getView(DEFAULT_VIEW);
    }

    @Path("{url}")
    public Object doGetSite(@PathParam("url") final String pURL)
            throws ClientException {
        CoreSession session = getContext().getCoreSession();
        
        DocumentModelList listDoc = session.query("SELECT * FROM Document where webc:url = '" + pURL +"' ");
        if (listDoc != null && !listDoc.isEmpty()){
            DocumentModel document = listDoc.get(0);
            if (session.exists(document.getRef())) {
                DocumentModel doc = session.getDocument(document.getRef());
                return newObject("LabsSite", doc);
            } else {
                return Response.ok().status(404).build();
            }
        }
        return Response.ok().status(404).build();
    }
    
    public boolean isAuthorized(){
        return ((NuxeoPrincipal)this.getContext().getPrincipal()).isAdministrator();
    }
    
    public String getPathForEdit(){
        return getPath();
    }
    
    public LabsSite getLabsSite() {
        return currentLabsSite;
    }
    
    public DocumentModel getDoc(){
        if (doc == null){
            PathRef siteRef = new PathRef(LabsSiteUtils.getSitesRootPath());
            try {
                doc = ctx.getCoreSession().getDocument(siteRef);
            } catch (ClientException e) {
                log.error(e, e);
            }
        }
        return doc;
    }
    
    public ArrayList<LabsSite> getLabsSites() throws ClientException{
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
        for (DocumentModel doc1:listDoc){
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
                    DocumentModel docLabsSite = getDocument(pTitle, pId, session, isNew);   
                    LabsSite labSite = docLabsSite.getAdapter(LabsSite.class);
                    labSite.setTitle(pTitle);
                    labSite.SetDescription(pDescription);
                    labSite.setURL(pURL);
                    saveDocument(session, isNew, docLabsSite);
                    return Response.status(Status.OK).build();
                }
            }
            else{
                DocumentModel docLabsSite = getDocument(pTitle, pId, session, isNew);   
                LabsSite labSite = docLabsSite.getAdapter(LabsSite.class);
                labSite.setTitle(pTitle);
                labSite.SetDescription(pDescription);
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

    @DELETE
    @Path("delete/{idLabsSite}")
    public Response deleteLabssite(@PathParam("idLabsSite") final String pIdLabsSite) {
        CoreSession session = ctx.getCoreSession();
        try {
            DocumentModel document = ctx.getCoreSession().getDocument(new IdRef(pIdLabsSite));
            session.removeDocument(document.getRef());
            session.save();
        } catch (ClientException e) {
            return Response.status(Status.GONE).build();
        }
        return Response.noContent().build();
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
            docLabsSite = pSession.createDocumentModel(getDoc().getPathAsString(), pName,LabsSiteConstants.Docs.SITE.type());
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
}
