package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.filemanager.api.FileManager;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.runtime.api.Framework;

@WebObject(type = "PageClasseur")
public class PageClasseurResource extends DocumentObject {

    private static final Log LOG = LogFactory.getLog(PageClasseurResource.class);
    
    @GET public Object doGet() {
        return getView("index");
    }

    @POST
    @Override
    public Response doPost() {
        LOG.debug("POST doPost");
        FormData form = ctx.getForm();
        if (form.isMultipartContent()) {
            String desc = form.getString("description");
            Blob blob = form.getFirstBlob();
            if (blob == null) {
                throw new IllegalArgumentException("Could not find any uploaded file");
            } else {
                blob.setFilename(StringUtils.deleteWhitespace(blob.getFilename()));
                try {
                    blob.persist();
                    CoreSession coreSession = ctx.getCoreSession();
                    DocumentModel fileDoc = Framework.getService(FileManager.class).createDocumentFromBlob(
                            coreSession, blob, doc.getPathAsString(), false,
                            blob.getFilename());
                    if (!StringUtils.isEmpty(desc)) {
                        fileDoc.setPropertyValue("dc:description", desc);
                    }
                    return Response.ok("Upload file ok", MediaType.TEXT_PLAIN).build();
                } catch (Exception e) {
                    return Response.serverError().status(Status.FORBIDDEN).entity(
                            e.getMessage()).build();
                }
            }
        }
        return Response.serverError().status(Status.FORBIDDEN).entity("ERROR").build();
    }
    
    @POST
    @Path("{folderId}/addFile")
    public Response doPostFolder(@PathParam("folderId") String folderId) {
        LOG.debug("POST doPostFolder");
        FormData form = ctx.getForm();
        if (form.isMultipartContent()) {
            String desc = form.getString("description");
            Blob blob = form.getFirstBlob();
            if (blob == null) {
                throw new IllegalArgumentException("Could not find any uploaded file");
            } else {
                blob.setFilename(blob.getFilename());
                try {
                    blob.persist();
                    CoreSession coreSession = ctx.getCoreSession();
                    DocumentModel folder = coreSession.getDocument(new IdRef(folderId));
                    if (!folder.getParentRef().equals(doc.getRef())) {
                        return Response.serverError().status(Status.FORBIDDEN).entity("folder is NOT a child of PageClasseur.").build();
                    }
                    DocumentModel fileDoc = Framework.getService(FileManager.class).createDocumentFromBlob(
                            coreSession, blob, folder.getPathAsString(), false, blob.getFilename());
                    if (!StringUtils.isEmpty(desc)) {
                        fileDoc.setPropertyValue("dc:description", desc);
                    }
                    return Response.ok("Upload file ok", MediaType.TEXT_PLAIN).build();
                } catch (Exception e) {
                    return Response.serverError().status(Status.FORBIDDEN).entity(
                            e.getMessage()).build();
                }
            }
        }
        return Response.serverError().status(Status.FORBIDDEN).entity("ERROR").build();
    }
    
    public BlobHolder getBlobHolder(final DocumentModel document) {
        return document.getAdapter(BlobHolder.class);
    }
    
}
