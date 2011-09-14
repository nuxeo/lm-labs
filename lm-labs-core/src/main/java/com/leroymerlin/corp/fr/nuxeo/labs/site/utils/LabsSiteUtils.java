package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.PathRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public final class LabsSiteUtils {

    private LabsSiteUtils() {
    }

    public static DocumentModel getSitesRoot(final CoreSession session)
            throws ClientException {
        return session.getDocument(new PathRef(getSitesRootPath()));

    }

    public static String getSitesRootPath() {
        return "/" + Docs.DEFAULT_DOMAIN.docName() + "/"
                + Docs.SITESROOT.docName();
    }

    public static String getSiteTreePath(DocumentModel siteDoc)
            throws ClientException {
        if (!Docs.SITE.type()
                .equals(siteDoc.getType())) {
            throw new IllegalArgumentException("document is not a "
                    + Docs.SITE.type());
        }
        return getSitesRootPath() + "/" + siteDoc.getName() + "/"
                + Docs.TREE.docName();
    }




}
