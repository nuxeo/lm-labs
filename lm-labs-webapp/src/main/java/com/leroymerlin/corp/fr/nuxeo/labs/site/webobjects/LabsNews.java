package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

@WebObject(type = "LabsNews")
@Produces("text/html; charset=UTF-8")
public class LabsNews extends DefaultObject {

    private static final Log log = LogFactory.getLog(LabsNews.class);
    
    private static final String DATE_FORMAT_STRING = "dd/MM/yyyy";

    private static final String EDIT_VIEW = "views/PageNews/editNews.ftl";
    
    private DocumentModel doc;

    @Override
    protected void initialize(Object... args) {
        if (args.length != 1) {
            throw new WebException("Must give 1 args");
        }

        if (args.length == 1) {
            doc = (DocumentModel) args[0];
        }
    }

    public com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews getLabsNews() {
        return doc.getAdapter(com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews.class);
    }
    public String getPathForEdit(){
        return this.getPrevious().getPath();
    }
    
    @GET
    public Object editNews(){
        Template template = getTemplate(EDIT_VIEW);
        return template;
    }

    @DELETE
    public Response deleteNews() {
        CoreSession session = ctx.getCoreSession();
        try {
            session.removeDocument(doc.getRef());
            session.save();
        } catch (ClientException e) {
            return Response.status(Status.GONE).build();
        }
        return Response.noContent().build();
    }

    @POST
    public Response doPost(
            @FormParam("newsTitle") String pTitle,
            @FormParam("newsContent") String pContent,
            @FormParam("newsId") String pNewsId,
            @FormParam("newsStartPublication") String pStartPublication,
            @FormParam("newsEndPublication") String pEndPublication,
            @FormParam("newsAccroche") String pAccroche){
        CoreSession session = ctx.getCoreSession();
        boolean isNew = isNew(pNewsId);
        try {
            DocumentModel docNews = getDocument(pTitle, pNewsId, session, isNew);   
            com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews news = docNews.getAdapter(com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews.class);
            news.setTitle(pTitle);
            news.setContent(pContent);
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
            Calendar cal = Calendar.getInstance();
            if (!StringUtils.isEmpty(pStartPublication)){
                try {
                    cal.setTimeInMillis((sdf.parse(pStartPublication)).getTime());
                    news.setStartPublication(cal);
                } catch (ParseException e) {
                    log.error(e, e);
                }
            }
            if (!StringUtils.isEmpty(pEndPublication)){
                try {
                    cal = Calendar.getInstance();
                    cal.setTimeInMillis((sdf.parse(pEndPublication)).getTime());
                    news.setEndPublication(cal);
                } catch (ParseException e) {
                    log.error(e, e);
                }
            }
            else{
                news.setEndPublication(null);
            }
            news.setAccroche(pAccroche);
            saveDocument(session, isNew, docNews);
            return redirect(this.getPrevious().getPath());
        } catch (ClientException e) {
            return Response.status(Status.GONE).build();
        }
    }

    /**
     * @param pSession
     * @param pIsNew
     * @param pDocNews
     * @throws ClientException
     */
    private void saveDocument(CoreSession pSession, boolean pIsNew,
            DocumentModel pDocNews) throws ClientException {
        if (pIsNew){
            pDocNews = pSession.createDocument(pDocNews);
        }
        else{
            pDocNews = pSession.saveDocument(pDocNews);
        }
        pSession.save();
    }

    /**
     * @param pTitle
     * @param pIdNews
     * @param pSession
     * @param pIsNew
     * @return
     * @throws ClientException
     */
    private DocumentModel getDocument(String pTitle, String pIdNews,
            CoreSession pSession, boolean pIsNew) throws ClientException {
        DocumentModel docNews;
        if (pIsNew){
            docNews = pSession.createDocumentModel(doc.getPathAsString(), pTitle,LabsSiteConstants.Docs.LABSNEWS.type());
        }
        else{
            docNews = pSession.getDocument(new IdRef(pIdNews));
        }
        return docNews;
    }

    /**
     * @param pIdNews
     * @return
     */
    private boolean isNew(String pIdNews) {
        boolean isNew = false;
        if (StringUtils.isEmpty(pIdNews)){
            isNew = true;
        }
        else {
            if ("-1".equals(pIdNews)){
                isNew = true;
            }
            else{
                isNew = false;
            }
        }
        return isNew;
    }
}
