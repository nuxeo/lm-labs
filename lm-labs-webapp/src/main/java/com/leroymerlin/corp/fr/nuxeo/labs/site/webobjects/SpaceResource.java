package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.servlet.ServletRequest;
import javax.ws.rs.Produces;

import org.nuxeo.ecm.platform.web.common.vh.VirtualHostHelper;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "Space", superType = "LabsPage")
@Produces("text/html; charset=UTF-8")
public class SpaceResource extends PageResource{




    public String getBaseUrl() {
        ServletRequest request = ctx.getRequest();
        String baseURL = VirtualHostHelper.getBaseURL(request);
        if(baseURL.endsWith("//")) {
            baseURL = baseURL.substring(0,baseURL.length() - 1);
        }

        return baseURL;
    }

}
