/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.comment.api.CommentableDocument;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageListLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Entry;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntryType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Header;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.UrlType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.CommonHelper;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean.FreemarkerBean;

/**
 * @author fvandaele
 * 
 */
@WebObject(type = "PageListLine")
@Produces("text/html; charset=UTF-8")
public class PageListLineResource extends DocumentObject {
    
    private static final Log LOG = LogFactory.getLog(PageListLineResource.class);
    
    private static final String IMPOSSIBLE_TO_DELETE_LINE_ID = "Impossible to delete line id:";

    private static final String IMPOSSIBLE_TO_GET_LINE_ID = "Impossible to get line id:";

    private static final String IMPOSSIBLE_TO_SAVE_LINE = "Impossible to save line";

    private static final String DATE_FORMAT_STRING = "dd/MM/yyyy";
    
    private static final String EDIT_VIEW = "views/PageList/editLine.ftl";
    
    private PageList parent;

    @Override
    public void initialize(Object... args) {
        if (args.length != 1) {
            throw new WebException("Must give 1 args");
        }
        if (args.length > 0) {
            doc = (DocumentModel) args[0];
            if (LabsSiteConstants.Docs.PAGELIST_LINE.type().equals(doc.getType())){
                try {
                    DocumentModel docParent = doc.getCoreSession().getDocument(doc.getParentRef());
                    parent = docParent.getAdapter(PageList.class);
                } catch (ClientException e) {
                    throw WebException.wrap(
                            "Failed to get parent " + doc.getPathAsString(), e);
                }
            }
            else{
                parent = doc.getAdapter(PageList.class);
            }
        }
    }
    
    public List<DocumentModel> getComments() throws ClientException{
        return doc.getAdapter(CommentableDocument.class).getComments();
    }

    @GET
    @Override
    public Object doGet() {
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

    @Override
    public Response doDelete() {
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
    @Override
    public Response doPut() {
        FormData form = ctx.getForm();
        String value = null;
        
        Set<Header> headerSet = null;
        Entry entry = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
        EntriesLine entriesLine = new EntriesLine(); 
        try {
            if (LabsSiteConstants.Docs.PAGELIST_LINE.type().equals(doc.getType())){
                entriesLine.setDocLine(doc);
            }
            headerSet = parent.getHeaderSet();
            for (Header head : headerSet){
                value = form.getString(new Integer(head.getIdHeader()).toString());
                entry = new Entry();
                
                EntryType entryType = EntryType.valueOf(head.getType());
                if (EntryType.linePropTypes().contains(entryType)) {
                    entry.setText(entryType.xpath());
                } else {
                    switch(entryType){
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
                }
                entry.setIdHeader(head.getIdHeader());
                entriesLine.getEntries().add(entry);
            }
            String principalName = ctx.getPrincipal().getName();
            entriesLine.setUserName(principalName);
            LabsSite site = CommonHelper.siteDoc(doc).getSite();
            parent.saveLine(entriesLine, site);
        } catch (Exception e) {
            LOG.error(IMPOSSIBLE_TO_SAVE_LINE, e);
            return Response.ok("?message_error=label.pageList.line_updated_error",
                    MediaType.TEXT_PLAIN).status(Status.CREATED).build();
        }
        return Response.ok("?message_success=label.pageList.line_updated",
                MediaType.TEXT_PLAIN).status(Status.CREATED).build();
    }
    
    @Override
    public Response doPost() {
        FormData form = ctx.getForm();
        String desc = form.getString("description");
        String title = form.getString("title");
        Blob blob = form.getFirstBlob();
        try {
            blob.persist();
            if (blob.getLength() > 0) {
                doc.getAdapter(PageListLine.class).addFile(blob, desc, title);
                getCoreSession().save();
            }
        } catch (Exception e) {
            return Response.serverError()
                    .status(Status.FORBIDDEN)
                    .entity(e.getMessage())
                    .build();
        }
        String ajaxEnabled = form.getString("ajax");
        if (BooleanUtils.toBoolean(ajaxEnabled)) {
            return Response.status(Status.CREATED).build();
        }
        return redirect(prev.getPath());
    }

    public BlobHolder getBlobHolder(final DocumentModel document) {
        return document.getAdapter(BlobHolder.class);
    }
    
    @Path("@file/{filename}")
    public Object saveLine(@PathParam("filename") final String filename) throws ClientException {
        DocumentModel lineFile = getCoreSession().getChild(doc.getRef(), filename);
        return newObject("PageListLineFile", lineFile);
    }
    
    public DocumentModelList getFiles() throws ClientException {
        return doc.getAdapter(PageListLine.class).getFiles();
    }

//    private void manageAddedPermission(EntriesLine entriesLine){
//        if(entries)
//    }
}
