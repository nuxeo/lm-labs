/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.Sorter;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.blocs.ExternalURL;
import com.leroymerlin.corp.fr.nuxeo.labs.site.sort.ExternalURLSorter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;

/**
 * @author fvandaele
 * 
 */
@WebObject(type = "PageBlocs")
@Produces("text/html; charset=UTF-8")
public class PageBlocs extends Page {
    
    public static final String SITE_VIEW = "index";
    
    private static final Log LOG = LogFactory.getLog(PageBlocs.class);

    @GET
    @Override
    public Object doGet() {

        Template template;
        if (LabsSiteConstants.Docs.WELCOME.docName().equals(doc.getName())) {
            template = getView("welcome");
        } else {
            template = getView(SITE_VIEW);
        }
        return template;
    }
    
    
    public List<DocumentModel> getChildren() {
        try {
            return getCoreSession().query("SELECT * FROM Page WHERE ecm:parentId = '" + LabsSiteUtils.getSiteTree(LabsSiteUtils.getParentSite(doc)).getId() + "'");
        } catch (ClientException e) {
            LOG.error(e, e);
        }
        return new DocumentModelListImpl();
    }
    
    public ArrayList<ExternalURL> getExternalURLs() throws ClientException {
        ArrayList<ExternalURL> listExtURL = new ArrayList<ExternalURL>();
        DocumentModelList listDoc = null;
        Sorter extURLSorter = new ExternalURLSorter();
        listDoc = getCoreSession().getChildren(doc.getRef(), LabsSiteConstants.Docs.EXTERNAL_URL.type(), null, null, extURLSorter);
        for (DocumentModel doc:listDoc){
            ExternalURL extURL = doc.getAdapter(ExternalURL.class);
            listExtURL.add(extURL);
        }
        return listExtURL;
    }
    

    @PUT
    @Path(value="persistExternalURL")
    public Object modifyExternalURL(
            @FormParam("extUrlName") String pName,
            @FormParam("extURLURL") String pURL,
            @FormParam("extURLOrder") int pOrder){
        CoreSession session = ctx.getCoreSession();
        try {
            DocumentModel docExtURL = session.createDocumentModel(doc.getPathAsString(), pName, LabsSiteConstants.Docs.EXTERNAL_URL.type());
            ExternalURL extURL = docExtURL.getAdapter(ExternalURL.class);
            extURL.setName(pName);
            extURL.setURL(pURL);
            extURL.setOrder(pOrder);
            session.createDocument(docExtURL);
            session.save();
            return redirect(this.getPath());
        } catch (ClientException e) {
            return Response.status(Status.GONE).build();
        }
    }

    @PUT
    @Path(value="persistExternalURL/{idExt}")
    public Object addExternalURL(
            @FormParam("extUrlName") String pName,
            @FormParam("extURLURL") String pURL,
            @FormParam("extURLOrder") int pOrder,
            @PathParam("idExt") final String pId){
        CoreSession session = ctx.getCoreSession();

        try {
            DocumentModel docExtURL = session.getDocument(new IdRef(pId));
            ExternalURL extURL = docExtURL.getAdapter(ExternalURL.class);
            extURL.setName(pName);
            extURL.setURL(pURL);
            extURL.setOrder(pOrder);
            session.saveDocument(docExtURL);
            session.save();
            return redirect(this.getPath());
        } catch (ClientException e) {
            return Response.status(Status.GONE).build();
        }
    }

    @DELETE
    @Path("deleteExternalURL/{idExt}")
    public Object doDeleteNews(@PathParam("idExt") final String pId) throws ClientException {
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
