package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.StreamingOutput;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.LabsNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.SyndicationUtils;
import com.sun.syndication.feed.synd.SyndFeed;

@WebAdapter(name = "labsrss", type = "LabsRssAdapter", targetType = "Document")
@Produces("application/rss+xml;charset=UTF-8")
public class LabsRssAdapter extends DefaultAdapter {

    private static final String BAD_TYPE_OF_DOCUMENT = "Bad type of document for this resource!";

    private static final int TOP_NEW_MAX = 10;

    @GET
    @Produces("application/rss+xml")
    public StreamingOutput getFeed() {
        LabsSite site = (LabsSite) getContext().getProperty("site");
        String rssTitle = ctx.getMessage("label.rss.last_message.title");
        String rssDesc = ctx.getMessage("label.rss.last_message.desc");
        try {
            DocumentModelList lastUpdatedDocs = site.getLastUpdatedDocs();
            final SyndFeed feed = SyndicationUtils.buildRss(lastUpdatedDocs,
                    rssTitle, rssDesc, getContext());
            return SyndicationUtils.generateStreamingOutput(feed);
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }

    @GET
    @Path(value = "topnews")
    @Produces("application/rss+xml")
    public StreamingOutput getFeedNews() {
        try {
            DocumentModel doc = getTarget().getAdapter(DocumentModel.class);
            if (!LabsSiteConstants.Docs.PAGENEWS.type().equals(doc.getType())) {
                throw new WebResourceNotFoundException(BAD_TYPE_OF_DOCUMENT);
            }
            final PageNews pageNews = doc.getAdapter(PageNews.class);
            List<LabsNews> topNews = pageNews.getTopNews(TOP_NEW_MAX);
            final SyndFeed feed = pageNews.buildRssLabsNews(
                    topNews,
                    this.getContext().getBaseURL()
                            + this.getContext().getUrlPath(doc),
                    getContext().getMessage(
                            "label.labsNews.description.default"));
            return SyndicationUtils.generateStreamingOutput(feed);
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
    }
    
    @GET
    @Path(value = "lastNews")
    @Produces("application/rss+xml")
    public StreamingOutput getFeedLastNews() {
        LabsSite site = (LabsSite) getContext().getProperty("site");
        String rssTitle = ctx.getMessage("label.rss.last_news.title");
        String rssDesc = ctx.getMessage("label.rss.last_news.desc");
        try {
            DocumentModelList lastUpdatedNewsDocs = site.getLastUpdatedNewsDocs();
            final SyndFeed feed = SyndicationUtils.buildRss(lastUpdatedNewsDocs,
                    rssTitle, rssDesc, getContext());
            return SyndicationUtils.generateStreamingOutput(feed);
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }
    
    @GET
    @Path(value = "lastUpload")
    @Produces("application/rss+xml")
    public StreamingOutput getFeedLastUpload() {
        LabsSite site = (LabsSite) getContext().getProperty("site");
        String rssTitle = ctx.getMessage("label.rss.last_upload.title");
        String rssDesc = ctx.getMessage("label.rss.last_upload.desc");
        try {
            // FIXME
            DocumentModelList lastUpdatedNewsDocs = site.getLastUpdatedNewsDocs();
            final SyndFeed feed = SyndicationUtils.buildRss(lastUpdatedNewsDocs,
                    rssTitle, rssDesc, getContext());
            return SyndicationUtils.generateStreamingOutput(feed);
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }

    @GET
    @Path(value = "all")
    @Produces("application/rss+xml")
    public StreamingOutput getAll() {
        LabsSite site = (LabsSite) getContext().getProperty("site");
        String rssTitle = ctx.getMessage("label.rss.all.title");
        String rssDesc = ctx.getMessage("label.rss.all.desc");
        try {
            // FIXME
            DocumentModelList lastUpdatedDocs = site.getLastUpdatedDocs();
            DocumentModelList lastUpdatedNewsDocs = site.getLastUpdatedNewsDocs();

            // FIXME limite
            
            DocumentModelList allDocs = lastUpdatedDocs;
            allDocs.addAll(lastUpdatedNewsDocs);

            final SyndFeed feed = SyndicationUtils.buildRss(
                    allDocs, rssTitle, rssDesc, getContext());
            return SyndicationUtils.generateStreamingOutput(feed);
        } catch (ClientException e) {
            throw WebException.wrap(e);
        }
    }
}
