package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;

@WebObject(type = "siteRoot")
@Produces("text/html; charset=UTF-8")
@Path("/siteroot")
public class SiteRoot extends ModuleRoot {

    @GET
    public Object doGetDefaultView() {
        return getView("index");
    }
}
