/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.rest.CommentService;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebAdapter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.AuthorFullName;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

/**
 * @author fvandaele
 *
 */
@WebAdapter(name = "labscomments", type = "LabsComments")
public class LabsCommentsService extends CommentService {
    
    private AuthorFullName afn = null;
    
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
            afn = new AuthorFullName(new HashMap<String, String>(), LabsSiteConstants.Comments.COMMENT_AUTHOR);
            afn.loadFullName(comments);
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
        Template view = getView("index").arg("comments", comments);
        boolean reverseComments = Boolean.parseBoolean(ctx.getForm().getString("reverseComments"));
        boolean removeOnlyLastComment = Boolean.parseBoolean(ctx.getForm().getString("removeOnlyLastComment"));
        String isPage = ctx.getForm().getString("isPage");
        if (!StringUtils.isEmpty(isPage) && "yes".equals(isPage)){
            view.arg("deleteComment", "deleteCommentPage").arg("divTitleComments", "divTitleCommentsPage").arg("reverseComments", reverseComments).arg("removeOnlyLastComment", removeOnlyLastComment);
        }
        else{
            view.arg("deleteComment", "deleteComment").arg("divTitleComments", "divTitleComments");
        }
        return view;
    }
    
    public String getFullName(String pAuthor){
        if (afn != null){
            return afn.getFullName(pAuthor);
        }
        return "";
    }

}
