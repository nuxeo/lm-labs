package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.Sorter;
import org.nuxeo.ecm.core.api.security.SecurityConstants;

import com.leroymerlin.common.core.utils.Slugify;
import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class PageNewsAdapter extends AbstractPage implements PageNews {

    public PageNewsAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public LabsNews createNews( String pTitle)
            throws ClientException {
        CoreSession session = doc.getCoreSession();

        String name = Slugify.slugify(pTitle);
        DocumentModel document = session
                .createDocumentModel(doc.getPathAsString(), name,
                        LabsSiteConstants.Docs.LABSNEWS.type());

        document.setPropertyValue("dc:title", pTitle);
        return session.createDocument(document).getAdapter(LabsNews.class);
    }

    @Override
    public List<LabsNews> getAllNews() throws ClientException {
      List<LabsNews> listNews = new ArrayList<LabsNews>();
      DocumentModelList listDoc = null;
      Sorter pageNewsSorter = new PageNewsSorter();
      if (getCoreSession().hasPermission(doc.getRef(),
              SecurityConstants.WRITE)) {
          listDoc = getCoreSession().getChildren(doc.getRef(),
                  LabsSiteConstants.Docs.LABSNEWS.type(), null, null,
                  pageNewsSorter);
      } else {
          listDoc = getCoreSession().getChildren(doc.getRef(),
                  LabsSiteConstants.Docs.LABSNEWS.type(), null,
                  new PageNewsFilter(Calendar.getInstance()), pageNewsSorter);
      }

      for (DocumentModel doc : listDoc) {
          LabsNews news = doc.getAdapter(LabsNews.class);
          listNews.add(news);
      }
      return listNews;
    }

    private CoreSession getCoreSession() {
        return doc.getCoreSession();
    }
}
