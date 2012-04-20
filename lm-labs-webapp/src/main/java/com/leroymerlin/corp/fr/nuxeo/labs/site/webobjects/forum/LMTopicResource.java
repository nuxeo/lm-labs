package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.forum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.webengine.model.WebObject;

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
        	lmTopic = doc.getAdapter(LMTopic.class);
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
	
	
//	@GET
//    public Object doGet() {
//        return getView("index")/*.arg("forum", lmForum)*/;
//    }
    
//    @Override
//    @POST
//    public Response doPost() {
//    	LMTopic topic = null;
//        try {
//            FormData form = ctx.getForm();
//            String pTitle = form.getString("dc:title");
//            CoreSession session = ctx.getCoreSession();
//            LMForum lmForum = doc.getAdapter(LMForum.class);
//            topic = lmForum.addTopic(session, pTitle);
////
////            LabsNewsResource.fillNews(form, news);
////
////
//            DocumentModel newDocNews = session.saveDocument(topic.getDocument());
//            session.save();
//
//            return redirect(getPath() + "/" + newDocNews.getName() + "?props=open");
//        } catch (ClientException e) {
//            throw WebException.wrap(e);
//        } catch (IOException e) {
//            throw WebException.wrap(e);
//        }catch (LabsBlobHolderException e) {
//            log.info("The size of blob is too small !", e);
//            DocumentModel newDocNews = save(topic);
//            return redirect(getPath() + "/" + newDocNews.getName()
//                    + "?message_success=label.labsNews.news_notupdated.size&props=open");
//        }
//
//    }
//    
//    private DocumentModel save(LMTopic topic){
//        CoreSession session = ctx.getCoreSession();
//        DocumentModel newDocNews = null;
//        try {
//            newDocNews = session.saveDocument(topic.getDocument());
//            session.save();
//        } catch (ClientException e) {
//            throw WebException.wrap(e);
//        }
//        return newDocNews;
//    }
}