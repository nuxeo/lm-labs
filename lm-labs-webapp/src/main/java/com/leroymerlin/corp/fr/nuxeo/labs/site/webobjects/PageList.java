/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects;


import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Produces;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.PageListEntryType;

/**
 * @author fvandaele
 *
 */
@WebObject(type = "PageList")
@Produces("text/html; charset=UTF-8")
public class PageList extends Page {
    
    public static final String SITE_VIEW = "index";
    
    public String formatHTML(String pIn){
        if (!StringUtils.isEmpty(pIn)){
            return pIn.replaceAll("\n", "<br/>");
        }
        return pIn;
    }

    public List<Enum<PageListEntryType>> getHeaderTypes(){
        List<Enum<PageListEntryType>> result = new ArrayList<Enum<PageListEntryType>>();
        for (Enum<PageListEntryType> type:PageListEntryType.values()){
            result.add(type);
        }
        return result;
    }
    
}
