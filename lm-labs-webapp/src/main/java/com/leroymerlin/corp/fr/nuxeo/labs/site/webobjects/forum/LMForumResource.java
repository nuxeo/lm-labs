package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.forum;

import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.forum.LMForum;
import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractLabsBase;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.NotifiablePageResource;
import com.leroymerlin.corp.fr.nuxeo.topic.LMTopic;

@WebObject(type = "PageForum", superType = "LabsPage")
public class LMForumResource extends NotifiablePageResource{
	
//private static final Log log = LogFactory.getLog(LMForumResource.class);
    
	LMForum lmForum;
	
	@Override
    public void initialize(Object... args) {
        super.initialize(args);
        ctx.getEngine()
                .getRendering()
                .setSharedVariable("forum", getLabsForum());
    }

    public LMForum getLabsForum() {
        return doc.getAdapter(LMForum.class);
    }
	
    @Override
    @POST
    public Response doPost() {
    	LMTopic topic = null;
        try {
        
           FormData form = ctx.getForm();
           String pTitle = form.getString(AbstractLabsBase.DC_TITLE);
           CoreSession session = ctx.getCoreSession();
           LMForum lmForum = doc.getAdapter(LMForum.class);
           topic = lmForum.addTopic(session, pTitle);
           topic.setDescription(form.getString(AbstractLabsBase.DC_DESCRIPTION));
           topic.setCommentable(true);
           save(topic);

           return redirect(getPath() + "/");
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }
    
    /**
     * save a new topic
     * @param topic
     * @return a {@link DocumentModel}
     */
    private DocumentModel save(LMTopic topic){
        CoreSession session = ctx.getCoreSession();
        DocumentModel newDoctopic = null;
        try {
            newDoctopic = session.saveDocument(topic.getDocument());
            session.save();
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
        return newDoctopic;
    }
}
