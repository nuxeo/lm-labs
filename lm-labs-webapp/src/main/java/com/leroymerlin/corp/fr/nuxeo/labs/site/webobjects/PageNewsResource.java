/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

/**
 * @author fvandaele
 *
 */
@WebObject(type = "PageNews", superType = "Page")
@Produces("text/html; charset=UTF-8")
public class PageNewsResource extends PageResource {




    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        ctx.getEngine().getRendering().setSharedVariable("pageNews", doc.getAdapter(PageNews.class));

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


            session.saveDocument(news.getDocumentModel());
            session.save();

            return redirect(getPath()
                    + "?message_succes=label.news.news_created");
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }

    }

    @Path(value = "news")
    public Object doPostAddNews() throws ClientException {
        return newObject(LabsSiteConstants.Docs.LABSNEWS.type(), doc);
    }

    @Path("news/{idNews}")
    public Object doDeleteNews(@PathParam("idNews") final String pIdNews)
            throws ClientException {
        DocumentModel document = getCoreSession().getDocument(
                new IdRef(pIdNews));
        return newObject(LabsSiteConstants.Docs.LABSNEWS.type(), document);
    }

}
