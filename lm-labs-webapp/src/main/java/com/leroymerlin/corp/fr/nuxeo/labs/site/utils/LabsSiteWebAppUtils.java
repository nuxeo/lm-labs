package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.webengine.model.Resource;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public final class LabsSiteWebAppUtils {

    private LabsSiteWebAppUtils() {}
    
    public static String getTreeview(final DocumentModel parent, Resource site) throws ClientException, IllegalArgumentException {
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
                result.append("{").append("\"text\":").append("\"");
                if (doc.hasSchema("page")) {
                    StringBuilder url = new StringBuilder();
                    if (site != null) { // TODO
                        url.append(site.getPath());
                    }
                    url.append("/id/").append(doc.getId());
                    result.append(getHref(url.toString(), doc.getName()));
                } else if ("Folder".equals(parent.getType())
                        && parent.getCoreSession().getDocument(parent.getParentRef()).hasSchema("page")
                        && doc.getAdapter(BlobHolder.class) != null) {
                    StringBuilder url = new StringBuilder();
                    if (site != null) { // TODO
                        url.append(site.getPath());
                    }
                    DocumentModel pageDoc = parent.getCoreSession().getDocument(parent.getParentRef());
                    url.append("/id/").append(pageDoc.getId());
                    url.append("/doc/").append(doc.getId());
                    url.append("/@blob/preview");
                    result.append(getHref(url.toString(), doc.getName()));
                } else if (Docs.EXTERNAL_URL.type().equals(doc.getType())) {
                    result.append(getHref((String) doc.getPropertyValue("exturl:url"), doc.getName()));
                } else {
                    result.append(doc.getName());
                }
                result.append("\"");
                if (session.hasChildren(doc.getRef())) {
                    result.append(",\"expanded\": true,\"children\":");
                    result.append(getTreeview(doc, site));
                }
                result.append("}");
                i++;
            }
            result.append("]");

        }

        return result.toString();
    }
    
    private static String getHref(String url, String text) {
        StringBuilder result = new StringBuilder();
        result.append("<a href='");
        result.append(url);
        result.append("'>").append(text).append("</a>");
        return result.toString();
    }

}
