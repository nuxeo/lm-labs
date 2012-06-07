package com.leroymerlin.corp.fr.nuxeo.labs.site.test;

import java.util.Calendar;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;

import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseur;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurFolder;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public class AllDocTypeRepositoryInit extends OfmRepositoryInit {

    public static final String SITE_URL = "ofm";
    public static final String SITE_TITLE = "OFM";

    public static final String PAGE_CLASSEUR_TITLE = "Page Classeur";
    public static final String FILE1_NAME = "pomodoro_cheat_sheet.pdf";
    public static final String FILE1_DESCRIPTION = "Ma Description";
    public static final String FOLDER1_NAME = "folder1";

    public static final String LABS_NEWS_TITLE = "LabsNewsTitle";
    public static final String PAGE_NEWS_TITLE = "pageNews";
    public static final String NEWS_TITLE_OF_DOC = "News1";

    public static final String PAGE_LIST_TITLE = "pageList";

    @Override
    public void populate(CoreSession session) throws ClientException {
        super.populate(session);

        String parentPath = ofm.getPathAsString() + "/"
                + LabsSiteConstants.Docs.TREE.docName();

        createTestTypes(session, parentPath);
        session.save();
    }

    protected void createTestTypes(CoreSession session, String parentPath)
            throws ClientException {
        createHtmlPage(session, parentPath);
        createPageClasseur(session, parentPath);
        createNews(session, parentPath);
        createPageList(session, parentPath);
    }

    private void createPageList(CoreSession session, String parentPath)
            throws ClientException {
        DocumentModel pageList = session.createDocumentModel(parentPath,
                PAGE_LIST_TITLE, LabsSiteConstants.Docs.PAGELIST.type());
        pageList = session.createDocument(pageList);

    }

    private void createNews(CoreSession session, String parentPath)
            throws ClientException {
        DocumentModel pageNews = session.createDocumentModel(parentPath,
                PAGE_NEWS_TITLE, LabsSiteConstants.Docs.PAGENEWS.type());
        pageNews = session.createDocument(pageNews);

        DocumentModel news = session.createDocumentModel(
                pageNews.getPathAsString(), NEWS_TITLE_OF_DOC,
                LabsSiteConstants.Docs.LABSNEWS.type());
        LabsNews newsAdapter = Tools.getAdapter(LabsNews.class, news, session);
        newsAdapter.setContent("labsNewsContent<br />Passage à la ligne");
        newsAdapter.setTitle(LABS_NEWS_TITLE);
        newsAdapter.setStartPublication(Calendar.getInstance());
        session.createDocument(news);

    }

    private void createHtmlPage(CoreSession session, String parentPath)
            throws ClientException {
        DocumentModel doc = session.createDocumentModel(parentPath,
                "htmlTestPage", "HtmlPage");
        HtmlPage page = Tools.getAdapter(HtmlPage.class, doc, session);
        page.setTitle("HTML Test page");
        page.setDescription("Page HTML de test");
        session.createDocument(page.getDocument());

    }

    private void createPageClasseur(CoreSession session, String parentPath)
            throws ClientException {
        PageClasseur classeur = new PageClasseurAdapter.Model(session,
                ofm.getPathAsString() + "/"
                        + LabsSiteConstants.Docs.TREE.docName(),
                PAGE_CLASSEUR_TITLE).create();
        classeur.setTitle(PAGE_CLASSEUR_TITLE);

        PageClasseurFolder folder = classeur.addFolder(FOLDER1_NAME, null);
        session.save();
        try {
            Blob blob = new FileBlob(
                    getClass().getResourceAsStream("/" + FILE1_NAME));
            blob.setFilename(FILE1_NAME);
            folder.addFile(blob, FILE1_DESCRIPTION, "title");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
