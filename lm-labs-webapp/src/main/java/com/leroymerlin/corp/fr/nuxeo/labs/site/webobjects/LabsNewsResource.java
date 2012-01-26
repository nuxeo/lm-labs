package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlRow;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;

@WebObject(type = "LabsNews", superType = "LabsPage")
@Produces("text/html; charset=UTF-8")
public class LabsNewsResource extends PageResource {

    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "dd/MM/yyyy");

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        ctx.getEngine()
                .getRendering()
                .setSharedVariable("news", getLabsNews());
    }

    public LabsNews getLabsNews() {
        return doc.getAdapter(LabsNews.class);
    }
    
    public Page getPage() throws ClientException {
        return null;
    }
    
    
    

    @POST
    @Override
    public Response doPost() {
        FormData form = ctx.getForm();
        CoreSession session = ctx.getCoreSession();
        try {
            LabsNews news = doc.getAdapter(LabsNews.class);
            fillNews(form, news);
            session.saveDocument(news.getDocumentModel());
            session.save();

            return redirect(getPath()
                    + "?message_success=label.labsNews.news_updated");
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }

    }

    @Path("s/{index}")
    public Object doGetSection() {
        // For the news there is only one section
        return newObject("HtmlSection", doc, getLabsNews());
    }

    static Calendar getDateFromStr(String strDate) {
        Calendar cal = Calendar.getInstance();
        if (!StringUtils.isEmpty(strDate)) {
            try {
                cal.setTimeInMillis((sdf.parse(strDate)).getTime());
                return cal;
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }

    static void fillNews(FormData form, LabsNews news) throws ClientException {
        String pTitle = form.getString("dc:title");
        String startDate = form.getString("newsStartPublication");
        String endDate = form.getString("newsEndPublication");
        String content = form.getString("newsContent");

        news.setTitle(pTitle);
        news.setStartPublication(getDateFromStr(startDate));
        news.setEndPublication(getDateFromStr(endDate));
        news.setContent(content);

    }

    @Override
    public Response doDelete() {
        super.doDelete();
        if (this.getPrevious() != null){
            redirect(this.getPrevious().getPath() + "?message_success=label.labsNews.news_updated");
        }
        return redirect(ctx.getBasePath());
    }
    
    public Map<String, String> getColumnLayoutsSelect() throws ClientException {
    	return HtmlRow.getColumnLayoutsSelect();
    }

}
