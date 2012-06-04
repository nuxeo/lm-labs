package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

@RunWith(FeaturesRunner.class)
@Features(com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures.class)
@Deploy( {
    "org.nuxeo.ecm.platform.commandline.executor",
    "org.nuxeo.ecm.platform.picture.convert"
})
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

        LabsNews news = Tools.getAdapter(LabsNews.class, doc, session);
        assertThat(news,is(notNullValue()));
        news.setTitle("Le titre de la news");

        //Persist document in db
        doc = session.createDocument(doc);

        //Commit
        session.save();

        doc = session.getDocument(new PathRef("/myNews"));
        news = Tools.getAdapter(LabsNews.class, doc, session);
        assertThat(news,is(notNullValue()));
        assertThat(news.getTitle(), is("Le titre de la news"));

    }

    @Test
    public void iCanGetPropertiesOnNewsAdapter() throws Exception {
      //Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "myNews",NEWS_TYPE);

        doc.setPropertyValue("dc:creator", "creator");

        LabsNews news = Tools.getAdapter(LabsNews.class, doc, session);
        assertThat(news,is(notNullValue()));
        news.setTitle("Le titre de la news");
        news.setAccroche("Accroche");
        news.setContent("Content");
        Calendar start = Calendar.getInstance();
        news.setStartPublication(start);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DATE, 1);
        news.setEndPublication(end);
        news.setNewsTemplate("newTemplate.temp");

        //Persist document in db
        doc = session.createDocument(doc);

        //Commit
        session.save();

        doc = session.getDocument(new PathRef("/myNews"));
        news = Tools.getAdapter(LabsNews.class, doc, session);
        assertThat(news,is(notNullValue()));
        assertThat(news.getTitle(), is("Le titre de la news"));
        assertThat(news.getCreator(), is(notNullValue()));
        assertThat(news.getLastContributor(), is(notNullValue()));
        assertThat(news.getStartPublication(), is(notNullValue()));
        assertThat(news.getStartPublication().get(Calendar.DATE), is(start.get(Calendar.DATE)));
        assertThat(news.getStartPublication().get(Calendar.MONTH), is(start.get(Calendar.MONTH)));
        assertThat(news.getStartPublication().get(Calendar.YEAR), is(start.get(Calendar.YEAR)));
        assertThat(news.getStartPublication().get(Calendar.HOUR_OF_DAY), is(0));
        assertThat(news.getStartPublication().get(Calendar.MINUTE), is(0));
        assertThat(news.getStartPublication().get(Calendar.SECOND), is(0));
        assertThat(news.getStartPublication().get(Calendar.MILLISECOND), is(0));
        
        assertThat(news.getEndPublication(), is(notNullValue()));
        assertThat(news.getEndPublication().get(Calendar.DATE), is(end.get(Calendar.DATE)));
        assertThat(news.getEndPublication().get(Calendar.MONTH), is(end.get(Calendar.MONTH)));
        assertThat(news.getEndPublication().get(Calendar.YEAR), is(end.get(Calendar.YEAR)));
        assertThat(news.getEndPublication().get(Calendar.HOUR_OF_DAY), is(23));
        assertThat(news.getEndPublication().get(Calendar.MINUTE), is(59));
        assertThat(news.getEndPublication().get(Calendar.SECOND), is(59));
        assertThat(news.getEndPublication().get(Calendar.MILLISECOND), is(999));
        
        assertThat(news.getAccroche(), is("Accroche"));
        assertThat(news.getContent(), is("Content"));
        assertThat(news.getNewsTemplate(), is("newTemplate.temp"));

        
    }

    @Test
    public void iCanSetPropertiesAEndPublicationNull() throws Exception {
      //Use the session as a factory
        DocumentModel doc = session.createDocumentModel("/", "myNews",NEWS_TYPE);

        doc.setPropertyValue("dc:creator", "creator");

        LabsNews news = Tools.getAdapter(LabsNews.class, doc, session);
        assertThat(news,is(notNullValue()));
        news.setTitle("Le titre de la news");
        news.setAccroche("Accroche");
        news.setContent("Content");
        Calendar start = Calendar.getInstance();
        news.setStartPublication(start);
        news.setEndPublication(null);
        news.setNewsTemplate("newTemplate.temp");

        //Persist document in db
        doc = session.createDocument(doc);

        //Commit
        session.save();

        doc = session.getDocument(new PathRef("/myNews"));
        news = Tools.getAdapter(LabsNews.class, doc, session);
        assertThat(news,is(notNullValue()));
        
        assertNull(news.getEndPublication());


        
    }

    @Test
    public void iCanGetRowsForANews() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "myNews",NEWS_TYPE);
        LabsNews news = Tools.getAdapter(LabsNews.class, doc, session);

        assertThat(news.getRows().size(),is(0));

        HtmlRow row = news.addRow();
        row.addContent(4, "picture");
        row.addContent(12, "content");

        doc = session.createDocument(doc);


        doc = session.getDocument(new PathRef("/myNews"));
        news = Tools.getAdapter(LabsNews.class, doc, session);
        assertThat(news.getRows().size(),is(1));
        assertThat(news.row(0).content(0).getHtml(), is("picture"));
        assertThat(news.row(0).content(1).getHtml(), is("content"));

        row = news.addRow();
        row.addContent(12, "content");
        row.addContent(4, "picture");
        doc = session.saveDocument(doc);


        doc = session.getDocument(new PathRef("/myNews"));
        news = Tools.getAdapter(LabsNews.class, doc, session);
        assertThat(news.getRows().size(),is(2));
        assertThat(news.row(0).content(0).getHtml(), is("picture"));
        assertThat(news.row(0).content(1).getHtml(), is("content"));
        assertThat(news.row(1).content(0).getHtml(), is("content"));
        assertThat(news.row(1).content(1).getHtml(), is("picture"));

    }
    
    @Test
    public void iCanGetLabsNewsBolbHolder() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "myNews",NEWS_TYPE);
        LabsNews news = Tools.getAdapter(LabsNews.class, doc, session);
        news.setTitle("le titre");
        doc = session.createDocument(news.getDocumentModel());
        session.save();

        doc = session.getDocument(new PathRef("/myNews"));
        assertThat(news.getBlobHolder(),is(notNullValue()));
    }
    
    @Ignore
    @Test
    public void iCanSetOriginalPicture() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "myNews",NEWS_TYPE);
        LabsNews news = Tools.getAdapter(LabsNews.class, doc, session);
        news.setTitle("le titre");
        news.setOriginalPicture( new FileBlob(getFileFromPath("labsNewsImg/vision.jpg"), "image/jpeg", null,"vision.jpg", null));
        news.setSummaryPicture( new FileBlob(getFileFromPath("labsNewsImg/gastro.png"), "image/png", null,"gastro.png", null));
        doc = session.createDocument(news.getDocumentModel());
        session.save();

        doc = session.getDocument(new PathRef("/myNews"));
        assertThat(news.getBlobHolder(),is(notNullValue()));
        assertThat(news.getBlobHolder().getBlobs().size(),is(4));
    }

    private static File getFileFromPath(String path) {
        File file = FileUtils.getResourceFileFromContext(path);
        assertTrue(file.length() > 0);
        return file;
    }
    
    @Ignore
    @Test
    public void iCanSetCropCoordsPicture() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "myNews",NEWS_TYPE);
        LabsNews news = Tools.getAdapter(LabsNews.class, doc, session);
        news.setTitle("le titre");
        news.setCropCoords("testCoordonées");
        doc = session.createDocument(news.getDocumentModel());
        session.save();

        doc = session.getDocument(new PathRef("/myNews"));
        assertThat(news.getBlobHolder(),is(notNullValue()));
        assertThat(news.getBlobHolder().getCropCoords(),is("testCoordonées"));
    }
    
    @Test
    public void iCanAddAccordeonPictures() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "myNews",NEWS_TYPE);
        LabsNews news = Tools.getAdapter(LabsNews.class, doc, session);
        news.setTitle("le titre");
        
        List<Blob> blobs = new ArrayList<Blob>();
        blobs.add(new FileBlob(getFileFromPath("labsNewsImg/banniere.jpg"), "image/jpeg", null,"banniere.jpg", null));
        blobs.add(new FileBlob(getFileFromPath("labsNewsImg/default_banner.png"), "image/png", null,"default_banner.png", null));
        news.getBlobHolder().addAccordeonPictures(blobs);
        
        doc = session.createDocument(news.getDocumentModel());
        session.save();

        doc = session.getDocument(new PathRef("/myNews"));
        assertThat(news.getBlobHolder(),is(notNullValue()));
        assertThat(news.getBlobHolder().getAccordeonBlobs().size(),is(2));
    }
    
    @Test
    public void iCanSetAccordeonPictures() throws Exception {
        DocumentModel doc = session.createDocumentModel("/", "myNews",NEWS_TYPE);
        LabsNews news = Tools.getAdapter(LabsNews.class, doc, session);
        news.setTitle("le titre");
        
        List<Blob> blobs = new ArrayList<Blob>();
        blobs.add(new FileBlob(getFileFromPath("labsNewsImg/banniere.jpg"), "image/jpeg", null,"banniere.jpg", null));
        blobs.add(new FileBlob(getFileFromPath("labsNewsImg/default_banner.png"), "image/png", null,"default_banner.png", null));
        news.getBlobHolder().addAccordeonPictures(blobs);
        
        doc = session.createDocument(news.getDocumentModel());
        session.save();

        doc = session.getDocument(new PathRef("/myNews"));
        assertThat(news.getBlobHolder(),is(notNullValue()));
        assertThat(news.getBlobHolder().getAccordeonBlobs().size(),is(2));
        
        blobs = new ArrayList<Blob>();
        blobs.add(new FileBlob(getFileFromPath("labsNewsImg/banniere.jpg"), "image/jpeg", null,"banniere.jpg", null));
        news.getBlobHolder().setAccordeonPictures(blobs);
        
        doc = session.createDocument(news.getDocumentModel());
        session.save();

        doc = session.getDocument(new PathRef("/myNews"));
        assertThat(news.getBlobHolder(),is(notNullValue()));
        assertThat(news.getBlobHolder().getAccordeonBlobs().size(),is(1));
    }
    
    @Ignore
    @Test
    public void iCanGetSummaryPicture() throws Exception {
        //TODO
        DocumentModel doc = session.createDocumentModel("/", "myNews",NEWS_TYPE);
        LabsNews news = Tools.getAdapter(LabsNews.class, doc, session);
        news.setTitle("le titre");
        //news.setOriginalPicture( new FileBlob(getFileFromPath("labsNewsImg/vision.jpg"), "image/jpeg", null,"vision.jpg", null));
        news.setOriginalPicture( new FileBlob(getFileFromPath("labsNewsImg/gastro.png"), "image/png", null,"gastro.png", null));
        
        doc = session.createDocument(news.getDocumentModel());
        session.save();

        doc = session.getDocument(new PathRef("/myNews"));
        news = Tools.getAdapter(LabsNews.class, doc, session);
        //assertThat(news.getBlobHolder(),is(notNullValue()));
        assertThat(news.hasSummaryPicture(),is(true));
        assertThat(news.getSummaryPicture(),is(notNullValue()));
    }
}
