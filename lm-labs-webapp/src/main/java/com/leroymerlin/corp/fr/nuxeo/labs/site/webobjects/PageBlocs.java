/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;

/**
 * @author fvandaele
 *
 */
@WebObject(type = "PageBlocs")
@Produces("text/html; charset=UTF-8")
public class PageBlocs extends DocumentObject {

    public static final String SITE_VIEW = "index";

    @GET
    @Override
    public Object doGet() {

        Template template = getView(SITE_VIEW);

        // get children
        CoreSession session = getContext().getCoreSession();
        // set variable
        template.arg("siteName", doc.getName());
        try {
            template.arg("rootFolder", session.getChildren(doc.getRef()));
        } catch (ClientException e) {
            // FIXME log
            e.printStackTrace();
        }

        return template;
    }

}
