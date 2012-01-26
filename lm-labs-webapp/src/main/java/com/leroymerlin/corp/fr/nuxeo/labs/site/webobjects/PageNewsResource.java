/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;


import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html.RowTemplate;

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

            String model = form.getString("newsModel");
            String content = form.getString("newsContent");

            HtmlRow row = news.addRow();

            if ("2COL".equals(model)) {
                row.initTemplate(RowTemplate.R2COL_2575.value());
                row.content(0)
                        .setHtml(getSamplePictureHtml());
                row.content(1)
                        .setHtml(content);
            } else {
                row.initTemplate(RowTemplate.R1COL.value());
                row.content(0)
                        .setHtml(content);
            }

            session.saveDocument(news.getDocumentModel());
            session.save();

            return redirect(getPath());
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }

    }


    private String getSamplePictureHtml() {
        return "<img src=\"" + LabsSiteWebAppUtils.getSkinPathPrefix(getModule(), getContext()) + "/images/samplePicture.jpg\"/>";
    }

}
