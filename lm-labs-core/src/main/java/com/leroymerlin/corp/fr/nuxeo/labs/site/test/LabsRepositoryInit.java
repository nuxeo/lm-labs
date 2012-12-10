package com.leroymerlin.corp.fr.nuxeo.labs.site.test;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.SiteManagerException;

public class LabsRepositoryInit extends DefaultRepositoryInit {
    
    @Override
    public void populate(CoreSession session) throws ClientException {
        super.populate(session);
        try {
            Framework.getService(SiteManager.class).createSite(session, SiteFeatures.SITE_NAME, SiteFeatures.SITE_NAME);
        } catch (SiteManagerException e) {
            throw new ClientException(e);
        } catch (Exception e) {
            throw new ClientException(e);
        }
    }

}
