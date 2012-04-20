package com.leroymerlin.corp.fr.nuxeo.forum;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractLabsBase;
import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.topic.LMTopic;

public class LMForumImpl extends AbstractPage implements LMForum {

	public LMForumImpl(DocumentModel doc) {
		this.doc = doc;
	}

	@Override
	public LMTopic addTopic(CoreSession session, String topicTitle) throws ClientException {
		
		DocumentModel docTopic = session.createDocumentModel(doc.getPathAsString(), topicTitle,"LMForumTopic");
		docTopic = session.createDocument(docTopic);
		docTopic.setPropertyValue(AbstractLabsBase.DC_TITLE, topicTitle);
		session.save();
		return docTopic.getAdapter(LMTopic.class);
	}

	@Override
	public LMTopic getTopic(CoreSession session, String topicTitle) throws ClientException {
		LMTopic aLMTopic = null;
		DocumentModel docTopic = session.getDocument(new PathRef(doc.getPathAsString() + '/' + topicTitle));
		if (docTopic != null)
			aLMTopic = docTopic.getAdapter(LMTopic.class);
		
		return aLMTopic;			
	}

	@Override
	public List<LMTopic> getTopics(CoreSession session) throws ClientException {
		
		List<DocumentModel> listAllDocsChildrenTopic = session.getChildren(doc.getRef(), "LMForumTopic");
		
		List<LMTopic> allTopicsAdapter = new ArrayList<LMTopic>();
		for (DocumentModel docTopic : listAllDocsChildrenTopic)
			allTopicsAdapter.add(docTopic.getAdapter(LMTopic.class));
		
		return allTopicsAdapter;
	}
}