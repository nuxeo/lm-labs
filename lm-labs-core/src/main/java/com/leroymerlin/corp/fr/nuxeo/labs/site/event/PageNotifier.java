package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.notification.MailNotification;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.EventNames;

@Deprecated
public class PageNotifier {

    private static final Log LOG = LogFactory.getLog(PageNotifier.class);

    protected void fireEvent(DocumentModel doc) throws Exception {
        DocumentEventContext ctx = new DocumentEventContext(doc.getCoreSession(), doc.getCoreSession().getPrincipal(), doc);
        ctx.setProperty("PageId", doc.getId());
        String loopbackurl = Framework.getProperty("nuxeo.loopback.url");
        ctx.setProperty("baseUrl", loopbackurl);
        ctx.setProperty("siteUrl", (Serializable) doc.getAdapter(SiteDocument.class).getSite().getURL());
        ctx.setProperty("siteTitle", (Serializable) doc.getAdapter(SiteDocument.class).getSite().getTitle());
        ctx.setProperty("pageUrl", doc.getAdapter(Page.class).getPath());
        LOG.debug("firing event " + EventNames.PAGE_MODIFIED + " for " + doc.getPathAsString());
        EventProducer evtProducer = Framework.getService(EventProducer.class);
        evtProducer.fireEvent(ctx.newEvent(EventNames.PAGE_MODIFIED));
        doc.getAdapter(MailNotification.class).setAsNotified();
        doc = doc.getCoreSession().saveDocument(doc);
        
    }

}
