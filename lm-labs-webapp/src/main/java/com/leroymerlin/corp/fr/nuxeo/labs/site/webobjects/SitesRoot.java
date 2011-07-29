package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;

@WebObject(type = "sitesRoot")
@Produces("text/html; charset=UTF-8")
@Path("/labssites")
public class SitesRoot extends ModuleRoot {

    protected static final String DEFAULT_VIEW = "index";

    @GET
    public Object doGetDefaultView() {
        return getView(DEFAULT_VIEW);
    }

    @Path("{siteName}")
    public Object doGetSite(@PathParam("siteName") final String siteName)
            throws ClientException {
        CoreSession session = getContext().getCoreSession();
        PathRef siteRef = new PathRef(LabsSiteUtils.getSitesRootPath() + "/" + siteName);
        if (getContext().getCoreSession().exists(siteRef)) {
            DocumentModel doc = session.getDocument(siteRef);
            return newObject("site", doc);
        } else {
            return Response.ok().status(404).build();
        }
    }
}
