package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.MailNotification;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.EventNames;

public abstract class PageNewsNotifier {

    private static final Log LOG = LogFactory.getLog(PageNewsNotifier.class);

    protected void fireEvent(DocumentModel pageNews, List<DocumentModel> newsToNotify) throws Exception {
        EventProducer evtProducer = Framework.getService(EventProducer.class);
        DocumentEventContext ctx = new DocumentEventContext(pageNews.getCoreSession(), pageNews.getCoreSession().getPrincipal(), pageNews);
        ctx.setProperty("PageNewsId", pageNews.getId());
        String loopbackurl = Framework.getProperty("nuxeo.loopback.url");
        ctx.setProperty("baseUrl", loopbackurl);
        PageNews adapter = pageNews.getAdapter(PageNews.class);
        ctx.setProperty("siteUrl", (Serializable) pageNews.getAdapter(SiteDocument.class).getSite().getURL());
        ctx.setProperty("siteTitle", (Serializable) pageNews.getAdapter(SiteDocument.class).getSite().getTitle());
        List<String> titles = new ArrayList<String>();
        if (newsToNotify == null) {
            newsToNotify = adapter.getNewsToNotify();
        }
        if (newsToNotify.isEmpty()) {
            LOG.warn("No news to notify for PageNews " + pageNews.getPathAsString());
            return;
        }
        for (DocumentModel news : newsToNotify) {
            news.getAdapter(MailNotification.class).setAsNotified();
            titles.add(news.getTitle());
        }
        ctx.setProperty("newsTitlesList", (Serializable) titles);
        ctx.setProperty("pageNewsUrl", adapter.getPath());
        evtProducer.fireEvent(ctx.newEvent(EventNames.NEWS_PUBLISHED_UNDER_PAGENEWS));
        for (DocumentModel news : newsToNotify) {
            news = pageNews.getCoreSession().saveDocument(news);
        }
    }

}
