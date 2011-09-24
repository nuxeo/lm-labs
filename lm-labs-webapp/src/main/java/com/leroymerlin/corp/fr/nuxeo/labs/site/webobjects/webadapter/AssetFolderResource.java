package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.rest.DocumentHelper;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurFolder;

@WebObject(type = "AssetFolder", superType = "LabsPage")
public class AssetFolderResource extends DocumentObject {


    @Path(value = "{path}")
    @Override
    public Resource traverse(@PathParam("path") String path) {
        try {
            PathRef pathRef = new PathRef(doc.getPath().append(path).toString());
            DocumentModel doc = ctx.getCoreSession().getDocument(pathRef);

            if(doc.isFolder()) {
                return ctx.newObject("AssetFolder", doc);
            } else {
                return ctx.newObject(doc.getType(), doc);
            }
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
    }


    @POST
    public Response doPost() {
        FormData form = ctx.getForm();
        if (form.isMultipartContent()) {
            String desc = form.getString("description");
            Blob blob = form.getFirstBlob();
            try {
                blob.persist();
                if (blob.getLength() > 0) {
                    PageClasseurFolder folder = doc.getAdapter(PageClasseurFolder.class);
                    folder.addFile(blob, desc);
                    getCoreSession().save();
                }
                return redirect(getPath());
            } catch (Exception e) {
                throw WebException.wrap(e);
            }
        } else {
            return super.doPost();
        }

    }

}
