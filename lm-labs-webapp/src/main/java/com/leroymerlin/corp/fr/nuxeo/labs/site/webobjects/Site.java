package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.exceptions.WebDocumentException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

@WebObject(type = "site")
@Produces("text/html; charset=UTF-8")
public class Site extends DocumentObject {

    private static final String WELCOME = "welcome";
    public static final String SITE_VIEW = "index";

    @Path("id/{idPage}")
    public Object doGetPageId(@PathParam("idPage") final String idPage){
        DocumentRef docRef = new IdRef(idPage);
        try {
            DocumentModel destDoc = ctx.getCoreSession().getDocument(docRef);
            String type = null;
            if (LabsSiteConstants.Docs.PAGENEWS.type().equals(destDoc.getType())){
                return newObject(type, destDoc);
            }
            else{
                throw new WebDocumentException("Type not supported");
            }
        } catch (ClientException e) {
            throw WebException.wrap("The document id='" + idPage + "' not exists", e);
        }
    }

    @Path("{path}")
    @Override public Resource traverse(@PathParam("path") String pageName) {
        if (WELCOME.equals(pageName)){
            try {
                DocumentModel destDoc = getCoreSession().getDocument(new PathRef(doc.getPathAsString() + "/" + LabsSiteConstants.Docs.TREE.docName() + "/" + LabsSiteConstants.Docs.WELCOME.docName()));
                return newObject(LabsSiteConstants.Docs.WELCOME.type(), destDoc);
            } catch (ClientException e) {
                throw WebException.wrap("Page with name '" + WELCOME + "' not found.", e);
            }
        }
        try {
            DocumentModel tree = getCoreSession().getChild(doc.getRef(), LabsSiteConstants.Docs.TREE.docName());
            StringBuffer sb = new StringBuffer("SELECT * FROM Document");
            sb.append(" WHERE ecm:parentId = '").append(tree.getId()).append("'");
            sb.append(" and ecm:name = '").append(pageName).append("'");
            DocumentModelList pages = getCoreSession().query(sb.toString());
            if (pages.isEmpty()) {
                throw new WebException("Page with name '" + pageName + "' not found.");
            }
            DocumentModel destDoc = pages.get(0);
            if (LabsSiteConstants.Docs.LABSNEWS.type().equals(destDoc.getType())){
                return newObject(destDoc.getType(), destDoc);
            }
            else{
                throw new WebDocumentException("Type not supported");
            }
        } catch (ClientException e) {
            throw WebException.wrap("Page with name '" + pageName + "' not found.", e);
        }
    }
}
