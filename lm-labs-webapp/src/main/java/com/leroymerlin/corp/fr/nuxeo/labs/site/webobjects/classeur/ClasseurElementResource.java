package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.classeur;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.LifeCycleConstants;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "ClasseurElement")
public class ClasseurElementResource extends DocumentObject {

    @Override
    public Response getDelete() {
        try {
            if (setAsDeleted()) {
                ctx.getCoreSession().save();
            }
        } catch (ClientException e) {
            throw WebException.wrap("Failed to set as 'deleted' for document " + doc.getPathAsString(), e);
        }
        if (prev != null) { // show parent ? TODO: add getView(method) to be able to change the view method
            return redirect(prev.getPath());
        }
        return redirect(ctx.getBasePath());
    }
    
    @GET
    @Path("@permanentDelete")
    public Response doPermanentDelete() {
        return doDelete();
    }

    @DELETE
    public Response doDelete() {
        super.doDelete();
        return redirect(prev.getPrevious()
                .getPath());
    }
    
    public boolean setAsDeleted() throws ClientException {
        if (doc.getAllowedStateTransitions().contains(LifeCycleConstants.DELETE_TRANSITION)) {
            doc.followTransition(LifeCycleConstants.DELETE_TRANSITION);
            doc = doc.getCoreSession().saveDocument(doc);
            return true;
        }
        return false;
    }
}
