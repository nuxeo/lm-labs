/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.rest.CommentService;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
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
        DocumentModel document;
        try{
            DocumentObject dobj = (DocumentObject) getTarget();
            document = dobj.getDocument();
            comments = getCommentManager().getComments(document);
            afn = new AuthorFullName(new HashMap<String, String>(), LabsSiteConstants.Comments.COMMENT_AUTHOR);
            afn.loadFullName(comments);
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
        view.arg("isTopic", (LabsSiteConstants.Docs.LABSTOPIC.type().equals(document.getType())));
        return view;
    }
    
    @GET
    @Path("@getLastComment")
    public Response doGetLastComment() {
        List<DocumentModel> comments = new ArrayList<DocumentModel>();
        DocumentModel document;
        try{
            DocumentObject dobj = (DocumentObject) getTarget();
            document = dobj.getDocument();
            afn = new AuthorFullName(new HashMap<String, String>(), LabsSiteConstants.Comments.COMMENT_AUTHOR);
            List<DocumentModel> commentsList = getCommentManager().getComments(document);
            if (commentsList.size() > 0){
                DocumentModel docComment = commentsList.get(commentsList.size() - 1);
                comments.add(docComment);
                afn.loadFullName(comments);
                return Response.ok(docComment.getPropertyValue(LabsSiteConstants.Comments.COMMENT_TEXT)).build();
            }
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
        return Response.ok("nok").build();
    }
    
    @POST
    @Path("@setLastComment")
    public Response doSetLastComment() {
        DocumentModel document;
        try{
            DocumentObject dobj = (DocumentObject) getTarget();
            CoreSession session = dobj.getCoreSession();
            document = dobj.getDocument();
            List<DocumentModel> commentsList = getCommentManager().getComments(document);
            if (commentsList.size() > 0){
                DocumentModel docComment = commentsList.get(commentsList.size() - 1);
                docComment.setPropertyValue(LabsSiteConstants.Comments.COMMENT_TEXT, ctx.getForm().getString("text"));
                session.saveDocument(docComment);
                session.save();
            }
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
        return redirect(getTarget().getPath());
    }
    
    @DELETE
    @Override
    public Response deleteComment() throws Exception {
        FormData form = ctx.getForm();
        String docId = form.getString(FormData.PROPERTY);
        boolean isFirst = new Boolean(form.getString("isFirst")).booleanValue();
        CoreSession session = getContext().getCoreSession();
        DocumentModel comment = session.getDocument(new IdRef(docId));
        
        if (getContext().getPrincipal().getName().equals(comment.getPropertyValue("comment:author")) && isFirst){
            return super.deleteComment(); 
        }
        if (LabsSiteConstants.CommentsState.PENDING.getState().equals(comment.getCurrentLifeCycleState())){
            comment.followTransition(LabsSiteConstants.CommentsState.REJECT.getTransition());
            session.saveDocument(comment);
            session.save();
        }
        return redirect(getTarget().getPath());
    }
    
    public String getFullName(String pAuthor){
        if (afn != null){
            return afn.getFullName(pAuthor);
        }
        return "";
    }

}
