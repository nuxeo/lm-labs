package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter.assets;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.rest.DocumentHelper;
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
            PathRef pathRef = new PathRef(doc.getPath().append(path).toString());

            if (ctx.getCoreSession().exists(pathRef)) {
                DocumentModel currentDoc = ctx.getCoreSession().getDocument(
                        pathRef);

                if (currentDoc.isFolder()) {
                    return ctx.newObject("AssetFolder", currentDoc);
                } else {
                    return ctx.newObject(currentDoc.getType(), currentDoc);
                }
            } else {
                return null;
            }

        } catch (Exception e) {
            throw WebException.wrap(e);
        }
    }

    public String getCalledRef() {
        return (String) ctx.getRequest().getSession().getAttribute(
                "calledRef");
    }

    public String getCallFunction() {
        return (String) ctx.getRequest().getSession().getAttribute(
                "callFunction");
    }

    @POST
    public Response doPost() {
        FormData form = ctx.getForm();
        CoreSession session = getCoreSession();
        if (form.isMultipartContent()) {
            String desc = form.getString("description");
            String noRedirect = form.getString("no_redirect");
            Blob blob = form.getFirstBlob();
            try {
                blob.persist();
                if (blob.getLength() > 0) {
                    DocumentModel file = addFile(blob);
                    if (!StringUtils.isBlank(desc)) {
                        file.setPropertyValue("dc:title", desc);
                        session.saveDocument(file);
                    }
                    session.save();
                }
                if (noRedirect != null) {
                    JSONObject json = new JSONObject();
                    json.element("text", "OK");
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();
                } else {
                    return redirect(getPath());
                }
            } catch (Exception e) {
                throw WebException.wrap(e);
            }
        } else {
            String name = ctx.getForm().getString("dublincore:title");
            DocumentModel newDoc = DocumentHelper.createDocument(ctx, doc, name);
            String pathSegment = URIUtils.quoteURIPathComponent(newDoc.getName(),
                    true);
            try {
                newDoc.setPropertyValue("dc:title", name);
                session.saveDocument(newDoc);
            } catch (Exception e) {
                throw WebException.wrap(e);
            }
            return redirect(getPath() + '/' + pathSegment);
        }
    }

    private DocumentModel addFile(Blob blob) throws Exception {
        return Framework.getService(FileManager.class).createDocumentFromBlob(
                ctx.getCoreSession(), blob, doc.getPathAsString(), true,blob.getFilename());

    }

}
