package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public final class LabsSiteUtils {

    private LabsSiteUtils() {
    }

    @Deprecated
    public static final DocumentModelList getRootFolder(
            final DocumentModel site, final CoreSession session)
            throws ClientException {
        PathRef welcomeRef = new PathRef(site.getPathAsString() + "/"
                + Docs.TREE.docName() + "/" + Docs.WELCOME.docName());
        if (site == null || session == null || !session.exists(welcomeRef)) {
            return null;
        }

        return session.getChildren(welcomeRef);
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
        if (!Docs.SITE.type().equals(siteDoc.getType())) {
            throw new IllegalArgumentException("document is not a "
                    + Docs.SITE.type());
        }
        return getSitesRootPath() + "/" + siteDoc.getName() + "/"
                + Docs.TREE.docName();
    }

    public static DocumentModel getSiteTree(DocumentModel siteDoc)
            throws ClientException {
        return siteDoc.getCoreSession().getDocument(
                new PathRef(getSiteTreePath(siteDoc)));
    }

    /**
     * @param site
     * @return JSON representation of site's tree.
     * @throws ClientException if unable to retrieve children of site.
     * @throws IllegalArgumentException if site is <code>null</code>.
     */
    public static Object getSiteMap(final DocumentModel site) throws ClientException, IllegalArgumentException {
        if (site == null) {
            throw new IllegalArgumentException("site's document model can not be null.");
        }
        return site.getCoreSession().getChildren(site.getRef());
    }

    public static String getTreeview(final DocumentModel parent) throws ClientException {
        if (parent == null) {
            throw new IllegalArgumentException("document model can not be null.");
        }
        CoreSession session = parent.getCoreSession();
        DocumentModelList children = session.getChildren(parent.getRef());
        StringBuilder result = null;
        if (children != null) {
            result = new StringBuilder();

            result.append("[");
            int i = 0;
            for (DocumentModel doc : children) {
                if (i > 0) {
                    result.append(",");
                }
                result.append("{").append("\"text\":").append("\"<a href='").append(
                        doc.getPathAsString()).append("'>").append(
                        doc.getName()).append("</a>\"");
                if (session.hasChildren(doc.getRef())) {
                    result.append(",\"expanded\": true,\"children\":");
                    result.append(getTreeview(doc));
                }
                result.append("}");
                i++;
            }
            result.append("]");

        }

        return result.toString();
    }

    /**
     * @param parent
     * @return list of Page Document models.
     * @throws ClientException if unable to retrieve Page document models under <code>parent</code>.
     * @throws IllegalArgumentException if parent is null.
     */
    public static DocumentModelList getAllDoc(final DocumentModel parent) throws ClientException, IllegalArgumentException {
        if (parent == null) {
            throw new IllegalArgumentException("document model can not be null.");
        }

        return parent.getCoreSession().query("SELECT * FROM Page where ecm:path STARTSWITH '"
                + parent.getPathAsString() + "'");
    }

    public static DocumentModel getParentSite(DocumentModel doc)
            throws ClientException {
        CoreSession session = doc.getCoreSession();
        if (Docs.SITE.type().equals(doc.getType())) {
            return doc;
        }
        while (!doc.getParentRef().equals(session.getRootDocument().getRef())) {
            doc = session.getDocument(doc.getParentRef());
            if (Docs.SITE.type().equals(doc.getType())) {
                return doc;
            }
        }
        throw new IllegalArgumentException("document '" + doc.getPathAsString()
                + "' is not lacated in a site.");
    }
}
