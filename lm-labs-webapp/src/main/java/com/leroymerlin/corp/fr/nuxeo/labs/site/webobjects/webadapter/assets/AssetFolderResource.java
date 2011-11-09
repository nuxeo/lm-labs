package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter.assets;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.filemanager.api.FileManager;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.runtime.api.Framework;

@WebObject(type = "AssetFolder", superType = "LabsPage")
public class AssetFolderResource extends DocumentObject {

    @Path(value = "{path}")
    @Override
    public Resource traverse(@PathParam("path") String path) {
        try {
            PathRef pathRef = new PathRef(doc.getPath()
                    .append(path)
                    .toString());
            DocumentModel currentDoc = ctx.getCoreSession()
                    .getDocument(pathRef);

            if (currentDoc.isFolder()) {
                return ctx.newObject("AssetFolder", currentDoc);
            } else {
                return ctx.newObject(currentDoc.getType(), currentDoc);
            }
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
    }

    public String getCKEditorFuncNum() {
        return (String) ctx.getRequest().getSession().getAttribute("CKEditorFuncNum");
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
                    DocumentModel file = addFile(blob);
                    if(!StringUtils.isBlank(desc)) {
                        file.setPropertyValue("dc:title", desc);
                        getCoreSession().saveDocument(file);
                    }
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

    private DocumentModel  addFile(Blob blob) throws Exception {
        return Framework.getService(FileManager.class)
                .createDocumentFromBlob(doc.getCoreSession(), blob,
                        doc.getPathAsString(), true,
                        StringEscapeUtils.escapeHtml(blob.getFilename()));

    }

}
