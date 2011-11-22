package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

public final class SyndicationUtils {

    private static final String FEED_TYPE = "rss_2.0";

    private SyndicationUtils() {
    }

    public static SyndFeed buildRss(final DocumentModelList docList,
            final String rssTitle, final String rssDesc, final String basePath)
            throws ClientException {
        final SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType(FEED_TYPE);
        feed.setTitle(rssTitle);
        // FIXME field it when IHM is developped
        feed.setLink("LIEN A DETERMINER");
        feed.setDescription(rssDesc);
        feed.setEntries(createRssEntries(docList, basePath));
        return feed;
    }

    private static List<SyndEntry> createRssEntries(
            final DocumentModelList docList, final String basePath)
            throws ClientException {
        List<SyndEntry> entries = new ArrayList<SyndEntry>();
        SyndEntry entry;
        for (DocumentModel doc : docList) {
            entry = new SyndEntryImpl();
            entry.setTitle(doc.getTitle());
            entry.setLink(basePath + "/" + doc.getName());
            entry.setPublishedDate(new Date()); // FIXME
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
