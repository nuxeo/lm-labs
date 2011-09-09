package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.classeur;

import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurFolder;

@WebObject(type = "PageClasseurFolder")
public class PageClasseurFolderResource extends DocumentObject {

    private PageClasseurFolder folder;

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        folder = doc.getAdapter(PageClasseurFolder.class);
    }

    @POST
    @Override
    public Response doPost() {

        FormData form = ctx.getForm();
        if (form.isMultipartContent()) {
            String desc = form.getString("description");
            Blob blob = form.getFirstBlob();
            try {
                blob.persist();
                if (blob.getLength() > 0) {
                    folder.addFile(blob, desc);
                    getCoreSession().save();
                }
                return redirect(prev.getPath());
            } catch (Exception e) {
                return Response.serverError()
                        .status(Status.FORBIDDEN)
                        .entity(e.getMessage())
                        .build();
            }
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

}
