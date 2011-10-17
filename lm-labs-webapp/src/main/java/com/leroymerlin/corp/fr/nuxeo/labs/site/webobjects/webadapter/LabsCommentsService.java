/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.rest.CommentService;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.runtime.api.Framework;

/**
 * @author fvandaele
 *
 */
@WebAdapter(name = "labscomments", type = "LabsComments", targetFacets = { "Commentable" })
public class LabsCommentsService extends CommentService {
    
    Map<String, String> mapUserName = new HashMap<String, String>();
    
    @Override
    protected void publishComment(CoreSession session, DocumentModel target, DocumentModel comment) throws Exception {
        //do nothing
    }

    @Override
    protected void initialize(Object... args) {
        super.initialize(args);
    }
    
    @GET
    public Object doGet() {
        List<DocumentModel> comments = new ArrayList<DocumentModel>();
        try{
            DocumentObject dobj = (DocumentObject) getTarget();
            DocumentModel docLine = dobj.getDocument();
            comments = getCommentManager().getComments(docLine);
            loadFullName(comments);
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
        Template view = getView("index").arg("comments", comments);
        String isPage = ctx.getForm().getString("isPage");
        if (!StringUtils.isEmpty(isPage) && "yes".equals(isPage)){
            view.arg("deleteComment", "deleteCommentPage").arg("divTitleComments", "divTitleCommentsPage");
        }
        else{
            view.arg("deleteComment", "deleteComment").arg("divTitleComments", "divTitleComments");
        }
        return view;
    }

    private void loadFullName(List<DocumentModel> comments) throws Exception {
        mapUserName = new HashMap<String, String>();
        if (!comments.isEmpty()){
            UserManager userManager = Framework.getService(UserManager.class);
            String author = null;
            NuxeoPrincipal user = null;
            for(DocumentModel comment : comments){
                author = (String)comment.getPropertyValue("comment:author");
                if (!mapUserName.containsKey(author)){
                    user = userManager.getPrincipal(author);
                    if (user != null){
                        mapUserName.put(author, user.getFirstName() + " " + user.getLastName());
                    }
                    else{
                        mapUserName.put(author, author);
                    }
                }
            }
        }
    }
    
    public String getFullName(String pAuthor){
        if (mapUserName.containsKey(pAuthor)){
            return mapUserName.get(pAuthor);
        }
        return "";
    }

}
