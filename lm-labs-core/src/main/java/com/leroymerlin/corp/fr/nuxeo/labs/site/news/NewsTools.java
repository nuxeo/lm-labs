package com.leroymerlin.corp.fr.nuxeo.labs.site.news;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;

public class NewsTools {
    
    public static SyndContent createRssNewsDescription(LabsNews news, CoreSession session) throws ClientException {
        SyndContent description;
        description = new SyndContentImpl();
        description.setType("text/html");
        if(!StringUtils.isEmpty(news.getAccroche())){
            description.setValue(news.getAccroche());
        }
        else if (contentNewsIsInRow(news, session)){
            description.setValue(news.getRows(session).get(0).content(0).getHtml());
        }
        else{
            description.setValue(news.getContent());
        }
        return description;
    }
    
    private static boolean contentNewsIsInRow(LabsNews news, CoreSession session) {
        return news.getRows(session) != null && news.getRows(session).size() > 0 
                && news.getRows(session).get(0) != null && news.getRows(session).get(0).content(0) != null 
                && !StringUtils.isEmpty(news.getRows(session).get(0).content(0).getHtml());
    }
}
