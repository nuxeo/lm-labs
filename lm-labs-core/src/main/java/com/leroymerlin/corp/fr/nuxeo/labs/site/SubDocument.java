package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

public interface SubDocument extends LabsAdapter {

    public abstract DocumentModel addFile(Blob blob, String description, String title) throws ClientException;

    public abstract DocumentModelList getFiles() throws ClientException;

    public abstract void removeFile(String title) throws ClientException;

}