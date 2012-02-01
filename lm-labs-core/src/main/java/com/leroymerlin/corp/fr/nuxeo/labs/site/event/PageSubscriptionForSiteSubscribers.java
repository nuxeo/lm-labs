package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import java.security.Principal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.platform.ec.notification.NotificationConstants;
import org.nuxeo.ecm.platform.notification.api.NotificationManager;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.notification.PageSubscription;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageSubscriptionForSiteSubscribers implements EventListener {

    private static final Log LOG = LogFactory.getLog(PageSubscriptionForSiteSubscribers.class);

    @Override
    public void handleEvent(Event evt) throws ClientException {
        if (!(evt.getContext() instanceof DocumentEventContext)) {
            return;
        }
        String eventName = evt.getName();
        if (!DocumentEventTypes.DOCUMENT_CREATED.equals(eventName)
                && !DocumentEventTypes.DOCUMENT_CREATED_BY_COPY.equals(eventName)
                ) {
            return;
        }
        DocumentEventContext ctx = (DocumentEventContext) evt.getContext();
        DocumentModel doc = ctx.getSourceDocument();
        if (doc != null) {
            if (!Docs.notifiableDocs().contains(Docs.fromString(doc.getType()))) {
                return;
            }
            if (Docs.SITE.type().equals(doc.getType())) {
                return;
            }
            Page page = doc.getAdapter(Page.class);
            if (page == null) {
                return;
            }
            LOG.debug(eventName + " " + doc.getPathAsString());
            try {
                String notifNameOnSite = LabsSiteConstants.NotifNames.PAGE_MODIFIED;
                NotificationManager notificationService = Framework.getService(NotificationManager.class);
                UserManager userManager = Framework.getService(UserManager.class);
                List<String> siteSubscribers = notificationService.getUsersSubscribedToNotificationOnDocument(notifNameOnSite, doc.getAdapter(SiteDocument.class).getSite().getDocument().getId());
                PageSubscription subscriptionAdapter = doc.getAdapter(PageSubscription.class);
                for (String user : siteSubscribers) {
                    Principal princ = userManager.getPrincipal(StringUtils.removeStart(user, NotificationConstants.USER_PREFIX));
                    if (ctx.getCoreSession().hasPermission(princ, doc.getRef(), SecurityConstants.READ)) {
                        subscriptionAdapter.subscribe(princ.getName());
                    }
                }
            } catch (Exception e) {
                LOG.error(e, e);
            }
        }
    }
}
