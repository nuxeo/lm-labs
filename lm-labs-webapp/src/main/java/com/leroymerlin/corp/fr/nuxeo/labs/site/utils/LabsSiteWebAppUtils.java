package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.webengine.model.Resource;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public final class LabsSiteWebAppUtils {

    private LabsSiteWebAppUtils() {
    }

    public static String getTreeview(final DocumentModel parent, Resource site,
            final Boolean enableBrowsingTree) throws ClientException,
            IllegalArgumentException {
        if (parent == null) {
            throw new IllegalArgumentException(
                    "document model can not be null.");
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
                result.append("{").append("\"text\":").append("\"");
                if (doc.hasSchema("page")) {
                    StringBuilder url = new StringBuilder();
                    if (site != null) { // TODO
                        url.append(site.getPath());
                    }
                    url.append(buildEndUrl(doc));
                    if (enableBrowsingTree) {
                        result.append(getHref(url.toString(), doc.getName()));
                    } else {
                        result.append(getHrefToBrowseTree(url.toString(),
                                doc.getName(), doc.getRef()));
                    }
                } else if ("Folder".equals(parent.getType())
                        && parent.getCoreSession().getDocument(
                                parent.getParentRef()).hasSchema("page")
                        && doc.getAdapter(BlobHolder.class) != null) {
                    StringBuilder url = new StringBuilder();
                    if (site != null) { // TODO
                        url.append(site.getPath());
                    }
                    url.append(buildEndUrl(doc));
                    if (enableBrowsingTree) {
                        result.append(getHref(url.toString(), doc.getName()));
                    } else {
                        result.append(getHrefToBrowseTree(url.toString(),
                                doc.getName(), doc.getRef()));
                    }
                } else if (Docs.EXTERNAL_URL.type().equals(doc.getType())) {
                    StringBuilder url = new StringBuilder();
                    url.append(buildEndUrl(doc));
                    if (enableBrowsingTree) {
                        result.append(getHref(url.toString(), doc.getName()));
                    } else {
                        result.append(getHrefToBrowseTree(url.toString(),
                                doc.getName(), doc.getRef()));
                    }
                } else {
                    result.append(doc.getName());
                }
                result.append("\"");
                if (session.hasChildren(doc.getRef())) {
                    result.append(",\"expanded\": true,\"children\":");
                    result.append(getTreeview(doc, site, enableBrowsingTree));
                }
                result.append("}");
                i++;
            }
            result.append("]");

        }

        return result.toString();
    }

    public static String getTreeview(final DocumentModel parent, Resource site)
            throws ClientException, IllegalArgumentException {
        return getTreeview(parent, site, true);
    }

    /**
     * TODO uni tests
     * 
     * @param doc
     * @return
     * @throws ClientException
     */
    public static String buildEndUrl(DocumentModel doc) throws ClientException {
        StringBuilder url = new StringBuilder();
        CoreSession session = doc.getCoreSession();
        DocumentModel parent = session.getParentDocument(doc.getRef());
        if (doc.hasSchema("page")) {
            // TODO improve
            url.append("/id/").append(doc.getId());
        } else if ("Folder".equals(parent.getType())
                && parent.getCoreSession().getDocument(parent.getParentRef()).hasSchema(
                        "page") && doc.getAdapter(BlobHolder.class) != null) {
            DocumentModel pageDoc = parent.getCoreSession().getDocument(
                    parent.getParentRef());
            url.append("/id/").append(pageDoc.getId());
            url.append("/doc/").append(doc.getId());
            url.append("/@blob/preview");
        } else if (Docs.EXTERNAL_URL.type().equals(doc.getType())) {
            url.append((String) doc.getPropertyValue("exturl:url"));
        } else {
            throw new UnsupportedOperationException(
                    "Unable to generate URL for document '"
                            + doc.getPathAsString() + "' of type "
                            + doc.getType());
        }
        return url.toString();
    }

    private static String getHref(final String url, final String text) {
        StringBuilder result = new StringBuilder();
        result.append("<a href='");
        result.append(url);
        result.append("'>").append(text).append("</a>");
        return result.toString();
    }

    public static String getHrefToBrowseTree(final String url,
            final String text, final DocumentRef ref) throws ClientException {
        StringBuilder result = new StringBuilder();
        result.append("<a id='");
        result.append(ref);
        result.append("' href='");
        result.append(url);
        result.append("' class='browseLink' onclick='addJs(this);return false;'>");
        result.append(text);
        result.append("</a>");
        return result.toString();
    }

}
