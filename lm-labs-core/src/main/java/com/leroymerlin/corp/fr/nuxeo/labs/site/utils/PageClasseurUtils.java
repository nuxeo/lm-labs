package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.io.IOException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.platform.filemanager.api.FileManager;
import org.nuxeo.runtime.api.Framework;

public final class PageClasseurUtils {

    private PageClasseurUtils() {}
    
    /**
     * Imports a {@link Blob} in subfolder of a PageClasseur.
     * @param classeur PageClasseur document model
     * @param parentFolderId document ID of folder in which blob will be imported
     * @param desc description to store in document model
     * @param blob
     * @return {@link DocumentModel} created from Blob.
     * @throws IllegalArgumentException blob is <code>null</code> OR folder is not a child of PageClasseur.
     * @throws IOException Unable to process blob.
     * @throws ClientException Unable to get parent folder's DocumentModel.
     * @throws Exception createDocumentFromBlob() failed.
     */
    public static DocumentModel importBlobInPageClasseur(DocumentModel classeur, String parentFolderId, String desc, Blob blob) throws Exception {
        if (blob == null) {
            throw new IllegalArgumentException("Could not find any uploaded file");
        } else {
//            blob.setFilename(blob.getFilename());
            blob.persist();
            DocumentModel folder = classeur.getCoreSession().getDocument(new IdRef(parentFolderId));
            if (!folder.getParentRef().equals(classeur.getRef())) {
                throw new IllegalArgumentException("folder is NOT a child of PageClasseur.");
            }
            DocumentModel fileDoc = Framework.getService(FileManager.class).createDocumentFromBlob(
                    classeur.getCoreSession(), blob, folder.getPathAsString(), true, StringEscapeUtils.escapeHtml(blob.getFilename()));
            if (!StringUtils.isEmpty(desc)) {
                fileDoc.setPropertyValue("dc:description", desc);
                fileDoc = classeur.getCoreSession().saveDocument(fileDoc);
            }
            return fileDoc;
        }
    }
    
}
