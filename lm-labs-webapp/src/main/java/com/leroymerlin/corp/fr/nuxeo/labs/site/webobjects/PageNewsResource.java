/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;


import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;

/**
 * @author fvandaele
 *
 */
@WebObject(type = "PageNews", superType = "LabsPage")
@Produces("text/html; charset=UTF-8")
public class PageNewsResource extends NotifiablePageResource {

    //private static final Log LOG = LogFactory.getLog(PageNewsResource.class);
    
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
        try {
            FormData form = ctx.getForm();
            String pTitle = form.getString("dc:title");
            CoreSession session = ctx.getCoreSession();
            PageNews pageNews = doc.getAdapter(PageNews.class);
            LabsNews news = pageNews.createNews(pTitle);

            LabsNewsResource.fillNews(form, news);

            DocumentModel newDocNews = session.saveDocument(news.getDocumentModel());
            session.save();

            return redirect(getPath() + "/" + newDocNews.getName());
        } catch (ClientException e) {
            throw WebException.wrap(e);
        } catch (IOException e) {
            throw WebException.wrap(e);
        }

    }

    public String getSamplePictureHtml() {
        return "<img src=\"" + LabsSiteWebAppUtils.getSkinPathPrefix(getModule(), getContext()) + "/images/samplePicture.jpg\"/>";
    }

}
