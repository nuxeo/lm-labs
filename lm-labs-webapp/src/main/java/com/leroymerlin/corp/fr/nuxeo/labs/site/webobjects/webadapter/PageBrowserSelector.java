package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import javax.ws.rs.GET;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebAdapter;

@WebAdapter(name = "pagesBrowser", type = "PagesBrowser", targetType = "LabsSite")
public class PageBrowserSelector extends CkEditorParametersAdapter {

	public PageBrowserSelector() {
		super();
	}

    @GET
    public Template doGet() throws ClientException {
        return getView("index");
    }

}
