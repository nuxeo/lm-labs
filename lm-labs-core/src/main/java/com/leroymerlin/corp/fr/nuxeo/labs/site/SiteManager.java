package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;

public interface SiteManager {
    LabsSite createSite(CoreSession session, String title, String url)
            throws ClientException, SiteManagerException;

    LabsSite getSite(CoreSession session, String url) throws ClientException,
            SiteManagerException;

    void removeSite(CoreSession session, LabsSite site) throws ClientException,
            SiteManagerException;

    Boolean siteExists(CoreSession session, String url) throws ClientException;

    List<LabsSite> getAllSites(CoreSession session) throws ClientException;
}
