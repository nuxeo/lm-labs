package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;

public class NewsTools {
    
    public static SyndContent createRssNewsDescription(LabsNews news) throws ClientException {
        SyndContent description;
        description = new SyndContentImpl();
        description.setType("text/html");
        if(!StringUtils.isEmpty(news.getAccroche())){
            description.setValue(news.getAccroche());
        }
        else if (contentNewsIsInRow(news)){
            description.setValue(news.getRows().get(0).content(0).getHtml());
        }
        else{
            description.setValue(news.getContent());
        }
        return description;
    }
    
    private static boolean contentNewsIsInRow(LabsNews news) {
        return news.getRows() != null && news.getRows().size() > 0 
                && news.getRows().get(0) != null && news.getRows().get(0).content(0) != null 
                && !StringUtils.isEmpty(news.getRows().get(0).content(0).getHtml());
    }
}
