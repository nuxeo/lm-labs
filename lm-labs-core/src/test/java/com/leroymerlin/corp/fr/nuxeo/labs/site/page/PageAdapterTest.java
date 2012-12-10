package com.leroymerlin.corp.fr.nuxeo.labs.site.page;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.platform.comment.api.CommentableDocument;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.base.LabsCommentFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

@RunWith(FeaturesRunner.class)
@Features({ SiteFeatures.class, LabsCommentFeature.class })
@Deploy({"com.leroymerlin.labs.core.test:OSGI-INF/core-types-contribTest.xml"})
@RepositoryConfig(init = PageClasseurRepositoryInit.class)
public class PageAdapterTest {

    private static final String TITRE_1 = "titre 1";

    private static final String DESCRIPTION_1 = "Ma description";

    @Inject
    private CoreSession session;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void iCanGetGenericAdaptorForPageClasseur() throws Exception {
        DocumentModel pageClasseur = session.getDocument(new PathRef(
                "/page_classeur"));
        Page adapter = Tools.getAdapter(Page.class, pageClasseur, session);
        assertNotNull(adapter);
    }

    @Test
    public void iCanSetTitle() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        assertNotNull(doc);
        Page page = Tools.getAdapter(Page.class, doc, session);
        assertNotNull(page);
        page.setTitle(TITRE_1);
        assertNotNull(page.getTitle());
        assertEquals(TITRE_1, page.getTitle());
    }

    @Test
    public void iCannotSetTitleToNull() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        assertNotNull(doc);
        Page page = Tools.getAdapter(Page.class, doc, session);
        assertNotNull(page);
        thrown.expect(IllegalArgumentException.class);
        page.setTitle(null);
    }

    @Test
    public void iCanSetDescription() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        Page page = Tools.getAdapter(Page.class, doc, session);
        assertNotNull(page);
        page.setDescription(DESCRIPTION_1);
        assertEquals(DESCRIPTION_1, page.getDescription());
    }

    @Test
    public void canGetCommentAdapter() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        assertTrue(doc.hasFacet("Commentable"));
        Page page = Tools.getAdapter(Page.class, doc, session);
        assertNotNull(page);
        CommentableDocument commentable = doc.getAdapter(CommentableDocument.class);
        assertNotNull(commentable);
        assertNotNull(commentable.getComments());
    }

    @Test
    public void canAddAndGetComments() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        assertTrue(doc.hasFacet("Commentable"));
        Page page = Tools.getAdapter(Page.class, doc, session);
        assertNotNull(page);
        CommentableDocument commentable = doc.getAdapter(CommentableDocument.class);
        assertNotNull(commentable);
        assertNotNull(commentable.getComments());
        DocumentModel newComment = newComment("Un commentaire");
        commentable.addComment(newComment);
             
        assertNotNull(commentable);
        assertNotNull(commentable.getComments());
        assertThat(commentable.getComments().size(), is(1));
        assertThat(
                (String) commentable.getComments().get(0).getPropertyValue(
                        "comment:text"), is("Un commentaire"));
    }

    private DocumentModel newComment(String cText) throws ClientException {
        DocumentModel comment = session.createDocumentModel("Comment");
        comment.setPropertyValue("comment:author",
                session.getPrincipal().getName());
        comment.setPropertyValue("comment:text", cText);
        comment.setPropertyValue("comment:creationDate", new Date());
        return comment;
    }

    @Test
    public void iCanGetDefaultCommentable() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        Page page = Tools.getAdapter(Page.class, doc, session);
        assertNotNull(page);
        assertFalse(page.isCommentable());
    }

    @Test
    public void iCanSetCommentable() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        Page page = Tools.getAdapter(Page.class, doc, session);
        assertNotNull(page);
        assertFalse(page.isCommentable());
        page.setCommentable(true);
        session.saveDocument(doc);
        session.save();
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(Page.class, doc, session);
        assertTrue(page.isCommentable());
    }

    @Test
    public void iCanGetDefaultDisplayableParameters() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        Page page = Tools.getAdapter(Page.class, doc, session);
        
        assertTrue(page.isDisplayable("dc:title"));
        assertTrue(page.isDisplayable("dc:description"));
    }

    @Test
    public void iCanSetDisplayableParameters() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        Page page = Tools.getAdapter(Page.class, doc, session);
        List<String> lisParamNotDisplayable = new ArrayList<String>();
        lisParamNotDisplayable.add("dc:title");
        page.setNotDisplayableParameters(lisParamNotDisplayable);
        
        session.saveDocument(doc);
        session.save();
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(Page.class, doc, session);
        
        assertFalse(page.isDisplayable("dc:title"));
        assertTrue(page.isDisplayable("dc:description"));
    }

    @Test
    public void iCanGetDefaultElementsPerPage() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        Page page = Tools.getAdapter(Page.class, doc, session);
        
        assertTrue(page.getElementsPerPage() == 0);
    }

    @Test
    public void iCanSetElementsPerPage() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        Page page = Tools.getAdapter(Page.class, doc, session);
        page.setElementsPerPage(5);
        
        session.saveDocument(doc);
        session.save();
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(Page.class, doc, session);
        assertTrue(page.getElementsPerPage() == 5);
    }
    
    @Test
    public void iCanHideAndShowInNavigation() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        Page page = Tools.getAdapter(Page.class, doc, session);
        assertFalse(page.isHiddenInNavigation());
        page.hideInNavigation();
        assertTrue(page.isHiddenInNavigation());
        page.showInNavigation();
        assertFalse(page.isHiddenInNavigation());
    }
    /* A GARDER
    @Test
    public void iCanSetCollapseType() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        Page page = Tools.getAdapter(Page.class, doc, session);
        assertEquals(CollapseTypes.EXPAND_ALL.type(), page.getCollapseType());

        page.setCollapseType(CollapseTypes.COLLAPSE_ALL);

        session.saveDocument(doc);
        session.save();
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(Page.class, doc, session);
        assertEquals(CollapseTypes.COLLAPSE_ALL.type(), page.getCollapseType());
    }
     */

    @Test
    public void iCanGetDefaultEmptyListTags() throws Exception {
        DocumentModel pageClasseur = session.getDocument(new PathRef(
                "/page_classeur"));
        Page page = Tools.getAdapter(Page.class, pageClasseur, session);
        assertNotNull(page);
        assertNotNull(page.getLabsTags());
        assertThat(page.getLabsTags().size(), is(0));
    }

    @Test
    public void iCanGetAndSetTags() throws Exception {
        DocumentModel pageClasseur = session.getDocument(new PathRef(
                "/page_classeur"));
        Page page = Tools.getAdapter(Page.class, pageClasseur, session);
        assertNotNull(page);
        List<String> tags = new ArrayList<String>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");
        page.setLabsTags(tags);
        pageClasseur = session.saveDocument(pageClasseur);
        session.save();
        pageClasseur = session.getDocument(pageClasseur.getRef());
        page = Tools.getAdapter(Page.class, pageClasseur, session);
        assertNotNull(page.getLabsTags());
        assertThat(page.getLabsTags().size(), is(3));
        assertThat(page.getLabsTags().get(0), is("tag1"));
        assertThat(page.getLabsTags().get(1), is("tag2"));
        assertThat(page.getLabsTags().get(2), is("tag3"));
    }

    @Test
    public void iCanGetDefaultIsELementTemplate() throws Exception {
        DocumentModel pageClasseur = session.getDocument(new PathRef(
                "/page_classeur"));
        Page page = Tools.getAdapter(Page.class, pageClasseur, session);
        assertNotNull(page);
        assertThat(page.isElementTemplate(), is(false));
    }

    @Test
    public void iCanSetIsELementTemplate() throws Exception {
        DocumentModel pageClasseur = session.getDocument(new PathRef(
                "/page_classeur"));
        Page page = Tools.getAdapter(Page.class, pageClasseur, session);
        assertNotNull(page);
        assertThat(page.isElementTemplate(), is(false));
        page.setElementTemplate(true);
        pageClasseur = session.saveDocument(page.getDocument());
        session.save();
        
        pageClasseur = session.getDocument(pageClasseur.getRef());
        page = Tools.getAdapter(Page.class, pageClasseur, session);
        assertNotNull(page);
        assertThat(page.isElementTemplate(), is(true));
    }

    @Test
    public void iCanSetAndGetELementPreview() throws Exception {
        DocumentModel pageClasseur = session.getDocument(new PathRef(
                "/page_classeur"));
        Page page = Tools.getAdapter(Page.class, pageClasseur, session);

        page.setElementTemplate(true);
        page.setElementPreview(getTestBlob());
        pageClasseur = session.saveDocument(page.getDocument());
        session.save();
        
        pageClasseur = session.getDocument(pageClasseur.getRef());
        page = Tools.getAdapter(Page.class, pageClasseur, session);
        assertNotNull(page);
        assertThat(page.isElementTemplate(), is(true));
        assertNotNull(page.getElementPreview());
    }

    private Blob getTestBlob() {
        String filename = "vision.jpg";
        File testFile = new File(
                FileUtils.getResourcePathFromContext("testFiles/" + filename));
        Blob blob = new FileBlob(testFile);
        blob.setMimeType("image/jpeg");
        blob.setFilename(filename);
        return blob;
    }

}
