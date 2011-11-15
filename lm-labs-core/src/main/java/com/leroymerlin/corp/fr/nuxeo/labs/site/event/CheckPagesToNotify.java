package com.leroymerlin.corp.fr.nuxeo.labs.site.event;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.notification.MailNotification;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.EventNames;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.State;

public class CheckPagesToNotify implements EventListener {

    private static final Log LOG = LogFactory.getLog(CheckPagesToNotify.class);

    @Override
    public void handleEvent(Event evt) throws ClientException {
        if (!EventNames.CHECK_PAGES_TO_NOTIFY.equals(evt.getName())) {
            return;
        }
        try {
            RepositoryManager repositoryManager = Framework.getService(RepositoryManager.class);
            CoreSession session = repositoryManager.getDefaultRepository().open();
            SiteManager siteManager = Framework.getService(SiteManager.class);
            List<LabsSite> sites = siteManager.getAllSites(session);
            for (LabsSite site : sites) {
                Collection<DocumentModel> publishedPages = (List<DocumentModel>) site.getPages(null, State.PUBLISH);
                for (DocumentModel page : publishedPages) {
                    MailNotification adapter = page.getAdapter(MailNotification.class);
                    adapter.fireNotificationEvent();
                }
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }

    }

}
