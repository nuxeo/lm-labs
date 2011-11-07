package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.EventNames;

public abstract class PageNewsNotifier {

    protected void fireEvent(DocumentModel pageNews) throws Exception {
        EventProducer evtProducer = Framework.getService(EventProducer.class);
        DocumentEventContext ctx = new DocumentEventContext(pageNews.getCoreSession(), pageNews.getCoreSession().getPrincipal(), pageNews);
        evtProducer.fireEvent(ctx.newEvent(EventNames.NEWS_PUBLISHED_UNDER_PAGENEWS));
    }

}
