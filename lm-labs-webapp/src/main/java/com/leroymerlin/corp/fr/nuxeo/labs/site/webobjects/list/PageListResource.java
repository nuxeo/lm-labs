/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hsqldb.lib.StringUtil;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Header;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.PageResource;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean.ColSize;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean.EntryType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean.FreemarkerBean;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean.LabsFontDto;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean.LabsFontName;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list.bean.LabsFontSize;

/**
 * @author fvandaele
 * 
 */
@WebObject(type = "PageList")
@Produces("text/html; charset=UTF-8")
public class PageListResource extends PageResource {


    private static final String IMPOSSIBLE_TO_SAVE_THE_HEADERS_LIST = "Impossible to save the headers list";

    private static final String IMPOSSIBLE_TO_GET_HEADER_SET_ON_PAGE_LIST = "Impossible to getHeaderSet() on PageList";

    private static final String THE_HEADERS_WIDTHS_SHOULD_NOT_BE_EMPTY = "The headers widths should not be empty.";

    private static final String THE_HEADERS_FONTS_SHOULD_NOT_BE_EMPTY = "The headers fonts should not be empty.";

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

    public List<LabsFontDto> getHeaderFonts() {
        List<LabsFontDto> fonts = new ArrayList<LabsFontDto>();
        for (Enum<LabsFontName> fontName : LabsFontName.values()) {
            for (Enum<LabsFontSize> fontSize : LabsFontSize.values()) {
                fonts.add(new LabsFontDto(fontName, fontSize));
            }
        }
        if (fonts.isEmpty()){
            LOG.error(THE_HEADERS_FONTS_SHOULD_NOT_BE_EMPTY);
        }
        return fonts;
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
    
    public String getDefault(){
        return Header.DEFAULT;
    }
    
    public String getLineStyle(Header pHead) throws ClientException{
        StringBuilder style = new StringBuilder("style='");
        boolean hasOneStyle = false;
        if(!pHead.getWidth().equals(Header.DEFAULT)){
            style.append("width: ").append(ColSize.valueOf(pHead.getWidth()).getSize()).append("px;");
            hasOneStyle = true;
        }
        if(!pHead.getFont().equals(Header.DEFAULT)){
            style.append("font-family: \"").append(getContext().getMessage(LabsFontName.valueOf(pHead.getFontName()).getI18n())).append("\";");
            style.append("font-size: ").append(LabsFontSize.valueOf(pHead.getFontSize()).getSize()).append("px;");
            hasOneStyle = true;
        }
        if (isAuthorized()){
            style.append("cursor: pointer;");
            hasOneStyle = true;
        }
        if (hasOneStyle){
            style.append("' ");
            return style.toString();
        }
        return "";
    }
    
    public String getLineOnclick(EntriesLine pLine) throws ClientException{
        StringBuilder onclick = new StringBuilder();
        if(isAuthorized()){
            onclick.append("onclick=\"javascript:modifyLine('").append(getPath()).append("/line/").append(pLine.getDocRef().reference()).append("');\" ");
        }
        return onclick.toString();
    }
    
    public boolean isAuthorized() throws ClientException{
        if (this.allContributors == null){
            this.allContributors = false;
            if (!doc.getAdapter(PageList.class).isAllContributors()){
                this.allContributors = getCoreSession().hasPermission(doc.getRef(), "Write");
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
            @FormParam("allContributors") String pAllContributors, @FormParam("commentableLines") String pCommentableLines) {
        CoreSession session = ctx.getCoreSession();
        Gson gson = new Gson();
        try {
            Type mapType = new TypeToken<Map<String, Header>>() {}.getType();
            Map<String, Header> map = gson.fromJson(pJson, mapType);
            com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList pgl = doc.getAdapter(com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList.class);
            pgl.resetHeaders();
            int position = 0;
            SortedSet<Header> setHeaders = getSetHeaders(map);
            List<Header> headersToSave = new ArrayList<Header>();
            for (Header header : setHeaders) {              
                if (!StringUtil.isEmpty(header.getName())){
                    header.setOrderPosition(position);
                    position++;
                    header.extractElementsOfFont();
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
            return Response.ok("?message_error=label.pageList.header.headers_updated_error",
                    MediaType.TEXT_PLAIN).status(Status.CREATED).build();
        }
        return Response.ok("?message_success=label.pageList.header.headers_updated",
                MediaType.TEXT_PLAIN).status(Status.CREATED).build();
    }
    
    public boolean isAllContributors() throws ClientException{
        return doc.getAdapter(PageList.class).isAllContributors();
    }
    
    public boolean isCommentableLines() throws ClientException{
        return doc.getAdapter(PageList.class).isCommentableLines();
    }
    
    @Path("line/{id}")
    public Object saveLine(@PathParam("id") final String pId) throws ClientException {
        DocumentModel docLine = getCoreSession().getDocument(new IdRef(pId));
        return newObject(LabsSiteConstants.Docs.PAGELIST_LINE.type(), docLine, doc.getAdapter(PageList.class));
        
    }
    
    @Path(value="line")
    public Object saveLine() throws ClientException {
        return newObject(LabsSiteConstants.Docs.PAGELIST_LINE.type(), doc, doc.getAdapter(PageList.class));
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
        com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList pgl = doc.getAdapter(com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList.class);
        List<String> listHeadersName = new ArrayList<String>();
        Set<Header> headerSet = null;
        List<EntriesLine> entriesLines = null;
        try {
            int index = 0;
            headerSet = pgl.getHeaderSet();
            for (Header head : headerSet){
                head.createFont();
                String order = BASE_KEY + index;
                mapHead.put(order, head);
                headersName.append("\"").append(order).append("\",");
                listHeadersName.add(order);
                index ++;
            }
            entriesLines = pgl.getLines();     
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
        FreemarkerBean result = new FreemarkerBean(json, headersNameJS, headerSet, listHeadersName, entriesLines);
        return result;
    }
}
