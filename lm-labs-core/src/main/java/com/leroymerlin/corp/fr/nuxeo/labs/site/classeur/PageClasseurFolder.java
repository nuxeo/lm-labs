package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

public interface PageClasseurFolder {
    DocumentModel getDocument();

    String getTitle() throws ClientException;

    DocumentModel addFile(Blob blob, String description) throws ClientException;

    DocumentModelList getFiles() throws ClientException;

    void removeFile(String title) throws ClientException;
}
