package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.platform.comment.api.CommentableDocument;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.LabsCommentFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Entry;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.UrlType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features({SiteFeatures.class, LabsCommentFeature.class})
@RepositoryConfig(cleanup=Granularity.METHOD)
public class PageListLineAdapterTest {
    private static final boolean CHECKBOX = true;
    private static final Calendar CAL = Calendar.getInstance();
    private static final String TEXT = "text";
    private static final int ID_HEADER = 1;
    private static final String PATH_SEPARATOR = "/";
    private static final String LINE_TITLE = "line";
    @Inject
    private CoreSession session;

    @Test
    public void canCreateDataModel() throws Exception {
        new PageListLineAdapter.Model(session, PATH_SEPARATOR, LINE_TITLE).create();
        assertTrue(session.exists(new PathRef(PATH_SEPARATOR + LINE_TITLE)));
    }

    @Test
    public void canSetLineAndGetLine() throws Exception {
        PageListLineAdapter.Model model = new PageListLineAdapter.Model(session, PATH_SEPARATOR, LINE_TITLE);
        PageListLine lineAdapter = model.getAdapter();
        assertThat(lineAdapter,is(notNullValue()));
        
        Entry entry = new Entry();
        entry.setIdHeader(ID_HEADER);
        entry.setText(TEXT);
        entry.setDate(CAL);
        entry.setCheckbox(CHECKBOX);
        UrlType url = new UrlType("nameURL", "http://www.google.fr");
        entry.setUrl(url);
        
        EntriesLine line = new EntriesLine();
        line.getEntries().add(entry);
        lineAdapter.setLine(line);
        
        lineAdapter = model.create();
        line = lineAdapter.getLine();
        assertThat(line.getEntries().size(), is(1));
        entry = line.getEntries().get(0);
        
        assertTrue(session.exists(new PathRef(PATH_SEPARATOR + LINE_TITLE)));
        assertTrue(ID_HEADER == entry.getIdHeader());
        assertTrue(TEXT.equals(entry.getText()));
        assertTrue(CAL.equals(entry.getDate()));
        assertTrue(CHECKBOX == entry.isCheckbox());
        assertTrue(url.equals(entry.getUrl()));
    }

    @Test
    public void canRemoveLine() throws Exception {
        PageListLineAdapter.Model model = new PageListLineAdapter.Model(session, PATH_SEPARATOR, LINE_TITLE);
        PageListLine lineAdapter = model.getAdapter();
        assertThat(lineAdapter,is(notNullValue()));
        
        Entry entry = new Entry();
        entry.setIdHeader(ID_HEADER);
        entry.setText(TEXT);
        entry.setDate(CAL);
        entry.setCheckbox(CHECKBOX);
        UrlType url = new UrlType("nameURL", "http://www.google.fr");
        entry.setUrl(url);
        
        EntriesLine line = new EntriesLine();
        line.getEntries().add(entry);
        lineAdapter.setLine(line);
        
        lineAdapter = model.create();
        
        line = lineAdapter.getLine();
        lineAdapter.removeLine();
        
        assertFalse(session.exists(new PathRef(PATH_SEPARATOR + LINE_TITLE)));
    }

    @Test
    public void canGetCommentAdapter() throws Exception {
        PageListLineAdapter.Model model = new PageListLineAdapter.Model(session, PATH_SEPARATOR, LINE_TITLE);
        model.create();
        session.save();
        CommentableDocument commentable = model.getCommentableDocument();
        assertNotNull(commentable);
        assertNotNull(commentable.getComments());
    }

    @Test
    @Ignore("Exception in commentManager - the repositoryName is null !")
    public void canAddAndGetComments() throws Exception {
        PageListLineAdapter.Model model = new PageListLineAdapter.Model(session, PATH_SEPARATOR, LINE_TITLE);
        model.create();
        session.save();
        CommentableDocument commentable = model.getCommentableDocument();
        assertNotNull(commentable);
        assertNotNull(commentable.getComments());
        DocumentModel newComment = newComment("Un commentaire");
        commentable.addComment(newComment);
        session.save();
        commentable = model.getCommentableDocument();
        assertNotNull(commentable);
        assertNotNull(commentable.getComments());
        assertThat(commentable.getComments().size(), is(1));
        assertThat((String)commentable.getComments().get(0).getPropertyValue("comment:text"), is("Un commentaire"));
    }
    
    private DocumentModel newComment(String cText) throws ClientException {
        DocumentModel comment = session.createDocumentModel("Comment");
        comment.setPropertyValue("comment:author", session.getPrincipal()
                .getName());
        comment.setPropertyValue("comment:text", cText);
        comment.setPropertyValue("comment:creationDate", new Date());
        comment = session.createDocument(comment);
        return comment;
    }
}
