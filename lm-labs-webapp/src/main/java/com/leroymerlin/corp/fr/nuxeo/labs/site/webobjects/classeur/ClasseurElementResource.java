package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.classeur;

import javax.ws.rs.DELETE;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "ClasseurElement")
public class ClasseurElementResource extends DocumentObject {

    @DELETE
    public Response doDelete() {
        super.doDelete();
        return redirect(prev.getPrevious()
                .getPath());
    }
}
