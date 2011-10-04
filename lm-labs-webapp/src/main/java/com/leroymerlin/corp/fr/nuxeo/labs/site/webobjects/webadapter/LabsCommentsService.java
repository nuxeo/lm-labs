/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.rest.CommentService;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.runtime.api.Framework;

/**
 * @author fvandaele
 *
 */
@WebAdapter(name = "labscomments", type = "LabsComments", targetType = "Document", targetFacets = { "Commentable" })
public class LabsCommentsService extends CommentService {

    @Override
    protected void initialize(Object... args) {
        super.initialize(args);
    }
    
    @GET
    @Path("{id}")
    public Object doGet(@PathParam("id") final String pId) {
        List<DocumentModel> comments = new ArrayList<DocumentModel>();
        try{
            DocumentObject dobj = (DocumentObject) getTarget();
            DocumentModel docLine = dobj.getCoreSession().getDocument(new IdRef(pId));
            comments = getCommentManager().getComments(docLine);
            loadFullName(comments);
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
        return getView("index").arg("comments", comments);
    }

    private void loadFullName(List<DocumentModel> comments) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        if (!comments.isEmpty()){
            UserManager userManager = Framework.getService(UserManager.class);
            String author = null;
            NuxeoPrincipal user = null;
            for(DocumentModel comment : comments){
                author = (String)comment.getPropertyValue("comment:author");
                if (!map.containsKey(author)){
                    user = userManager.getPrincipal(author);
                    if (user != null){
                        map.put(author, user.getFirstName() + " " + user.getLastName());
                    }
                    else{
                        map.put(author, null);
                    }
                }
            }
            String newAuthor = null;
            for(DocumentModel comment : comments){
                author = (String)comment.getPropertyValue("comment:author");
                if (map.containsKey(author)){
                    newAuthor = map.get(author);
                    if (newAuthor != null){
                        comment.setPropertyValue("comment:author", newAuthor);
                    }
                }
            }
        }
    }

}
