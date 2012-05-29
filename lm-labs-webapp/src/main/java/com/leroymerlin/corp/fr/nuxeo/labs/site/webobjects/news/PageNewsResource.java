/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.news;


import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.api.PageProviderService;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.runtime.api.Framework;

import com.ibm.icu.util.Calendar;
import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.LabsBlobHolderException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNewsAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.NotifiablePageResource;

/**
 * @author fvandaele
 *
 */
@WebObject(type = "PageNews", superType = "LabsPage")
@Produces("text/html; charset=UTF-8")
public class PageNewsResource extends NotifiablePageResource {

    private static final Log log = LogFactory.getLog(PageNewsResource.class);
    
    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        ctx.getEngine()
                .getRendering()
                .setSharedVariable("pageNews", doc.getAdapter(PageNews.class));

    }

    @Override
    @POST
    public Response doPost() {
        LabsNews news = null;
        try {
            FormData form = ctx.getForm();
            String pTitle = form.getString("dc:title");
            CoreSession session = ctx.getCoreSession();
            PageNews pageNews = doc.getAdapter(PageNews.class);
            news = pageNews.createNews(pTitle, session);

            LabsNewsResource.fillNews(form, news, session);

            DocumentModel newDocNews = session.saveDocument(news.getDocumentModel());
            session.save();

            return redirect(getPath() + "/" + newDocNews.getName() + "?props=open");
        } catch (ClientException e) {
            throw WebException.wrap(e);
        } catch (IOException e) {
            throw WebException.wrap(e);
        }catch (LabsBlobHolderException e) {
            log.info("The size of blob is too small !", e);
            DocumentModel newDocNews = save(news);
            return redirect(getPath() + "/" + newDocNews.getName()
                    + "?message_success=label.labsNews.news_notupdated.size&props=open");
        }

    }
    
    private DocumentModel save(LabsNews news){
        CoreSession session = ctx.getCoreSession();
        DocumentModel newDocNews = null;
        try {
            newDocNews = session.saveDocument(news.getDocumentModel());
            session.save();
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
        return newDocNews;
    }

    public String getSamplePictureHtml() {
        return "<img src=\"" + LabsSiteWebAppUtils.getSkinPathPrefix(getModule(), getContext()) + "/images/defaultNews.jpg\"/>";
    }

    /**
     * @param pageSize the element's number by page
     * @return the pageProvider
     * @throws Exception
     */
    public PageProvider<DocumentModel> getPageNewsPageProvider(long pageSize) throws Exception {
        PageProviderService ppService = Framework.getService(PageProviderService.class);
        List<SortInfo> sortInfos = null;
        Map<String, Serializable> props = new HashMap<String, Serializable>();

        props.put(CoreQueryDocumentPageProvider.CORE_SESSION_PROPERTY,
                (Serializable) getCoreSession());
        
        String coreQueryPageProviderName = "";
        Object[] paramQuery = null;
        if (getCoreSession().hasPermission(doc.getRef(),SecurityConstants.WRITE)){
            coreQueryPageProviderName = "list_news_write_nxql";
            paramQuery = new Object[] { doc.getId() };
        }
        else{
            coreQueryPageProviderName = "list_news_nxql";
            String date_str = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            paramQuery = new Object[] { doc.getId(), date_str, date_str };
        }
        
        @SuppressWarnings("unchecked")
        PageProvider<DocumentModel> pp = (PageProvider<DocumentModel>) ppService.getPageProvider(
                coreQueryPageProviderName, sortInfos, new Long(pageSize),
                null, props, paramQuery);
        return pp;
    }
    
    public List<LabsNews> getAllNews(List<DocumentModel> docs) throws ClientException{
        List<LabsNews> result = new ArrayList<LabsNews>();
        LabsNewsAdapter adapter = null;
        for (DocumentModel document : docs){
            adapter = (LabsNewsAdapter) document.getAdapter(LabsNews.class);
            if (adapter != null){
                result.add(adapter);
            }
        }
        return result;
    }

}
