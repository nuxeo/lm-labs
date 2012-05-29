package com.leroymerlin.corp.fr.nuxeo.forum;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.security.SecurityConstants;

import com.leroymerlin.common.core.security.SecurityData;
import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractLabsBase;
import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.SecurityDataHelper;
import com.leroymerlin.corp.fr.nuxeo.topic.LMTopic;

public class LMForumImpl extends AbstractPage implements LMForum {

	public LMForumImpl(DocumentModel doc) {
		this.doc = doc;
	}

	@Override
	public LMTopic addTopic(CoreSession session, String topicTitle) throws ClientException {
	    
	    DocumentModel docTopic = session.createDocumentModel(doc.getPathAsString(), LabsSiteUtils.doLabsSlugify(topicTitle), LabsSiteConstants.Docs.LABSTOPIC.type());
        
        docTopic = session.createDocument(docTopic);
        docTopic.setPropertyValue(AbstractLabsBase.DC_TITLE, topicTitle);
        session.save();
        
        //add read write for the user 
        final DocumentRef ref = docTopic.getRef();
        final String user =  session.getPrincipal().getName();
        UnrestrictedSessionRunner runner = new UnrestrictedSessionRunner(session){
            @Override
            public void run() throws ClientException {
                DocumentModel docu = session.getDocument(ref);
                if (docu != null){
                    SecurityData data = SecurityDataHelper.buildSecurityData(docu);
                    data.addModifiablePrivilege(user, SecurityConstants.READ_WRITE, true);
                    SecurityDataHelper.updateSecurityOnDocument(docu, data);
                    session.save();
                }
            }
        };
        runner.runUnrestricted();
        
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