package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

@WebObject(type = "site")
@Produces("text/html; charset=UTF-8")
public class Site extends DocumentObject {

    @Path("id/{idPage}")
    public Object doGetPageId(@PathParam("idPage") final String idPage){
        DocumentRef docRef = new IdRef(idPage);
        try {
            DocumentModel destDoc = ctx.getCoreSession().getDocument(docRef);
            String type = null;
            if (LabsSiteConstants.Docs.fromString(doc.getType()) != null) {
                return newObject(type, destDoc);
            }
            else{
                throw new WebException("Unsupported document type " + doc.getType());
            }
        } catch (ClientException e) {
            throw WebException.wrap("The document id='" + idPage + "' not exists", e);
        }
    }

    @Override public DocumentObject newDocument(String path) {
        try {
            PathRef pathRef = new PathRef(doc.getPath().append("/" + LabsSiteConstants.Docs.TREE.docName()).append(path).toString());
            DocumentModel doc = ctx.getCoreSession().getDocument(pathRef);
            if (LabsSiteConstants.Docs.fromString(doc.getType()) == null) {
                throw new WebException("Unsupported document type " + doc.getType());
            }
            return (DocumentObject) ctx.newObject(doc.getType(), doc);
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
    }

    /* (non-Javadoc)
     * @see org.nuxeo.ecm.core.rest.DocumentObject#doGet()
     */
    @Override
    public Object doGet() {
        return redirect(getPath() + "/" + LabsSiteConstants.Docs.WELCOME.docName());
    }

}
