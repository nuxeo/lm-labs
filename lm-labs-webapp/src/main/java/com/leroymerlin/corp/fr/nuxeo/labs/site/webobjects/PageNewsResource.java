/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import static org.nuxeo.ecm.webengine.WebEngine.SKIN_PATH_PREFIX_KEY;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Module;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
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
                RowTemplate.initRow(row, RowTemplate.R2COL_2575);
                row.content(0)
                        .setHtml(getSamplePictureHtml());
                row.content(1)
                        .setHtml(content);
            } else {
                RowTemplate.initRow(row, RowTemplate.R1COL);
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

        return "<img src=\"" + getSkinPath() + "/images/samplePicture.jpg\"/>";
    }

    private String getSkinPath() {
        Module module = ctx.getModule();
        HttpServletRequest request = ctx.getRequest();
        if (Framework.getProperty(SKIN_PATH_PREFIX_KEY) != null) {
            return module.getSkinPathPrefix();
        }
        String webenginePath = request.getHeader(WebContext.NUXEO_WEBENGINE_BASE_PATH);
        if (webenginePath == null) {
            return module.getSkinPathPrefix();
        } else {
            return ctx.getBasePath() + "/" + module.getName() + "/skin";
        }
    }

}
