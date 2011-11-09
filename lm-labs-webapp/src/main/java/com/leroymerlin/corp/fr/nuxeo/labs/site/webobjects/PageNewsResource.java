/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import static org.nuxeo.ecm.webengine.WebEngine.SKIN_PATH_PREFIX_KEY;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.ec.notification.NotificationConstants;
import org.nuxeo.ecm.platform.notification.api.NotificationManager;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Module;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.NotifNames;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.html.RowTemplate;

/**
 * @author fvandaele
 *
 */
@WebObject(type = "PageNews", superType = "Page")
@Produces("text/html; charset=UTF-8")
public class PageNewsResource extends PageResource {

    private static final Log LOG = LogFactory.getLog(PageNewsResource.class);

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

            return redirect(getPath() + "/" + news.getDocumentModel()
                    .getName() + "/@views/edit"
                    + "?message_succes=label.news.news_created");
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }

    }

    @GET
    @Path("@subscribe")
    public Response doSubscribe() {
        LOG.debug("@subscribe");
        try {
            if (!isSubscribed()) {
                NotificationManager notificationService = Framework.getService(NotificationManager.class);
                UserManager userManager = Framework.getService(UserManager.class);
                notificationService.addSubscription(NotificationConstants.USER_PREFIX + ctx.getPrincipal().getName(), NotifNames.NEWS_PUBLISHED, doc, true, userManager.getPrincipal(ctx.getPrincipal().getName()), NotifNames.NEWS_PUBLISHED);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOG.error(e, e);
        }
        return Response.noContent().build();
    }

    @GET
    @Path("@unsubscribe")
    public Response doUnsubscribe() {
        LOG.debug("@unsubscribe");
        try {
            NotificationManager notificationService = Framework.getService(NotificationManager.class);
            notificationService.removeSubscription(NotificationConstants.USER_PREFIX + ctx.getPrincipal().getName(), NotifNames.NEWS_PUBLISHED, doc.getId());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            LOG.error(e, e);
        }
        return Response.noContent().build();
    }
    
    @GET
    @Path("@subscribed")
    public Response doSubscribed() {
        return Response.ok().entity(isSubscribed().toString()).build();
    }
    
    public Boolean isSubscribed() {
        try {
            NotificationManager notificationService = Framework.getService(NotificationManager.class);
            List<String> subscriptions = notificationService.getSubscriptionsForUserOnDocument(NotificationConstants.USER_PREFIX + ctx.getPrincipal().getName(), doc.getId());
            if (subscriptions.contains(NotifNames.NEWS_PUBLISHED)) {
                return true;
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
        return false;
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
