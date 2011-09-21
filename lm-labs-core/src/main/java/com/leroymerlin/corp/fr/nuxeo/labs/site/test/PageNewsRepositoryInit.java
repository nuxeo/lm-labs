package com.leroymerlin.corp.fr.nuxeo.labs.site.test;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class PageNewsRepositoryInit extends OfmRepositoryInit {

    public static final String LABS_NEWS_TITLE = "LabsNewsTitle";
    public static final String PAGE_NEWS_TITLE = "pageNews";
    public static final String NEWS_TITLE_OF_DOC = "News1";

    @Override
    public void populate(CoreSession session) throws ClientException {
        super.populate(session);

        DocumentModel ofmTree = session.getDocument(new PathRef(ofm.getPathAsString() + "/" + OfmRepositoryInit.TREE));

        DocumentModel pageNews = session.createDocumentModel(ofmTree.getPathAsString(), PAGE_NEWS_TITLE, LabsSiteConstants.Docs.PAGENEWS.type());
        pageNews = session.createDocument(pageNews);

        DocumentModel news = session.createDocumentModel(pageNews.getPathAsString(), NEWS_TITLE_OF_DOC, LabsSiteConstants.Docs.LABSNEWS.type());
        LabsNews newsAdapter = news.getAdapter(LabsNews.class);
        newsAdapter.setContent("labsNewsContent<br />Passage à la ligne");
        newsAdapter.setTitle(LABS_NEWS_TITLE);
        newsAdapter.setStartPublication(Calendar.getInstance());
        session.createDocument(news);
        session.save();

    }

}
