package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.Calendar;
import java.util.List;

import javax.ws.rs.GET;


import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.comment.api.CommentableDocument;
import org.nuxeo.ecm.platform.rating.api.RatableDocument;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "LabsNews")
public class LabsNews extends DocumentObject {

    public RatableDocument getRater() {
        return doc.getAdapter(RatableDocument.class);
    }

    public List<DocumentModel> getComments() throws ClientException {
        return doc.getAdapter(CommentableDocument.class).getComments();
    }

    public com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews getLabsNews() {
        return doc.getAdapter(com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews.class);
    }
    
    public static DocumentModel getLabsNewsByURL(String url,
            CoreSession session) throws ClientException {
        String query = "SELECT * FROM LabsNews";
        DocumentModelList docs = session.query(query);
        return docs.size() > 0 ? docs.get(0) : null;
    }


    @GET
    public Object doGet() {
//        try {
//            getNews().setNewsTemplate("middle");
//        } catch (ClientException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        try {
//            CoreSession session = this.getContext().getCoreSession();
//            doc = getLabsNewsByURL(null, session);
            getLabsNews().setStartPublication(Calendar.getInstance());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return getView("index");
    }
}
