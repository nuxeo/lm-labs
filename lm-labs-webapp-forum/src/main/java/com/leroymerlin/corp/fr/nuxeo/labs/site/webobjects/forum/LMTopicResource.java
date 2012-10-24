package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.forum;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractLabsBase;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.FacetNames;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.NotifiablePageResource;
import com.leroymerlin.corp.fr.nuxeo.topic.LMTopic;

@WebObject(type = "LMForumTopic", superType = "LabsPage")
public class LMTopicResource extends NotifiablePageResource{
	
private static final Log log = LogFactory.getLog(LMTopicResource.class);
    
	LMTopic lmTopic;
	
	@Override
    public void initialize(Object... args) {
        super.initialize(args);
        ctx.getEngine()
                .getRendering()
                .setSharedVariable("topic", getLabsTopic());
    }

    public LMTopic getLabsTopic() {
        if (lmTopic == null){
        	lmTopic = Tools.getAdapter(LMTopic.class, doc, getCoreSession());
        	try {
        		log.info(lmTopic.getDescription());
        		log.info(lmTopic.getTitle());
        		log.info(lmTopic.getDocument().toString());
				
			} catch (PropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return lmTopic;
    }
    
    @GET
    @Path("@modify")
    public Object modify() {
        return getView("index").arg("modify", true);
    }
	
    @Override
    @POST
    public Response doPost() {
        try {
        
           FormData form = ctx.getForm();
           String pTitle = form.getString(AbstractLabsBase.DC_TITLE);
           lmTopic.setTitle(pTitle);
           lmTopic.setDescription(form.getString(AbstractLabsBase.DC_DESCRIPTION));
           lmTopic.setCommentable(true);
           save(lmTopic);

           return redirect(getPrevious().getPath());
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
        CoreSession session = getCoreSession();
        DocumentModel newDoctopic = null;
        try {
            newDoctopic = session.saveDocument(topic.getDocument());
            session.save();
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
        return newDoctopic;
    }
    
    @GET
    @Path("@visibility/{action}")
    public Response doSetVisibility(@PathParam("action") String action) throws ClientException {
        if ("show".equals(action)) {
            show(doc);
            return redirect(getPrevious().getPath());
        } else if ("hide".equals(action)) {
            hide(doc);
            return redirect(getPrevious().getPath());
        }
        return Response.notModified().build();
    }
    
    public boolean hide(DocumentModel file) throws ClientException {
        if (!file.getFacets().contains(FacetNames.LABSHIDDEN)) {
            file.addFacet(FacetNames.LABSHIDDEN);
            ctx.getCoreSession().saveDocument(file);
            return true;
        }
        return false;
    }

    public boolean show(DocumentModel file) throws ClientException {
        if (file.getFacets().contains(FacetNames.LABSHIDDEN)) {
            file.removeFacet(FacetNames.LABSHIDDEN);
            ctx.getCoreSession().saveDocument(file);
            return true;
        }
        return false;
    }
}