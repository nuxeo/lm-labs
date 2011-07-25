package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;

public final class NuxeoDocHelper {

    public static final Map<DocumentModel, DocumentModelList> getRootFolderAndChildren(
            final DocumentModel site, final CoreSession session)
            throws ClientException {
        PathRef welcomeRef = new PathRef(site.getPathAsString() + "/"
                + LabsSiteConstants.Docs.TREE.docName() + "/"
                + LabsSiteConstants.Docs.WELCOME.docName());
        if (site == null || session == null || !session.exists(welcomeRef)) {
            return null;
        }

        DocumentModelList rootList = session.getChildren(welcomeRef);
        Map<DocumentModel, DocumentModelList> rootAndChildren = new HashMap<DocumentModel, DocumentModelList>();
        if (rootList != null) {
            for (DocumentModel root : rootList) {
                DocumentModelList children = session.getChildren(root.getRef());
                rootAndChildren.put(root, children);
            }
        }

        return rootAndChildren;
    }
}
