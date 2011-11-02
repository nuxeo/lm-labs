package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

@WebAdapter(name = "labsrss", type = "LabsRssAdapter", targetType = "Document")
@Produces("application/rss+xml;charset=UTF-8")
public class LabsRssAdapter extends DefaultAdapter{
    
    private static final String BAD_TYPE_OF_DOCUMENT = "Bad type of document for this resource!";
    private static final int MAX = 10;
    
    @GET
    @Path(value="topnews")
    @Produces("application/rss+xml")
    public StreamingOutput getFeed() {
        try {
            DocumentModel doc = getTarget().getAdapter(DocumentModel.class);
            if(!LabsSiteConstants.Docs.PAGENEWS.type().equals(doc.getType())){
                throw new WebResourceNotFoundException(BAD_TYPE_OF_DOCUMENT);
            }
            String feedType = "rss_2.0";

            final SyndFeed feed = new SyndFeedImpl();
            feed.setFeedType(feedType);

            PageNews pageNews = doc.getAdapter(PageNews.class);
            feed.setTitle(pageNews.getTitle());
            feed.setLink(this.getContext().getBaseURL() + this.getContext().getUrlPath(doc));
            feed.setDescription(getRssNewsDescription(pageNews));
            feed.setEntries(createEntries(pageNews));
            return new StreamingOutput() {
                public void write(OutputStream output) throws IOException, WebApplicationException {
                    try {
                        Writer writer = new PrintWriter(output);
                        SyndFeedOutput outputFeed = new SyndFeedOutput();
                        outputFeed.output(feed,writer);
                        } catch (Exception e) {
                            throw new WebApplicationException(e);
                            }
                        }
                };
        }catch (Exception e) {
            throw WebException.wrap(e);
        }
    }

    /**
     * @param pageNews
     * @return
     * @throws PropertyException
     * @throws ClientException
     */
    private String getRssNewsDescription(PageNews pageNews) throws PropertyException, ClientException {
        if (!StringUtils.isEmpty(pageNews.getDescription())){
            return pageNews.getDescription();
        }
        else{
            return getContext().getMessage("label.labsNews.description.default"); 
        }
    }

    /**
     * @param pageNews
     * @param pathBase
     * @return
     * @throws ClientException
     */
    private List<SyndEntry> createEntries(PageNews pageNews) throws ClientException {
        String pathBase = this.getContext().getBaseURL();
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        SyndEntry entry;
        List<LabsNews> topNews = pageNews.getTopNews(MAX);
        for (LabsNews news:topNews){
            entry = new SyndEntryImpl();
            entry.setTitle(news.getTitle());
            entry.setLink(pathBase + this.getContext().getUrlPath(news.getDocumentModel()));
            entry.setPublishedDate(news.getStartPublication().getTime());
            entry.setDescription(createNewsDescription(news));
            entries.add(entry);
        }
        return entries;
    }

    /**
     * @param news
     * @return
     * @throws ClientException
     */
    private SyndContent createNewsDescription(LabsNews news) throws ClientException {
        SyndContent description;
        description = new SyndContentImpl();
        description.setType("text/html");
        if (newsIsInRow(news)){
            description.setValue(news.getRows().get(0).content(0).getHtml());
        }
        else{
            description.setValue(news.getContent());
        }
        return description;
    }

    /**
     * @param news
     * @return
     */
    private boolean newsIsInRow(LabsNews news) {
        return news.getRows() != null && news.getRows().size() > 0 
                && news.getRows().get(0) != null && news.getRows().get(0).content(0) != null 
                && !StringUtils.isEmpty(news.getRows().get(0).content(0).getHtml());
    }
}
