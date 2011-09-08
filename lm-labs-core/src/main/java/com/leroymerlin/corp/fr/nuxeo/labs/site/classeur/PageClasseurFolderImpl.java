package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.platform.filemanager.api.FileManager;
import org.nuxeo.runtime.api.Framework;

public class PageClasseurFolderImpl implements PageClasseurFolder {

    private final DocumentModel doc;

    public PageClasseurFolderImpl(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public DocumentModel getDocument() {
        return doc;
    }

    @Override
    public String getTitle() throws ClientException {
        return doc.getTitle();
    }

    @Override
    public DocumentModel addFile(Blob blob, String description)
            throws ClientException {
        if (blob == null) {
            throw new IllegalArgumentException(
                    "Could not find any uploaded file");
        } else {
            try {
                return doAddfile(blob, description);
            } catch (Exception e) {
                throw new ClasseurException(
                        "label.classeur.error.unable_to_add_file_to_folder", e);
            }
        }
    }

    private DocumentModel doAddfile(Blob blob, String description)
            throws Exception {
        blob.persist();
        DocumentModel fileDoc = Framework.getService(FileManager.class)
                .createDocumentFromBlob(doc.getCoreSession(), blob,
                        doc.getPathAsString(), true,
                        StringEscapeUtils.escapeHtml(blob.getFilename()));
        if (!StringUtils.isEmpty(description)) {
            fileDoc.setPropertyValue("dc:description", description);
            fileDoc = doc.getCoreSession()
                    .saveDocument(fileDoc);
        }
        return fileDoc;
    }

    @Override
    public DocumentModelList getFiles() throws ClientException {

        StringBuilder sb = new StringBuilder("SELECT * From Document");
        sb.append(" WHERE ecm:parentId = '")
                .append(doc.getId())
                .append("'");
        sb.append(" AND ecm:isProxy = 0 AND ecm:isCheckedInVersion = 0");
        sb.append(" AND ecm:currentLifeCycleState <> 'deleted'");
        sb.append(" ORDER BY dc:title ASC");

        DocumentModelList list = doc.getCoreSession()
                .query(sb.toString());
        return list;

    }

    @Override
    public void removeFile(String filename) throws ClientException {
        DocumentModelList files = getFiles();
        for(DocumentModel doc : files) {
            if(filename.equals(doc.getTitle())) {
                doc.getCoreSession().removeDocument(doc.getRef());
                break;
            }
        }

    }

}
