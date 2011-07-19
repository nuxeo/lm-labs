package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;

@WebObject(type = "sitesRoot")
@Produces("text/html; charset=UTF-8")
@Path("/sitesroot")
public class SitesRoot extends ModuleRoot {

    public static final String ROOT_PATH = "/default-domain/sites-root"; // FIXME

    public static final String DEFAULT_VIEW = "root-index";

    public static final String SITE_VIEW = "site-index";

    @GET
    public Object doGetDefaultView() {
        return getView(DEFAULT_VIEW);
    }

    @GET
    @Path("{siteName}")
    public Object doGetSite(@PathParam("siteName") final String siteName)
            throws ClientException {
        Template template = getView(SITE_VIEW);

        if (StringUtils.isNotBlank(siteName)
                && getContext().getCoreSession().exists(
                        new PathRef(ROOT_PATH + "/" + siteName))) {
            // FIXME
            template.arg("siteName", siteName);
        }

        return template;
    }
}
