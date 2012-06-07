/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.api.PageProviderService;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.runtime.api.Framework;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageListLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageListLineAdapter;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntryType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Header;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.NotifiablePageResource;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean.ColSize;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean.FreemarkerBean;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean.LabsFormatDate;

/**
 * @author fvandaele
 * 
 */
@WebObject(type = "PageList", superType = "LabsPage")
@Produces("text/html; charset=UTF-8")
public class PageListResource extends NotifiablePageResource {


    private static final String THE_HEADERS_FORMAT_DATE_SHOULD_NOT_BE_EMPTY = "The headers format date should not be empty.";

    private static final String IMPOSSIBLE_TO_EXPORT_ARRAY_IN_EXCEL = "Impossible to export array in excel !";

    private static final String IMPOSSIBLE_TO_SAVE_THE_HEADERS_LIST = "Impossible to save the headers list";

    private static final String IMPOSSIBLE_TO_GET_HEADER_SET_ON_PAGE_LIST = "Impossible to getHeaderSet() on PageList";

    private static final String THE_HEADERS_WIDTHS_SHOULD_NOT_BE_EMPTY = "The headers widths should not be empty.";

    private static final String THE_HEADERS_TYPES_SHOULD_NOT_BE_EMPTY = "The headers types should not be empty.";

    private static final String BASE_KEY = "order_";
    
    private Boolean allContributors = null;
    
    private static final Log LOG = LogFactory.getLog(PageListResource.class);

    public String formatHTML(String pIn) {
        if (!StringUtils.isEmpty(pIn)) {
            return pIn.replaceAll("\n", "<br/>");
        }
        return pIn;
    }

    public List<Enum<EntryType>> getHeaderTypes() {
        List<Enum<EntryType>> entriesType = new ArrayList<Enum<EntryType>>();
        for (Enum<EntryType> type : EntryType.values()) {
            entriesType.add(type);
        }
        if (entriesType.isEmpty()){
            LOG.error(THE_HEADERS_TYPES_SHOULD_NOT_BE_EMPTY);
        }
        return entriesType;
    }

    public List<Enum<ColSize>> getHeaderWidths() {
        List<Enum<ColSize>> width = new ArrayList<Enum<ColSize>>();
        for (Enum<ColSize> type : ColSize.values()) {
            width.add(type);
        }
        if (width.isEmpty()){
            LOG.error(THE_HEADERS_WIDTHS_SHOULD_NOT_BE_EMPTY);
        }
        return width;
    }

    public List<Enum<LabsFormatDate>> getHeaderFormatDates() {
        List<Enum<LabsFormatDate>> result = new ArrayList<Enum<LabsFormatDate>>();
        for (Enum<LabsFormatDate> type : LabsFormatDate.values()) {
            result.add(type);
        }
        if (result.isEmpty()){
            LOG.error(THE_HEADERS_FORMAT_DATE_SHOULD_NOT_BE_EMPTY);
        }
        return result;
    }
    
    public String getDefault(){
        return Header.DEFAULT;
    }
    
    public String getLineStyle(Header pHead) throws ClientException{
        return getLineStyle(pHead, null);
    }
    
    // TODO move this to a FTL
    public String getLineStyle(Header pHead, EntriesLine pLine) throws ClientException{
        StringBuilder style = new StringBuilder("");
        if(!Header.DEFAULT.equals(pHead.getWidth())){
            // For the old values
            try {
                int size = ColSize.valueOf(pHead.getWidth()).getSize();
                style.append("width: ").append(size).append("px;");
            } catch (Exception e) {}
        }
        if(!Header.DEFAULT.equals(pHead.getFontName())){
            style.append("font-family: ").append(pHead.getFontName()).append(";");
        }
        if(!Header.DEFAULT.equals(pHead.getFontSize())){
            style.append("font-size: ").append(pHead.getFontSize()).append(";");
        }
        if (pLine != null && isAuthorizedToModifyLine(pLine)){
            style.append("cursor: pointer;");
        }
        return style.toString();
    }
    
