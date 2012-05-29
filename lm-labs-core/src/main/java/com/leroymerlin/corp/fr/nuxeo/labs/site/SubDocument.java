package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

public interface SubDocument {

    public abstract DocumentModel addFile(Blob blob, String description, String title, CoreSession session) throws ClientException;

    public abstract DocumentModelList getFiles(CoreSession session) throws ClientException;

    public abstract void removeFile(String title, CoreSession session) throws ClientException;

}