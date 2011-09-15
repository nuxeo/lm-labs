/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;

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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hsqldb.lib.StringUtil;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.dto.Header;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.HeadersCollectionDto;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsFontDto;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsFontName;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsFontSize;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.PageListColSize;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.PageListEntryType;

/**
 * @author fvandaele
 * 
 */
@WebObject(type = "PageList")
@Produces("text/html; charset=UTF-8")
public class PageList extends Page {
    
    private static final String IMPOSSIBLE_TO_SAVE_THE_HEADERS_LIST = "Impossible to save the headers list";

    private static final String IMPOSSIBLE_TO_GET_HEADER_SET_ON_PAGE_LIST = "Impossible to getHeaderSet() on PageList";

    private static final String THE_HEADERS_WIDTHS_SHOULD_NOT_BE_EMPTY = "The headers widths should not be empty.";

    private static final String THE_HEADERS_FONTS_SHOULD_NOT_BE_EMPTY = "The headers fonts should not be empty.";

    private static final String THE_HEADERS_TYPES_SHOULD_NOT_BE_EMPTY = "The headers types should not be empty.";

    private static final String BASE_KEY = "order_";

    private static final Log LOG = LogFactory.getLog(PageList.class);

    public static final String SITE_VIEW = "index";

    public String formatHTML(String pIn) {
        if (!StringUtils.isEmpty(pIn)) {
            return pIn.replaceAll("\n", "<br/>");
        }
        return pIn;
    }

    public List<Enum<PageListEntryType>> getHeaderTypes() {
        List<Enum<PageListEntryType>> entriesType = new ArrayList<Enum<PageListEntryType>>();
        for (Enum<PageListEntryType> type : PageListEntryType.values()) {
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

    public List<Enum<PageListColSize>> getHeaderWidths() {
        List<Enum<PageListColSize>> width = new ArrayList<Enum<PageListColSize>>();
        for (Enum<PageListColSize> type : PageListColSize.values()) {
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
            SortedSet<Header> setHeaders = new TreeSet<Header>();
            setHeaders.addAll(map.values());
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
     * Get the DTO of headers list
     * @return the DTO of headers list
     */
    public HeadersCollectionDto getHeaders() {
        Map<String, Header> mapHead = new HashMap<String, Header>();
        StringBuilder headersName = new StringBuilder();
        com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList pgl = doc.getAdapter(com.leroymerlin.corp.fr.nuxeo.labs.site.list.PageList.class);
        List<String> listHeadersName = new ArrayList<String>();
        Set<Header> headerSet = null;
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
            
        } catch (ClientException e) {
            LOG.error(IMPOSSIBLE_TO_GET_HEADER_SET_ON_PAGE_LIST, e);
            headerSet = new TreeSet<Header>();
        }
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Header>>() {}.getType();
        String json = gson.toJson(mapHead, mapType);
        HeadersCollectionDto result = new HeadersCollectionDto(json, headersName.substring(0, headersName.length()-1), headerSet, listHeadersName);
        return result;
    }

}
