package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.classeur;

import javax.ws.rs.DELETE;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "ClasseurElement")
public class ClasseurElementResource extends DocumentObject {

    private static final Log LOG = LogFactory.getLog(ClasseurElementResource.class);


    @DELETE
    public Response doDelete() {
        super.doDelete();
        return redirect(prev.getPrevious().getPath());
    }
}
