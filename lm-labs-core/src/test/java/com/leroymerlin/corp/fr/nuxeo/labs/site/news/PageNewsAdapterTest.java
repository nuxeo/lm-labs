package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(init=PageNewsRepositoryInit.class, cleanup=Granularity.METHOD)
public class PageNewsAdapterTest {
    @Inject
    private CoreSession session;

    @Test
    public void canCreatePageListe() throws Exception {
        assertTrue(session.exists(new PathRef("/page_news")));
    }

    @Test
    public void iCanGetLabsNewsAdapter() throws Exception {
        DocumentModel document = session.getDocument(new PathRef("/page_news"));
        assertThat(document.getAdapter(PageNews.class), is(notNullValue()));
    }

    @Test
    public void iCanCreateNewsViaPageNewsAdapter() throws Exception {
        DocumentModel document = session.getDocument(new PathRef("/page_news"));
        PageNews pn = document.getAdapter(PageNews.class);

        LabsNews news = pn.createNews("Ma news");
        news.setContent("Hello World");
        DocumentModel doc = news.getDocumentModel();
        session.saveDocument(doc);
        session.save();

        String path = doc.getPathAsString();


        doc = session.getDocument(new PathRef(path));


        news = doc.getAdapter(LabsNews.class);
        assertThat(news.getTitle(),is("Ma news"));
        assertThat(news.getContent(),is("Hello World"));

    }

    @Test
    public void iCanRetrieveAllNews() throws Exception {
        DocumentModel document = session.getDocument(new PathRef("/page_news"));
        PageNews pn = document.getAdapter(PageNews.class);

        LabsNews news = pn.createNews("Ma news");
        session.saveDocument(news.getDocumentModel());
        news = pn.createNews("Ma news2");
        session.saveDocument(news.getDocumentModel());
        news = pn.createNews("Ma news3");
        session.saveDocument(news.getDocumentModel());
        session.save();

        List<LabsNews> allNews = pn.getAllNews();
        assertThat(allNews.size(), is(3));

    }
}
