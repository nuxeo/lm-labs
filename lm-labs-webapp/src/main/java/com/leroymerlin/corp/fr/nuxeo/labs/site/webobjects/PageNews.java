/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.util.ArrayList;
import java.util.Calendar;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.Sorter;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.filter.PageNewsFilter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.sort.PageNewsSorter;
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
    
    public boolean isAuthorized(){
        return ((NuxeoPrincipal)this.getContext().getPrincipal()).isAdministrator();
    }
    
    public String getPathForEdit(){
        return getPath();
    }
    
    public String formatHTML(String pIn){
        if (!StringUtils.isEmpty(pIn)){
            return pIn.replaceAll("\n", "<br/>");
        }
        return pIn;
    }

    public ArrayList<com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews> getListNews() throws ClientException {
        ArrayList<com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews> listNews = new ArrayList<com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews>();
        DocumentModelList listDoc = null;
        Sorter pageNewsSorter = new PageNewsSorter();
        boolean isAuthorized = isAuthorized();
        if (isAuthorized){
            listDoc = getCoreSession().getChildren(doc.getRef(), LabsSiteConstants.Docs.LABSNEWS.type(), null, null, pageNewsSorter);
        }
        else{
            listDoc = getCoreSession().getChildren(doc.getRef(), LabsSiteConstants.Docs.LABSNEWS.type(), null, new PageNewsFilter(Calendar.getInstance()), pageNewsSorter);
        }
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
