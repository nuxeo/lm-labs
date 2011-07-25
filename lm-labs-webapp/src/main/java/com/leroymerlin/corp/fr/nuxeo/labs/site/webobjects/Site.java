package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.NuxeoDocHelper;

@WebObject(type = "site")
@Produces("text/html; charset=UTF-8")
public class Site extends DocumentObject {

    public static final String SITE_VIEW = "index";

    @GET
    @Path("welcome")
    public Object doGetWelcome() throws ClientException {

        Template template = getView(SITE_VIEW);

        // get children
        CoreSession session = getContext().getCoreSession();
        Map<DocumentModel, DocumentModelList> rootFolderAndChildren = NuxeoDocHelper.getRootFolderAndChildren(
                doc, session);
        // set variable
        template.arg("siteName", doc.getName());
        template.arg("rootFolderAndChildren", rootFolderAndChildren);

        return template;
    }
}