    // TODO move this to a FTL
    public String getLineOnclick(EntriesLine pLine, PageProvider<DocumentModel> pp) throws ClientException{
        StringBuilder onclick = new StringBuilder();
        if(isAuthorizedToModifyLine(pLine)){
            onclick.append("onclick=\"javascript:modifyLine('").append(getPath())
            .append("/line/").append(pLine.getDocLine().getRef().reference())
            .append("', " + pp.getCurrentPageIndex() + " );\" ");
        }
        return onclick.toString();
    }
    
    private boolean isAuthorizedToModifyLine(EntriesLine pLine) throws ClientException{
        
        
        return getCoreSession().hasPermission(pLine.getDocLine().getRef(), "Write");
    }
    
    public boolean isAuthorized() throws ClientException{
        if (this.allContributors == null){
            this.allContributors = false;
            CoreSession session = getCoreSession();
            if (!Tools.getAdapter(PageList.class, doc, session).isAllContributors()){
                this.allContributors = session.hasPermission(doc.getRef(), "Write");
            }
            else{
                this.allContributors = true;
            }
        }
        return this.allContributors;
    }

    @POST
    @Path(value = "saveheaders")
    public Response saveHeaders(@FormParam("headerList") String pJson, 
            @FormParam("allContributors") String pAllContributors, @FormParam("commentableLines") String pCommentableLines, 
            @FormParam("currentPage") int currentPage) {
        CoreSession session = ctx.getCoreSession();
        Gson gson = new Gson();
        String urlCurrentPage = "";
        try {
            if (currentPage > - 1){
                urlCurrentPage = "&page=" + currentPage;
            }
            Type mapType = new TypeToken<Map<String, Header>>() {}.getType();
            Map<String, Header> map = gson.fromJson(pJson, mapType);
            com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList pgl = Tools.getAdapter(com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList.class, doc, session);
            pgl.resetHeaders();
            int position = 0;
            SortedSet<Header> setHeaders = getSetHeaders(map);
            List<Header> headersToSave = new ArrayList<Header>();
            for (Header header : setHeaders) {              
                if (!StringUtils.isEmpty(header.getName())){
                    header.setOrderPosition(position);
                    position++;
                    List<String> selectlist = header.getSelectlist();
                    if (selectlist != null && !selectlist.isEmpty()){
                        header.setSelectlist(reorganizeSelectlist(selectlist));
                    }
                    headersToSave.add(header);
                }
            }
            pgl.setAllContributors("on".equals(pAllContributors));
            pgl.setCommentableLines("on".equals(pCommentableLines));
            pgl.setHeaders(headersToSave);
            session.saveDocument(doc);
            session.save();
        } catch (ClientException e) {
            LOG.error(IMPOSSIBLE_TO_SAVE_THE_HEADERS_LIST, e);
            return Response.ok("?message_error=label.pageList.header.headers_updated_error" + urlCurrentPage,
                    MediaType.TEXT_PLAIN).status(Status.CREATED).build();
        }
        return Response.ok("?message_success=label.pageList.header.headers_updated" + urlCurrentPage,
                MediaType.TEXT_PLAIN).status(Status.CREATED).build();
    }
    
    public boolean isAllContributors() throws ClientException{
        return Tools.getAdapter(PageList.class, doc, ctx.getCoreSession()).isAllContributors();
    }
    
    public boolean isCommentableLines() throws ClientException{
        return Tools.getAdapter(PageList.class, doc, ctx.getCoreSession()).isCommentableLines();
    }
    
    @Path("line/{id}")
    public Object saveLine(@PathParam("id") final String pId) throws ClientException {
        DocumentModel docLine = getCoreSession().getDocument(new IdRef(pId));
        return newObject(LabsSiteConstants.Docs.PAGELIST_LINE.type(), docLine);
        
    }
    
    @Path(value="addline")
    public Object saveLine() throws ClientException {
        return newObject(LabsSiteConstants.Docs.PAGELIST_LINE.type(), doc);
    }

