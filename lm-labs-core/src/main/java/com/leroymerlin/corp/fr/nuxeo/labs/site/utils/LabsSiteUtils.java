package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;

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
        return "/default-domain/" + LabsSiteConstants.Docs.SITESROOT.docName();
    }
    
    public static String getSiteTreePath(DocumentModel doc) throws ClientException {
        LabsSite site = (LabsSite) doc.getAdapter(LabsSite.class);
        String url = site.getURL();
        if (StringUtils.isEmpty(url)) { // should never happen ...
            site.setURL(doc.getName());
            doc = doc.getCoreSession().saveDocument(doc);
            doc.getCoreSession().save();
        }
        return getSitesRootPath() + "/" + url + "/" + LabsSiteConstants.Docs.TREE.docName();
    }
    
    public static Object getSiteMap(final DocumentModel site,
            final CoreSession session) throws ClientException {
        if (site == null) {
            return null;
        }
        
        return session.getChildren(site.getRef());
    }
}
