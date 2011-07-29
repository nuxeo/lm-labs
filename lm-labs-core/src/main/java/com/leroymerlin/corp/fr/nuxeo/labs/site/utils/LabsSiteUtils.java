package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;

public final class LabsSiteUtils {

    private LabsSiteUtils() {}
    
    @Deprecated
    public static final DocumentModelList getRootFolder(
            final DocumentModel site, final CoreSession session)
            throws ClientException {
        PathRef welcomeRef = new PathRef(site.getPathAsString() + "/"
                + LabsSiteConstants.Docs.TREE.docName() + "/"
                + LabsSiteConstants.Docs.WELCOME.docName());
        if (site == null || session == null || !session.exists(welcomeRef)) {
            return null;
        }

        return session.getChildren(welcomeRef);
    }
    
    public static DocumentModel getSitesRoot(final CoreSession session) throws ClientException {
        return session.getDocument(new PathRef(getSitesRootPath()));
        
    }

    public static String getSitesRootPath() {
        return "default-domain/" + LabsSiteConstants.Docs.SITESROOT.docName();
    }
}
