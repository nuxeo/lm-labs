/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import static org.nuxeo.ecm.webengine.WebEngine.SKIN_PATH_PREFIX_KEY;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Module;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;

/**
 * @author fvandaele
 *
 */
@WebAdapter(name = "banner", type = "bannerAdapter")
public class BannerAdapter extends DefaultAdapter {
    
    private static final Log LOG = LogFactory.getLog(BannerAdapter.class);
    
    String NUXEO_WEBENGINE_BASE_PATH = "nuxeo-webengine-base-path";
    
    private String getSkinPathPrefix(Module module) {
        if (Framework.getProperty(SKIN_PATH_PREFIX_KEY) != null) {
            return module.getSkinPathPrefix();
        }
        String webenginePath = ctx.getRequest()
                .getHeader(NUXEO_WEBENGINE_BASE_PATH);
        if (webenginePath == null) {
            return module.getSkinPathPrefix();
        } else {
            return ctx.getBasePath() + "/" + module.getName() + "/skin";
        }
    }
    
    @GET
    public Response getImgBanner() throws ClientException {
        Response response = null;
        DocumentModel document = getDocumentSite();
        if (document != null) {
            response =  getImgResponse(document);
        }
        if (response == null){
            response = redirect(getSkinPathPrefix(getModule()) + "/images/banniere.jpg");
        }
        return response;
    }
    
    
    @GET
    @Path("url")
    public String getPathFoBanner() throws ClientException{
        DocumentModel document = getDocumentSite();
        LabsSite labssite = document.getAdapter(LabsSite.class);
        if (labssite.getLogo() != null && !StringUtils.isEmpty(labssite.getLogo().getFilename())){
            return document.getPath() + "/" + labssite.getLogo().getFilename();
        }
        return null;
    }

    private static Response getImgResponse(DocumentModel document) throws ClientException {
        Blob blob = document.getAdapter(LabsSite.class).getLogo();
        if (blob != null){
            return Response.ok().entity(blob).type(blob.getMimeType()).build();
        }
        return null;
    }
    
    private DocumentModel getDocumentSite() throws ClientException{
        Resource target = getTarget();
        if (target instanceof DocumentObject){
            DocumentObject site = (DocumentObject)target;
            return LabsSiteUtils.getParentSite(site.getDocument());
        }
        return null;
    }
    
    public Object getBanner() throws ClientException{
        DocumentModel doc = getDocumentSite();
        LabsSite labssite = doc.getAdapter(LabsSite.class);
        return labssite.getLogo();
    }
    
    @DELETE
    public Response deleteImgBanner() throws ClientException{
        DocumentModel document = getDocumentSite();
        if (document != null){
            document.getAdapter(LabsSite.class).setLogo(null);
            CoreSession session = ctx.getCoreSession();
            session.saveDocument(document);
            session.save();
        }
        return Response.noContent().build();
    }
    
    @POST
    public Response doPost() {
        LOG.debug("POST doPost");
        FormData form = ctx.getForm();
        if (form.isMultipartContent()) {
            Blob blob = form.getFirstBlob();
            if (blob == null) {
                throw new IllegalArgumentException("Could not find any uploaded file");
            } else {
                try {
                    DocumentModel docu = getDocumentSite();
                    LabsSite labssite = docu.getAdapter(LabsSite.class);
                    CoreSession session = ctx.getCoreSession();
                    blob.persist();
                    labssite.setLogo(blob);
                    session.saveDocument(docu);
                    session.save();
                    return Response.ok("Upload file ok", MediaType.TEXT_PLAIN).build();
                } catch (Exception e) {
                    LOG.error(e);
                    return Response.serverError().status(Status.FORBIDDEN).entity(
                            e.getMessage()).build();
                }
            }
        }
        return Response.serverError().status(Status.FORBIDDEN).entity("ERROR").build();
    }
}
