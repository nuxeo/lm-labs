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

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.assets.AssetsDocumentTree;

@WebAdapter(name = "assets", type = "assetsAdapter", targetType = "LabsSite")
public class AssetsAdapter extends DefaultAdapter {


    @GET
    public Template doGet() throws ClientException {
        Resource resource = getAssetResource(getSite());
        return resource.getView("index");
    }

    @POST
    public Response doPost() throws ClientException {
        AssetFolderResource resource = getAssetResource(getSite());
        return resource.doPost();
    }

    private AssetFolderResource getAssetResource(LabsSite site) throws ClientException {
        return (AssetFolderResource) ctx.newObject("AssetFolder", site.getAssetsDoc());
    }

    @Path("{path}")
    public Object doTraverse(@PathParam("path") String path) throws ClientException {
        AssetFolderResource res = getAssetResource(getSite());
        return res.traverse(path);
    }

    private LabsSite getSite() {
        return (LabsSite) ctx.getProperty("site");
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
            return Response.ok()
                    .entity(result)
                    .build();
        }
        return null;
    }

    @GET
    @Path("/@views/content")
    public Template doGetRootContent() throws ClientException {
        AssetFolderResource folder = getAssetResource(getSite());
        return folder.getView("content");
    }

}
