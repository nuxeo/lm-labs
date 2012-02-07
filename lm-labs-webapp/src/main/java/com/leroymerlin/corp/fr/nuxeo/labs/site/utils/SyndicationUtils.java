package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.webengine.model.WebContext;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

public final class SyndicationUtils {

    private static final String FEED_TYPE = "rss_2.0";

    private SyndicationUtils() {
    }

    public static SyndFeed buildRss(final DocumentModelList docList,
            final String rssTitle, final String rssDesc,
            final WebContext context) throws ClientException {
        final SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType(FEED_TYPE);
        feed.setTitle(rssTitle);
        feed.setLink(StringUtils.EMPTY);
        feed.setDescription(rssDesc);
        feed.setEntries(createRssEntries(docList, context));
        return feed;
    }

    public static StreamingOutput generateStreamingOutput(final SyndFeed feed) {
        return new StreamingOutput() {
            public void write(OutputStream output) throws IOException,
                    WebApplicationException {
                try {
                    Writer writer = new PrintWriter(output);
                    SyndFeedOutput outputFeed = new SyndFeedOutput();
                    outputFeed.output(feed, writer);
                } catch (Exception e) {
                    throw new WebApplicationException(e);
                }
            }
        };
    }

    private static List<SyndEntry> createRssEntries(
            final DocumentModelList docList, final WebContext context)
            throws ClientException {
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        SyndEntry entry;

        for (DocumentModel doc : docList) {
            entry = new SyndEntryImpl();
            entry.setTitle(doc.getTitle());

            if (doc.isFolder()) {
                entry.setLink(context.getBaseURL() + context.getUrlPath(doc));
            } else {
                entry.setLink(context.getBaseURL()
                        + context.getUrlPath(context.getCoreSession().getParentDocument(
                                doc.getRef())));
            }
            entry.setPublishedDate(((GregorianCalendar) doc.getPropertyValue("dc:modified")).getTime());
            entry.setDescription(createRssDescription((String) doc.getPropertyValue("dc:description")));
            entries.add(entry);
        }
        return entries;
    }

    private static SyndContent createRssDescription(final String desc)
            throws ClientException {
        SyndContent content;
        content = new SyndContentImpl();
        content.setType("text/html");
        content.setValue(desc);
        return content;
    }
}
