/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.ArrayList;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

/**
 * @author fvandaele
 *
 */
@WebObject(type = "PageNews")
@Produces("text/html; charset=UTF-8")
public class PageNews extends DocumentObject {
    
    public static final String SITE_VIEW = "index";

    public com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews getLabsNews() {
        return null;
    }
    
    public String getPathForEdit(){
        return getPath();
    }

    public ArrayList<com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews> getListNews() throws ClientException {
        ArrayList<com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews> listNews = new ArrayList<com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews>();
        DocumentModelList listDoc = getCoreSession().getChildren(doc.getRef(), LabsSiteConstants.Docs.LABSNEWS.type());        
        for (DocumentModel doc:listDoc){
            com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews news = doc.getAdapter(com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews.class);
            listNews.add(news);
        }
        return listNews;
    }

    @Path(value="persistNews")
    public Object doPostAddNews() throws ClientException {
        return newObject(LabsSiteConstants.Docs.LABSNEWS.type(), doc);
    }

    @Path("delete/{idNews}")
    public Object doDeleteNews(@PathParam("idNews") final String pIdNews) throws ClientException {
        DocumentModel document = getCoreSession().getDocument(new IdRef(pIdNews));
        return newObject(LabsSiteConstants.Docs.LABSNEWS.type(), document);
    }

    @Path("edit/{idNews}")
    public Object doEditNews(@PathParam("idNews") final String pIdNews) throws ClientException {
        DocumentModel document = getCoreSession().getDocument(new IdRef(pIdNews));
        return newObject(LabsSiteConstants.Docs.LABSNEWS.type(), document);
    }
    
}
