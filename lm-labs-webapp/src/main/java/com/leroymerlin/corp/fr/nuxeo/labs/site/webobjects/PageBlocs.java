/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;

/**
 * @author fvandaele
 * 
 */
@WebObject(type = "PageBlocs")
@Produces("text/html; charset=UTF-8")
public class PageBlocs extends Page {
    
    public static final String SITE_VIEW = "index";
    
    private static final Log LOG = LogFactory.getLog(PageBlocs.class);

    @GET
    @Override
    public Object doGet() {

        Template template = getView(SITE_VIEW);

        // get children
        CoreSession session = getContext().getCoreSession();
        // set variable
        try {
            template.arg("rootFolder", session.getChildren(doc.getRef()));
        } catch (ClientException ce) {
            LOG.error("Unable to get description " + ce);
        }

        return template;
    }
}
