package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.GET;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PageClasseur")
public class PageClasseurResource extends Page {

    private static final Log LOG = LogFactory.getLog(PageClasseurResource.class);
    
    @GET public Object doGet() {
        return getView("index");
    }

    
}
