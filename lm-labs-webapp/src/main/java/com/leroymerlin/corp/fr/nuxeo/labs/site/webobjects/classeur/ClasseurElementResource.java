package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.classeur;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.LifeCycleConstants;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurFolder;

@WebObject(type = "ClasseurElement")
public class ClasseurElementResource extends DocumentObject {

    private PageClasseurFolder parentFolder;

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
    
    @Override
    public Response getPut() {
        FormData form = ctx.getForm();
        String title = form.getString("dc:title");
        if (!StringUtils.isEmpty(title)) {
            super.getPut();
            return this.redirect(this.getPrevious().getPrevious().getPath() + "?message_success=label.PageClasseur.form.rename.saved");
            
        }
        return this.redirect(this.getPrevious().getPrevious().getPath() + "?message_error=label.PageClasseur.form.rename.unsaved");
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

    @Override
    public void initialize(Object... args) {
        assert args != null && args.length == 2;
        doc = (DocumentModel) args[0];
        parentFolder = (PageClasseurFolder) args[1];
    }
    
    @PUT
    @Path("@visibility/{action}")
    public Response doSetVisibility(@PathParam("action") String action) throws ClientException {
    	if ("show".equals(action)) {
    		parentFolder.show(doc);
    		return Response.noContent().build();
    	} else if ("hide".equals(action)) {
    		parentFolder.hide(doc);
    		return Response.noContent().build();
    	}
		return Response.notModified().build();
    }
}
