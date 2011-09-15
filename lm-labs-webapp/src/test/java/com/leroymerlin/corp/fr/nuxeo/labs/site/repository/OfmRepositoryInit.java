package com.leroymerlin.corp.fr.nuxeo.labs.site.repository;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.annotations.RepositoryInit;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManagerException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;

public class OfmRepositoryInit implements RepositoryInit {

    public static final String SITE_URL = "ofm";
    public static final String SITE_TITLE = "OFM";
    public static final String TREE = "tree";

    protected DocumentModel ofm;

    @Override
    public void populate(CoreSession session) throws ClientException {
        try {
            LabsSite site = getSiteManager().createSite(session, SITE_TITLE, SITE_URL);
            ofm = site.getDocument();
            session.save();
        } catch (SiteManagerException e) {
            //Site already exists... do nothinf
        }

    }

    protected SiteManager getSiteManager()  {
        try {
            return Framework.getService(SiteManager.class);
        } catch (Exception e) {
            return null;
        }
    }

}
