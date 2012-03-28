package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PageListLineFile")
@Produces("text/html; charset=UTF-8")
public class PageListLineFileResource extends DocumentObject {

    @Path("ajax")
    @DELETE
    public Response doDeleteAjax() {
        super.doDelete();
        return Response.noContent().build();
    }

}
