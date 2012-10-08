package com.leroymerlin.corp.fr.nuxeo.forum;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.platform.comment.api.CommentManager;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.LabsCommentFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.publisher.LabsPublisher;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.topic.LMTopic;


@RunWith(FeaturesRunner.class)
@Features({CoreFeature.class, SiteFeatures.class,  LabsCommentFeature.class})
@Deploy({"org.nuxeo.ecm.opensocial.spaces","com.leroymerlin.labs.forums.core"})
public class ForumCoreTest {
	
	private static final String FORUM_TITLE = "Mon titre de forum";
	private static final String FORUM_DESCRIPTION = "Ma description de forum";
	
	private static final String TOPIC_TITLE = "Mon titre de topic";
	private static final String TOPIC_DESCRIPTION = "Ma description de topic";
	
	private static final String TEST_COMMENT = "test comment";
	
	@Inject
	CoreSession session;
	
	@Inject
	CommentManager commentManager;
	
	@Test
	public void iCanCreateAForumDoc() throws Exception {
		DocumentModel doc = session.createDocumentModel("/","myForum","PageForum");
		doc = session.createDocument(doc);
		LMForum forum = doc.getAdapter(LMForum.class);
		
		forum.setTitle(FORUM_TITLE);
		forum.setDescription(FORUM_DESCRIPTION);
//		doc.setPropertyValue("dc:title", FORUM_TITLE);
//		doc.setPropertyValue("dc:description", FORUM_DESCRPTION);
		session.saveDocument(doc);
		session.save();
		
		
		doc = session.getDocument(new PathRef("/myForum"));
		assertThat(doc.getTitle(), is(FORUM_TITLE));
			
	}
	
	@Test
	public void iCanCreateATopicInAForum() throws Exception {
		DocumentModel doc = session.getDocument(new PathRef("/myForum"));
		LMForum forum = doc.getAdapter(LMForum.class);
		forum.addTopic(session, "myTopic");
		
//		forum.getTopic(session, "myTopic").setPropertyValue("dc:title", TOPIC_TITLE);
//		forum.getTopic(session, "myTopic").setPropertyValue("dc:description", TOPIC_DESCRIPTION);
//		session.saveDocument(forum.getTopic(session, "myTopic"));
		LMTopic topic = forum.getTopic(session, "myTopic");
		topic.setTitle(TOPIC_TITLE);
		topic.setDescription(TOPIC_DESCRIPTION);
		session.saveDocument(topic.getDocument());
		session.save();
		

//		DocumentModel doc = session.createDocumentModel("/myForum", "myTopic","LMForumTopic");
//		doc = session.createDocument(doc);
//		doc.setPropertyValue("dc:title", TOPIC_TITLE);
//		doc.setPropertyValue("dc:description", TOPIC_DESCRIPTION);
//		session.saveDocument(doc);
//		session.save();
	}
	
	
	@Test
	public void iCanGetAForumAdapter() throws Exception {
		DocumentModel doc = session.getDocument(new PathRef("/myForum"));
		LMForum forum = doc.getAdapter(LMForum.class);
		assertThat(forum,is(notNullValue()));
		assertThat(forum.getTitle(),is(FORUM_TITLE));
		assertThat(forum.getDescription(),is(FORUM_DESCRIPTION));
	}
	
	@Test
	public void isForumCommentable() throws Exception {
		DocumentModel doc = session.getDocument(new PathRef("/myForum"));
		assertTrue(doc.hasFacet("Commentable"));
	}
	
	@Test
	public void isCommentManagerNotNull() throws Exception {
		assertThat(commentManager,is(notNullValue()));
	}
	
	@Test
	public void iCanGetATopicAdapter() throws Exception {
		DocumentModel doc = session.getDocument(new PathRef("/myForum/myTopic"));
		LMTopic topic = doc.getAdapter(LMTopic.class);
		assertThat(topic,is(notNullValue()));
		assertThat(topic.getTitle(),is(TOPIC_TITLE));
		assertThat(topic.getDescription(),is(TOPIC_DESCRIPTION));
	}
	
	@Test
	public void iCanCreateCommentsOnTopic() throws Exception {
		DocumentModel doc = session.getDocument(new PathRef("/myForum/myTopic"));
		LMTopic topic = doc.getAdapter(LMTopic.class);
		assertThat(topic,is(notNullValue()));
		
		topic.addComment(session, TEST_COMMENT);
		assertThat(topic.getComments(),is(notNullValue()));
		assertThat(topic.getComments().size(), is(1));
		assertThat(
                (String) topic.getComments().get(0).getPropertyValue(
                        "comment:text"), is(TEST_COMMENT));
	}
	
	@Test
	public void LMForumHasAPageAdapter() throws Exception {
		DocumentModel doc = session.getDocument(new PathRef("/myForum"));
		Page page = doc.getAdapter(Page.class);
		assertThat(page, is(notNullValue()));
				
		LabsPublisher publisher = doc.getAdapter(LabsPublisher.class);
		assertThat(publisher, is( notNullValue()));
	}
	
	@Test
	public void canIsAllContibutorsDefault() throws Exception {
		DocumentModel doc = session.getDocument(new PathRef("/myForum"));
		LMForum forum = doc.getAdapter(LMForum.class);
		assertThat(forum.isAllContributors(),is(true));
	}
	
	@Test
	public void canSetAndIsAllContibutors() throws Exception {
		DocumentModel doc = session.getDocument(new PathRef("/myForum"));
		LMForum forum = doc.getAdapter(LMForum.class);
		List<String> fieldsNotDisplayable = new ArrayList<String>();
		fieldsNotDisplayable.add(LMForumImpl.TOPIC_NOT_ALL_CONTRIBUTOR);
		forum.setNotDisplayableParameters(fieldsNotDisplayable);
		forum.manageAllContributors(false);
		session.saveDocument(forum.getDocument());
		session.save();
		doc = session.getDocument(new PathRef("/myForum"));
		forum = doc.getAdapter(LMForum.class);
		assertThat(forum.isAllContributors(),is(false));
		
		forum.setNotDisplayableParameters(new ArrayList<String>());
		forum.manageAllContributors(true);
		session.saveDocument(forum.getDocument());
		session.save();
		doc = session.getDocument(new PathRef("/myForum"));
		forum = doc.getAdapter(LMForum.class);
		assertThat(forum.isAllContributors(),is(true));
	}
}