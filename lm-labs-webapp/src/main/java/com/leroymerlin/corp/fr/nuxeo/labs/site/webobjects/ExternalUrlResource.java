package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.BooleanUtils;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "ExternalUrl")
public class ExternalUrlResource extends DocumentObject {

    @Override
    public Response doPost() {
        Response response = super.doPost();
        String redirect = ctx.getForm().getString("redirect");
        if (BooleanUtils.toBoolean(redirect)) {
            return response;
        }
        return Response.status(Status.OK).build();
    }

}
