package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import javax.ws.rs.GET;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebAdapter;

@WebAdapter(name = "sharedElementBrowser", type = "SharedElementBrowser", targetType = "LabsSite")
public class SharedElementBrowserSelector extends CkEditorParametersAdapter {
	
	private String viewMode = "sharedElement";

	public SharedElementBrowserSelector() {
		super();
	}

    @GET
    public Template doGet() throws ClientException {
        return getView("index");
    }

	public String getViewMode() {
		return viewMode;
	}

}
