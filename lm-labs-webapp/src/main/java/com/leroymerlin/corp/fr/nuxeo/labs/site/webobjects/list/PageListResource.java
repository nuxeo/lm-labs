/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.list;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
import org.nuxeo.ecm.platform.rendering.api.RenderingEngine;
import org.nuxeo.ecm.platform.rendering.fm.FreemarkerEngine;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageListLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Entry;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Header;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.UrlType;
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
    
    private static final String IMPOSSIBLE_TO_DELETE_LINE_ID = "Impossible to delete line id:";

    private static final String IMPOSSIBLE_TO_GET_LINE_ID = "Impossible to get line id:";

    private static final String IMPOSSIBLE_TO_SAVE_LINE = "Impossible to save line";

    private static final String IMPOSSIBLE_TO_SAVE_THE_HEADERS_LIST = "Impossible to save the headers list";

    private static final String IMPOSSIBLE_TO_GET_HEADER_SET_ON_PAGE_LIST = "Impossible to getHeaderSet() on PageList";

    private static final String THE_HEADERS_WIDTHS_SHOULD_NOT_BE_EMPTY = "The headers widths should not be empty.";

    private static final String THE_HEADERS_FONTS_SHOULD_NOT_BE_EMPTY = "The headers fonts should not be empty.";

    private static final String THE_HEADERS_TYPES_SHOULD_NOT_BE_EMPTY = "The headers types should not be empty.";

    private static final String BASE_KEY = "order_";

    private static final Log LOG = LogFactory.getLog(PageListResource.class);

    private static final String DATE_FORMAT_STRING = "dd/MM/yyyy";
    
    
    /*
     * (non-Javadoc)
     *
     * @see
     * org.nuxeo.ecm.webengine.model.impl.AbstractResource#initialize(java.lang
     * .Object[])
     */
    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        // Add global fm variables
        // WARNING : these are GLOBAL vars, try to avoid using this trick (DMR)
        RenderingEngine rendering = getContext().getEngine().getRendering();
        if (rendering instanceof FreemarkerEngine) {
            FreemarkerEngine fm = (FreemarkerEngine) rendering;
            FreemarkerBean objectFreemarker = getFreemarkerBean();
            fm.setSharedVariable("headersMapJS", objectFreemarker.getHeadersMapJS());
            fm.setSharedVariable("headersNameJS", objectFreemarker.getHeadersNameJS());
            fm.setSharedVariable("headersSet", objectFreemarker.getHeadersSet());
            fm.setSharedVariable("headersNameList", objectFreemarker.getHeadersNameList());
            fm.setSharedVariable("entriesLines", objectFreemarker.getEntriesLines());
            fm.setSharedVariable("line", null);
            fm.setSharedVariable("key", "");
        }
    }

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

    @POST
    @Path(value = "saveheaders")
    public Response saveHeaders(@FormParam("headerList") String pJson) {
        CoreSession session = ctx.getCoreSession();
        Gson gson = new Gson();
        try {
            Type mapType = new TypeToken<Map<String, Header>>() {}.getType();
            Map<String, Header> map = gson.fromJson(pJson, mapType);
            com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList pgl = doc.getAdapter(com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList.class);
            pgl.resetHeaders();
            int position = 0;
            SortedSet<Header> setHeaders = getSetHeaders(map);
            for (Header header : setHeaders) {              
                if (!StringUtil.isEmpty(header.getName())){
                    header.setOrderPosition(position);
                    position++;
                    header.extractElementsOfFont();
                    List<String> selectlist = header.getSelectlist();
                    if (selectlist != null && !selectlist.isEmpty()){
                        header.setSelectlist(reorganizeSelectlist(selectlist));
                    }
                    pgl.addHeader(header);
                }
            }
            session.saveDocument(doc);
            session.save();
        } catch (ClientException e) {
            LOG.error(IMPOSSIBLE_TO_SAVE_THE_HEADERS_LIST, e);
            return Response.status(Status.GONE).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path(value = "line/{id}")
    public Object getLine(@PathParam("id") final String pId) {
        EntriesLine line = null;
        try {
            DocumentModel docLine = getCoreSession().getDocument(new IdRef(pId));
            PageListLine adapter = docLine.getAdapter(PageListLine.class);
            line = adapter.getLine();
        } catch (ClientException e) {
            LOG.error(IMPOSSIBLE_TO_GET_LINE_ID + pId, e);
        }
        return getView("editLine").arg("line", line).arg("key", "/" + pId);
    }

    @DELETE
    @Path(value = "saveline/{id}")
    public Object deleteLine(@PathParam("id") final String pId) {
        try {
            doc.getAdapter(PageList.class).removeLine(new IdRef(pId));
        } catch (Exception e) {
            LOG.error(IMPOSSIBLE_TO_DELETE_LINE_ID + pId, e);
        }
        return redirect(this.getPath());
    }

    @POST
    @Path(value = "saveline/{id}")
    public Object saveLine(@PathParam("id") final String pId) {
        return persistLine(pId);
    }

    @POST
    @Path(value = "saveline")
    public Object saveLine() {
        return persistLine(null);
    }

    /**
     * @param pId
     * @return
     */
    private Object persistLine(final String pId) {
        FormData form = ctx.getForm();
        String value = null;
        
        com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList pgl = doc.getAdapter(com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList.class);
        Set<Header> headerSet = null;
        Entry entry = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
        EntriesLine entriesLine = new EntriesLine(); 
        try {
            if (!StringUtil.isEmpty(pId)){
                entriesLine.setDocRef(new IdRef(pId));
            }
            headerSet = pgl.getHeaderSet();
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
                LOG.debug(value);
            }
            pgl.saveLine(entriesLine);
        } catch (Exception e) {
            LOG.error(IMPOSSIBLE_TO_SAVE_LINE, e);
            return Response.status(Status.PRECONDITION_FAILED).build();
        }
        return redirect(this.getPath());
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
    private FreemarkerBean getFreemarkerBean() {
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

   /* private List<EntriesLine> sortEntriesLine(Collection<EntriesLine> lines, Set<Header> headerSet) {
        List<EntriesLine> result = new ArrayList<EntriesLine>(lines.size());
        Map<Integer, Entry> mapByIdHead = null;
        EntriesLine entriesLine = null;
        for (EntriesLine map: lines){
            mapByIdHead = buildMapByIdHead(map);
            entriesLine = new EntriesLine();
            for (Header head:headerSet){
                entriesLine.getEntries().add(mapByIdHead.get(new Integer(head.getIdHeader())));
            }
            result.add(entriesLine);
        }
        return result;
    }

    private Map<Integer, Entry> buildMapByIdHead(EntriesLine map) {
        Map<Integer, Entry> mapByIdHead = new HashMap<Integer, Entry>();
        for (Entry entry:map.getEntries()){
            mapByIdHead.put(new Integer(entry.getIdHeader()), entry);
        }
        return mapByIdHead;
    }*/

}