    /**
     * @param map
     * @return
     */
    private SortedSet<Header> getSetHeaders(Map<String, Header> map) {
        SortedSet<Header> setHeaders = new TreeSet<Header>();
        for (Header header : map.values()){
            if (header != null){
                setHeaders.add(header);
            }
        }
        return setHeaders;
    }

    /**
     * @param pSelectlist
     * @return
     */
    private List<String> reorganizeSelectlist(List<String> pSelectlist) {
        List<String> newSelectlist = new ArrayList<String>();
        for (String option : pSelectlist){
            if(!StringUtils.isEmpty(option)){
                newSelectlist.add(option);
            }
        }
        return newSelectlist;
    }
   
    /**
     * Get the Bean of page list
     * @return the Bean of page list
     */
    public FreemarkerBean getFreemarkerBean() {
        Map<String, Header> mapHead = new HashMap<String, Header>();
        StringBuilder headersName = new StringBuilder();
        PageList pgl = Tools.getAdapter(PageList.class, doc, ctx.getCoreSession());
        List<String> listHeadersName = new ArrayList<String>();
        Set<Header> headerSet = null;
        try {
            int index = 0;
            headerSet = pgl.getHeaderSet();
            String order = null;
            for (Header head : headerSet){
                order = BASE_KEY + index;
                mapHead.put(order, head);
                headersName.append("\"").append(order).append("\",");
                listHeadersName.add(order);
                index ++;
            }
        } catch (ClientException e) {
            LOG.error(IMPOSSIBLE_TO_GET_HEADER_SET_ON_PAGE_LIST, e);
            headerSet = new TreeSet<Header>();
        }
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Header>>() {}.getType();
        String json = gson.toJson(mapHead, mapType);
        String headersNameJS = "";
        if (headersName.length() > 0){
            headersNameJS = headersName.substring(0, headersName.length()-1);
        }
        FreemarkerBean result = new FreemarkerBean(json, headersNameJS, headerSet, listHeadersName);
        return result;
    }

    /**
     * @param pageSize the element's number by page
     * @return the pageProvider
     * @throws Exception
     */
    public PageProvider<DocumentModel> getPageListLinesPageProvider(long pageSize) throws Exception {
        PageProviderService ppService = Framework.getService(PageProviderService.class);
        List<SortInfo> sortInfos = null;
        Map<String, Serializable> props = new HashMap<String, Serializable>();

        CoreSession session = getCoreSession();
        props.put(CoreQueryDocumentPageProvider.CORE_SESSION_PROPERTY,
                (Serializable) session);
        String nxql = null;
        if (session.hasPermission(doc.getRef(), SecurityConstants.READ_WRITE)) {
        	nxql = "list_line_write_nxql";
        }
        else{
        	nxql = "list_line_nxql";
        }
        @SuppressWarnings("unchecked")
        PageProvider<DocumentModel> pp = (PageProvider<DocumentModel>) ppService.getPageProvider(
        		nxql, sortInfos, new Long(pageSize),
                null, props, new Object[] { doc.getId() });
        return pp;
    }
    
    public List<EntriesLine> getEntriesLines(List<DocumentModel> docs) throws ClientException{
        List<EntriesLine> result = new ArrayList<EntriesLine>();
        PageListLineAdapter adapter = null;
        for (DocumentModel document : docs){
            adapter = (PageListLineAdapter) Tools.getAdapter(PageListLine.class, document, ctx.getCoreSession());
            if (adapter != null){
                result.add(adapter.getLine());
            }
        }
        return result;
    }
    
    @GET
    @Path(value="exportExcel/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public StreamingOutput exportToExcel(){
        final PageList pgl = Tools.getAdapter(PageList.class, doc, ctx.getCoreSession());
        return new StreamingOutput() {
            public void write(OutputStream output) throws IOException, WebApplicationException {
                try {
                    pgl.exportExcel(output);
                    } catch (Exception e) {
                        LOG.error(IMPOSSIBLE_TO_EXPORT_ARRAY_IN_EXCEL, e);
                        throw new WebApplicationException(e);
                        }
                    }
            };
    }
}
