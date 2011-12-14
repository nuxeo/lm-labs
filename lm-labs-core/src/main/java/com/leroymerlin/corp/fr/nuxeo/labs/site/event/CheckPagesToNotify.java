package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.notification.PageNotificationService;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.EventNames;
import com.mchange.v1.lang.BooleanUtils;

public class CheckPagesToNotify implements EventListener {

    private static final Log LOG = LogFactory.getLog(CheckPagesToNotify.class);
    
    private static final String PROPERTY_NAME_SERVICE_ENABLED = "labs.notification.enabled";

    @Override
    public void handleEvent(Event evt) throws ClientException {
        if (!EventNames.CHECK_PAGES_TO_NOTIFY.equals(evt.getName())) {
            return;
        }
        String enabled = Framework.getProperty(PROPERTY_NAME_SERVICE_ENABLED);
        if (enabled == null || !BooleanUtils.parseBoolean(enabled)) {
            LOG.debug("Labs Notifications disabled.");
            return;
        }
        try {
            PageNotificationService notificationService = Framework.getService(PageNotificationService.class);
            RepositoryManager repositoryManager = Framework.getService(RepositoryManager.class);
            CoreSession session = repositoryManager.getDefaultRepository().open();
            SiteManager siteManager = Framework.getService(SiteManager.class);
            List<LabsSite> sites = siteManager.getAllSites(session);
            for (LabsSite site : sites) {
                notificationService.notifyPagesOfSite(site.getDocument());
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }

    }

}
