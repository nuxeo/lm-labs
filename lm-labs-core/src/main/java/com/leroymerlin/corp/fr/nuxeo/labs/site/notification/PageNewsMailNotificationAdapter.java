package com.leroymerlin.corp.fr.nuxeo.labs.site.notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.news.PageNews;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.EventNames;

public class PageNewsMailNotificationAdapter extends MailNotificationAdapter {

    private static final Log LOG = LogFactory.getLog(PageNewsMailNotificationAdapter.class);

    public PageNewsMailNotificationAdapter(DocumentModel doc) {
        super(doc);
    }

    @Override
    public List<DocumentModel> getSubDocumentsToNotify() throws ClientException {
        if (subDocuments == null) {
            subDocuments = new ArrayList<DocumentModel>();
            Calendar today = Calendar.getInstance();
            PageNews pageNews = doc.getAdapter(PageNews.class);
            Collection<DocumentModel> newsStartingOn = pageNews.getTopNewsStartingOn(today);
            subDocuments = (List<DocumentModel>) CollectionUtils.select(newsStartingOn, new Predicate() {
                @Override
                public boolean evaluate(Object object) {
                    MailNotification adapter = ((DocumentModel) object).getAdapter(MailNotification.class);
                    try {
                        return adapter.isToBeNotified();
                    } catch (ClientException e) {
                        LOG.error(e, e);
                        return false;
                    }
                }}
                    );
        }
        return subDocuments;
    }

    @Override
    public void fireNotificationEvent() throws Exception {
        DocumentEventContext ctx = getEventContext();
        PageNews adapter = doc.getAdapter(PageNews.class);
        List<DocumentModel> newsToNotify = getSubDocumentsToNotify();
//        if (newsToNotify == null) {
//            newsToNotify = adapter.getNewsToNotify();
//        }
        if (newsToNotify.isEmpty()) {
            LOG.warn("No news to notify for PageNews " + doc.getPathAsString());
            return;
        }
        List<String> titles = new ArrayList<String>();
        for (DocumentModel news : newsToNotify) {
            news.getAdapter(MailNotification.class).setAsNotified();
            titles.add(news.getTitle());
        }
        ctx.setProperty("newsTitlesList", (Serializable) titles);
        LOG.debug("firing event " + EventNames.NEWS_PUBLISHED_UNDER_PAGENEWS + " for " + doc.getPathAsString());
        EventProducer evtProducer = Framework.getService(EventProducer.class);
        evtProducer.fireEvent(ctx.newEvent(EventNames.NEWS_PUBLISHED_UNDER_PAGENEWS));
        for (DocumentModel news : newsToNotify) {
            news = doc.getCoreSession().saveDocument(news);
        }
    }

}
