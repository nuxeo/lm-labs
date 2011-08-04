package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.rest.DocumentObject;

public class Page extends DocumentObject {

    private static final Log LOG = LogFactory.getLog(Page.class);

    public String getDescription() {
        try {
            return doc.getAdapter(
                    com.leroymerlin.corp.fr.nuxeo.labs.site.Page.class).getDescription();
        } catch (PropertyException pe) {
            LOG.error("Unable to get property " + pe);
        } catch (ClientException ce) {
            LOG.error("Unable to get description " + ce);
        }
        return null;
    }

    @POST
    @Path("updateDescription")
    public Object doSetDescription(
            @FormParam("description") final String description) {
        if (StringUtils.isBlank(description)) {
            return Response.status(Status.NOT_MODIFIED).build();
        }

        try {
            doc.getAdapter(
                    com.leroymerlin.corp.fr.nuxeo.labs.site.Page.class).setDescription(
                    description);
            getCoreSession().saveDocument(doc);
            getCoreSession().save();
        } catch (PropertyException pe) {
            LOG.error("Unable to get property " + pe);
        } catch (ClientException ce) {
            LOG.error("Unable to get description " + ce);
        }

        return Response.ok().build();
    }
}
