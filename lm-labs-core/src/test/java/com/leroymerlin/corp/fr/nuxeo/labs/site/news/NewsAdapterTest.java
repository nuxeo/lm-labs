package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.Date;

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
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

@RunWith(FeaturesRunner.class)
@Features(com.leroymerlin.corp.fr.nuxeo.labs.site.SiteFeatures.class)
@RepositoryConfig(cleanup = Granularity.METHOD)
public class NewsAdapterTest {

    private static final String NEWS_TYPE = LabsSiteConstants.Docs.LABSNEWS.type();

    @Inject
    private CoreSession session;

    @Test
    public void iCanCreateANewsDocument() throws Exception {
        // Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "news", NEWS_TYPE);

        // Modify property
        doc.setPropertyValue("dc:title", "le titre");

        // Persist document in db
        doc = session.createDocument(doc);

        // Commit
        session.save();

        doc = session.getDocument(new PathRef("/news"));
        assertThat(doc, is(notNullValue()));
        assertThat(doc.getTitle(), is("le titre"));

    }

    @Test
    public void iCanCreateANewsAdapter() throws Exception {
      //Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "myNews",NEWS_TYPE);
        
        LabsNews news = doc.getAdapter(LabsNews.class);
        assertThat(news,is(notNullValue()));
        news.setTitle("Le titre de la news");
        
        //Persist document in db
        doc = session.createDocument(doc);
        
        //Commit
        session.save();
        
        doc = session.getDocument(new PathRef("/myNews"));
        news = doc.getAdapter(LabsNews.class);
        assertThat(news,is(notNullValue()));
        assertThat(news.getTitle(), is("Le titre de la news"));

    }

    @Test
    public void iCanGetPropertiesOnNewsAdapter() throws Exception {
      //Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "myNews",NEWS_TYPE);

        doc.setPropertyValue("dc:creator", "creator");
        
        LabsNews news = doc.getAdapter(LabsNews.class);
        assertThat(news,is(notNullValue()));
        news.setTitle("Le titre de la news");
        news.setAccroche("Accroche");
        news.setContent("Content");
        news.setStartPublication(Calendar.getInstance());
        news.setEndPublication(Calendar.getInstance());
        news.setNewsTemplate("newTemplate.temp");
        
        //Persist document in db
        doc = session.createDocument(doc);
        
        //Commit
        session.save();
        
        doc = session.getDocument(new PathRef("/myNews"));
        news = doc.getAdapter(LabsNews.class);
        assertThat(news,is(notNullValue()));
        assertThat(news.getTitle(), is("Le titre de la news"));
        assertThat(news.getCreator(), is(notNullValue()));
        assertThat(news.getLastContributor(), is(notNullValue()));
        assertThat(news.getStartPublication(), is(notNullValue()));
        assertThat(news.getEndPublication(), is(notNullValue()));
        assertThat(news.getAccroche(), is("Accroche"));
        assertThat(news.getContent(), is("Content"));
        assertThat(news.getNewsTemplate(), is("newTemplate.temp"));

    }
}
