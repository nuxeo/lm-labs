package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.classeur;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurFolder;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

@WebObject(type = "PageClasseurFolder")
public class PageClasseurFolderResource extends DocumentObject {

    private static final Log LOG = LogFactory.getLog(PageClasseurFolderResource.class);

    private PageClasseurFolder folder;

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        folder = Tools.getAdapter(PageClasseurFolder.class, doc, ctx.getCoreSession());
    }
    
    @Override
    public Response getPut() {
        FormData form = ctx.getForm();
        String title = form.getString("dc:title");
        if (!StringUtils.isEmpty(title)) {
            return super.getPut();
        }
        return Response.serverError().status(Status.FORBIDDEN).entity("Folder name is empty").build();
    }

    @POST
    @Override
    public Response doPost() {
        FormData form = ctx.getForm();
        if (form.isMultipartContent()) {
            String desc = form.getString("description");
            String title = form.getString("title");
            List<Blob> failedBlobs = new ArrayList<Blob>();
            for (Blob blob : form.getBlobs("blob[]")) {
                blob.getFilename();
                try {
                    blob.persist();
                    if (blob.getLength() > 0) {
                        folder.addFile(blob, desc, title);
                    }
                } catch (Exception e) {
                    failedBlobs.add(blob);
                    LOG.error(e, e);
                }
            }
            if (!failedBlobs.isEmpty()) {
                return redirect(prev.getPath() + "?message_error=error.PageClasseur.upload.files" );
            }
            return redirect(prev.getPath() + "#" + folder.getDocument().getId());
        }
        return Response.serverError()
                .status(Status.FORBIDDEN)
                .entity("Invalid form")
                .build();
    }

    @Override
    public Object doGet() {
        return redirect(prev.getPath());
    }

    @Override
    public Response getDelete() {
        try {
            CoreSession session = ctx.getCoreSession();
            if (folder.setAsDeleted()) {
                session.save();
            }
        } catch (ClientException e) {
            throw WebException.wrap("Failed to set as 'deleted' for document " + doc.getPathAsString(), e);
        }
        if (prev != null) { // show parent ? TODO: add getView(method) to be able to change the view method
            return redirect(prev.getPath());
        }
        return redirect(ctx.getBasePath());
    }
    
    @GET
    @Path("@permanentDelete")
    public Response doPermanentDelete() {
        return doDelete();
    }

    @Path(value = "{path}")
    @Override
    public Resource traverse(@PathParam("path") String path) {
        try {
            PathRef pathRef = new PathRef(doc.getPath().append(path).toString());
            DocumentModel file = ctx.getCoreSession().getDocument(pathRef);
            return newObject("ClasseurElement", file, folder);
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }

    }
}
