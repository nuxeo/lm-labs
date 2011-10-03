/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.comment.api.CommentableDocument;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageListLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Entry;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Header;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.UrlType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean.EntryType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean.FreemarkerBean;

/**
 * @author fvandaele
 * 
 */
@WebObject(type = "PageListLine")
@Produces("text/html; charset=UTF-8")
public class PageListLineResource extends DefaultObject {
    
    private static final Log LOG = LogFactory.getLog(PageListLineResource.class);
    
    private static final String IMPOSSIBLE_TO_DELETE_LINE_ID = "Impossible to delete line id:";

    private static final String IMPOSSIBLE_TO_GET_LINE_ID = "Impossible to get line id:";

    private static final String IMPOSSIBLE_TO_SAVE_LINE = "Impossible to save line";

    private static final String DATE_FORMAT_STRING = "dd/MM/yyyy";
    
    private static final String EDIT_VIEW = "views/PageList/editLine.ftl";
    
    private DocumentModel doc;
    
    private PageList parent;

    @Override
    protected void initialize(Object... args) {
        if (args.length != 2) {
            throw new WebException("Must give more 2 args");
        }

        if (args.length > 0) {
            doc = (DocumentModel) args[0];
        }
        if (args.length > 1) {
            parent = (PageList) args[1];
        }
    }
    
    public List<DocumentModel> getComments() throws ClientException{
        return doc.getAdapter(CommentableDocument.class).getComments();
    }

    @GET
    public Object get() {
        EntriesLine line = null;
        FreemarkerBean bean = null;
        try {
            PageListLine adapter = doc.getAdapter(PageListLine.class);
            bean = new FreemarkerBean(null, null, parent.getHeaderSet(), null, null);
            line = adapter.getLine();
        } catch (ClientException e) {
            LOG.error(IMPOSSIBLE_TO_GET_LINE_ID + doc.getRef().reference(), e);
        }
        return getTemplate(EDIT_VIEW).arg("line", line).arg("key", "/" + doc.getRef().reference()).arg("bean", bean);
    }

    @DELETE
    public Object delete() {
        try {
            doc.getAdapter(PageListLine.class).removeLine();
        } catch (Exception e) {
            LOG.error(IMPOSSIBLE_TO_DELETE_LINE_ID + doc.getRef().reference(), e);
            return Response.ok("?message_error=label.pageList.line_deleted_error",
                    MediaType.TEXT_PLAIN).status(Status.CREATED).build();
        }
        return Response.ok("?message_success=label.pageList.line_deleted",
                MediaType.TEXT_PLAIN).status(Status.CREATED).build();
    }

    /**
     * @param pId
     * @return
     */
    @POST
    public Object save() {
        FormData form = ctx.getForm();
        String value = null;
        
        Set<Header> headerSet = null;
        Entry entry = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
        EntriesLine entriesLine = new EntriesLine(); 
        try {
            if (LabsSiteConstants.Docs.PAGELIST_LINE.type().equals(doc.getType())){
                entriesLine.setDocRef(doc.getRef());
            }
            headerSet = parent.getHeaderSet();
            for (Header head : headerSet){
                value = form.getString(new Integer(head.getIdHeader()).toString());
                entry = new Entry();
                
                switch(EntryType.valueOf(head.getType())){
                case CHECKBOX:
                    if ("on".equalsIgnoreCase(value)){
                        entry.setCheckbox(true);
                    }
                    else{
                        entry.setCheckbox(false);
                    }
                    break;
                case DATE:
                    if (!StringUtils.isEmpty(value.trim())){
                        cal.setTimeInMillis((sdf.parse(value)).getTime());
                        entry.setDate(cal);
                    }
                    else{
                        entry.setDate(null);
                    }
                    break;
                case SELECT:
                    entry.setText(value);
                    break;
                case TEXT:
                    entry.setText(value);
                    break;
                case URL:
                    entry.setUrl(new UrlType(form.getString(head.getIdHeader() + "DisplayText"), value));
                    break;
                }
                entry.setIdHeader(head.getIdHeader());
                entriesLine.getEntries().add(entry);
            }
            parent.saveLine(entriesLine);
        } catch (Exception e) {
            LOG.error(IMPOSSIBLE_TO_SAVE_LINE, e);
            Response.ok("?message_error=label.pageList.line_updated_error",
                    MediaType.TEXT_PLAIN).status(Status.CREATED).build();
        }
        return Response.ok("?message_success=label.pageList.line_updated",
                MediaType.TEXT_PLAIN).status(Status.CREATED).build();
    }
}
