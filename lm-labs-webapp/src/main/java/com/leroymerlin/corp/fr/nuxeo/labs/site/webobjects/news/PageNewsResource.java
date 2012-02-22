/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.news;


import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.LabsBlobHolderException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
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
            news = pageNews.createNews(pTitle);

            LabsNewsResource.fillNews(form, news);

            DocumentModel newDocNews = session.saveDocument(news.getDocumentModel());
            session.save();

            return redirect(getPath() + "/" + newDocNews.getName());
        } catch (ClientException e) {
            throw WebException.wrap(e);
        } catch (IOException e) {
            throw WebException.wrap(e);
        }catch (LabsBlobHolderException e) {
            log.info("The size of blob is too small !", e);
            DocumentModel newDocNews = save(news);
            return redirect(getPath() + "/" + newDocNews.getName()
                    + "?message_success=label.labsNews.news_notupdated.size");
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

}
