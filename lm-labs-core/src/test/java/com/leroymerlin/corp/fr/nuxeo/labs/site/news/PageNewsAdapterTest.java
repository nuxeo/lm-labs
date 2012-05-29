package com.leroymerlin.corp.fr.nuxeo.labs.site.news;



import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedOutput;

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

        LabsNews news = pn.createNews("Ma news", session);
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

        LabsNews news = pn.createNews("Ma news", session);
        session.saveDocument(news.getDocumentModel());
        news = pn.createNews("Ma news2", session);
        session.saveDocument(news.getDocumentModel());
        news = pn.createNews("Ma news3", session);
        session.saveDocument(news.getDocumentModel());
        session.save();

        List<LabsNews> allNews = pn.getAllNews(session);
        assertThat(allNews.size(), is(3));

    }
    
    @Test
    public void testContainsOnGetAllNews() throws Exception {
        DocumentModel document = session.getDocument(new PathRef("/page_news"));
        PageNews pn = document.getAdapter(PageNews.class);
        LabsNews news = pn.createNews("Ma news", session);
        session.saveDocument(news.getDocumentModel());
        List<LabsNews> allNews = pn.getTopNews(Integer.MAX_VALUE, session);
        assertFalse(allNews.contains(news));
        
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(Calendar.MONTH, cal1.get(Calendar.MONTH) -1);
        cal2.set(Calendar.MONTH, cal2.get(Calendar.MONTH) + 1);
        news.setStartPublication(cal1);
        news.setEndPublication(cal2);
        session.saveDocument(news.getDocumentModel());
        allNews = pn.getTopNews(Integer.MAX_VALUE, session);
        assertTrue(allNews.contains(news));
    }

    @Test
    public void iCanRetrieveTopNews() throws Exception {
        DocumentModel document = session.getDocument(new PathRef("/page_news"));
        PageNews pn = document.getAdapter(PageNews.class);

        createDatasTestForTopNews(pn);

        List<LabsNews> allNews = pn.getTopNews(2, session);
        assertThat(allNews.size(), is(2));
        
        allNews = pn.getTopNews(3, session);
        assertThat(allNews.size(), is(3));
        
        allNews = pn.getTopNews(5, session);
        assertThat(allNews.size(), is(3));

    }

    /**
     * @param pn
     * @throws ClientException
     */
    private void createDatasTestForTopNews(PageNews pn) throws ClientException {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.set(Calendar.MONTH, cal1.get(Calendar.MONTH) -1);
        cal2.set(Calendar.MONTH, cal2.get(Calendar.MONTH) + 1);
        
        LabsNews news = pn.createNews("Ma news", session);
        news.setStartPublication(cal1);
        news.setEndPublication(cal2);
        session.saveDocument(news.getDocumentModel());
        news = pn.createNews("Ma news2", session);
        cal1 = Calendar.getInstance();
        cal2 = Calendar.getInstance();
        cal1.set(Calendar.MONTH, cal1.get(Calendar.MONTH) - 2);
        cal2.set(Calendar.MONTH, cal2.get(Calendar.MONTH) - 1);
        news.setStartPublication(cal1);
        news.setEndPublication(cal2);
        session.saveDocument(news.getDocumentModel());
        news = pn.createNews("Ma news3", session);
        cal1 = Calendar.getInstance();
        cal2 = Calendar.getInstance();
        cal1.set(Calendar.MONTH, cal1.get(Calendar.MONTH) - 1);
        cal2.set(Calendar.MONTH, cal2.get(Calendar.MONTH) + 2);
        news.setStartPublication(cal1);
        news.setEndPublication(cal2);
        session.saveDocument(news.getDocumentModel());
        news = pn.createNews("Ma news4", session);
        cal1 = Calendar.getInstance();
        cal2 = Calendar.getInstance();
        cal1.set(Calendar.MONTH, cal1.get(Calendar.MONTH) - 1);
        cal2.set(Calendar.MONTH, cal2.get(Calendar.MONTH) + 2);
        news.setStartPublication(cal1);
        news.setEndPublication(cal2);
        session.saveDocument(news.getDocumentModel());
        session.save();

        List<LabsNews> allNews = pn.getTopNews(2, session);
        assertThat(allNews.size(), is(2));
        
        allNews = pn.getTopNews(3, session);
        assertThat(allNews.size(), is(3));
        
        allNews = pn.getTopNews(5, session);
        assertThat(allNews.size(), is(3));

    }
    @Test
    public void iCanBuildRssTopNews() throws Exception {
        DocumentModel document = session.getDocument(new PathRef("/page_news"));
        PageNews pn = document.getAdapter(PageNews.class);

        createDatasTestForTopNews(pn);
        
        List<LabsNews> allNews = pn.getTopNews(3, session);
        assertThat(allNews.size(), is(3));
        
        SyndFeed feed = pn.buildRssLabsNews(allNews, "localhost:8080", "DefaultDescription", session);
        assertNotNull(feed);
        assertNotNull(feed.getDescription());
        assertNotNull(feed.getEntries());

        assertThat(feed.getEntries().size(), is(3));

    }

    @Test
    public void iCanWriteRssTopNews() throws Exception {
        DocumentModel document = session.getDocument(new PathRef("/page_news"));
        PageNews pn = document.getAdapter(PageNews.class);
        pn.setTitle("titre page News");

        createDatasTestForTopNews(pn);
        
        List<LabsNews> allNews = pn.getTopNews(3, session);
        assertThat(allNews.size(), is(3));
        
        SyndFeed feed = pn.buildRssLabsNews(allNews, "http://localhost:8080/", "DefaultDescription", session);
        assertNotNull(feed);
        assertNotNull(feed.getDescription());
        assertNotNull(feed.getEntries());

        assertThat(feed.getEntries().size(), is(3));
        
        File temp = null;
        try {
            temp = File.createTempFile("tempfile", ".tmp");
            assertNotNull(temp);
            assertThat(temp.length(), is(0l));
            
            FileOutputStream out = new FileOutputStream(temp);
            
            Writer writer = new PrintWriter(out);
            SyndFeedOutput outputFeed = new SyndFeedOutput();
            outputFeed.output(feed, writer);
            out.close();
            
            assertTrue(temp.length() > 0);
        } catch (Exception e) {
            assertTrue(false);
        }
        finally{
            if (temp != null && temp.exists()){
                temp.delete();
            }
        }
    }
}
