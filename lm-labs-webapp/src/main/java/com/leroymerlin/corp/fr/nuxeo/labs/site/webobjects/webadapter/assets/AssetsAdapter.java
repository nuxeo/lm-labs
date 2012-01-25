/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter.assets;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.assets.AssetsDocumentTree;

@WebAdapter(name = "assets", type = "assetsAdapter", targetType = "LabsSite")
public class AssetsAdapter extends DefaultAdapter {

    public AssetsAdapter() {
        WebContext ctx = WebEngine.getActiveContext();
        //callerRef
        String parameter = ctx.getRequest().getParameter("CKEditorFuncNum");
        if (StringUtils.isBlank(parameter)) {
            parameter = ctx.getRequest().getParameter("calledRef");
        }
        if (StringUtils.isNotBlank(parameter)) {
            ctx.getRequest().getSession().setAttribute("calledRef",parameter);
        }
        
        //jscallback
        parameter = ctx.getRequest().getParameter("callFunction");
        if (StringUtils.isBlank(parameter)) {
            parameter = "CKEDITOR.tools.callFunction";
        }
        ctx.getRequest().getSession().setAttribute("callFunction",parameter);
    }

    @GET
    public Template doGet() throws ClientException {
        AssetFolderResource resource = getAssetResource(getSite());
        return resource.getView("index");
    }

    @POST
    public Response doPost() throws ClientException {
        AssetFolderResource resource = getAssetResource(getSite());
        return resource.doPost();
    }

    private AssetFolderResource getAssetResource(LabsSite site)
            throws ClientException {
        return (AssetFolderResource) ctx.newObject("AssetFolder",
                site.getAssetsDoc());
    }

    @Path("{path}")
    public Object doTraverse(@PathParam("path") String path)
            throws ClientException {
        AssetFolderResource res = getAssetResource(getSite());
        return res.traverse(path);
    }

    @Path("id/{id}")
    public Object doTraverseWithId(@PathParam("id") String id)
            throws ClientException {
        return doTraverseAsset(id);
    }

    @Path("paramId")
    public Object doTraverseWithParamId() throws ClientException {
        return doTraverseAsset(ctx.getForm().getString("id"));
    }

    private Object doTraverseAsset(final String id) throws ClientException {
        AssetFolderResource res = getAssetResource(getSite());
        String path = getContext().getCoreSession().getDocument(new IdRef(id)).getPath().toString();

        if (path.endsWith("assets")) {
            path = "";
        } else {
            path = path.substring(path.indexOf("assets/") + 7, path.length());
        }

        return res.traverse(path);
    }

    @GET
    @Path("json")
    public Response doGetJson(@QueryParam("root") String root)
            throws ClientException {
        LabsSite site = (LabsSite) ctx.getProperty("site");

        if (site != null) {
            DocumentModel assetsDoc = site.getAssetsDoc();
            AssetsDocumentTree tree = new AssetsDocumentTree(ctx, assetsDoc);
            String result = "";
            if (root == null || "source".equals(root)) {
                tree.enter(ctx, "");
                result = tree.getTreeAsJSONArray(ctx);
            } else {
                result = tree.enter(ctx, root);
            }
            return Response.ok().entity(result).build();
        }
        return null;
    }

    @GET
    @Path("/@views/content")
    public Template doGetRootContent() throws ClientException {
        AssetFolderResource folder = getAssetResource(getSite());
        return folder.getView("content");
    }

    private LabsSite getSite() {
        return (LabsSite) ctx.getProperty("site");
    }

}
