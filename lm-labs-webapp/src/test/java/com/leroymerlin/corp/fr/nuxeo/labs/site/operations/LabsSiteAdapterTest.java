package com.leroymerlin.corp.fr.nuxeo.labs.site.operations;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.adeo.nuxeo.features.directory.LMTestDirectoryFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.features.LabsWebAppFeature;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.DefaultRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.LabstTest;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

@RunWith(FeaturesRunner.class)
@Features( { LMTestDirectoryFeature.class, LabsWebAppFeature.class })
@Deploy({ "org.nuxeo.ecm.automation.core", "org.nuxeo.ecm.automation.features"
})
@RepositoryConfig(cleanup = Granularity.METHOD, init = DefaultRepositoryInit.class)
public class LabsSiteAdapterTest extends LabstTest {

    private final String USERNAME1 = "CGM";

    @Inject
    private CoreSession session;
    
    @Test
    public void testNewsWithStartDateTodayIsInRssFeed() throws Exception {
        DocumentModel siteDoc = session.createDocumentModel("/" + Docs.DEFAULT_DOMAIN.docName(), "NameSite1", Docs.SITE.type());
        siteDoc = session.createDocument(siteDoc);
        siteDoc.setPropertyValue("dc:title", "le titre");
        session.save();
        DocumentModel pageNewsDoc = session.createDocumentModel(session.getChild(siteDoc.getRef(), Docs.TREE.docName()).getPathAsString(), "page_news", Docs.PAGENEWS.type());
        pageNewsDoc = session.createDocument(pageNewsDoc);
        session.save();
        PageNews pageNews = pageNewsDoc.getAdapter(PageNews.class);
        pageNews.setSession(session);
        
        LabsNews news1 = pageNews.createNews("news 1");
        Calendar startDate = Calendar.getInstance();
        news1.setStartPublication(startDate);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        news1.setEndPublication(endDate);
        session.saveDocument(news1.getDocumentModel());
        session.save();
        
        LabsNews news2 = pageNews.createNews("news 2");
        startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 1);
        news2.setStartPublication(startDate);
        endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 2);
        news2.setEndPublication(endDate);
        session.saveDocument(news2.getDocumentModel());
        session.save();
        
        CoreSession userSession = changeUser(USERNAME1);
        DocumentModelList children = userSession.getChildren(new PathRef(pageNewsDoc.getPathAsString()));
        assertEquals(2, children.size());
        
        LabsSite site = siteDoc.getAdapter(LabsSite.class);
        DocumentModelList newsDocs = site.getLastPublishedNewsDocs(session);
        assertEquals(1, newsDocs.size());
    }

}
