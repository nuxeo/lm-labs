package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;

public interface PageClasseur extends Page {

    public DocumentModel getDocument();

    public List<PageClasseurFolder> getFolders(CoreSession session);

    public PageClasseurFolder addFolder(String title, CoreSession session) throws ClientException;

    public void removeFolder(String title, CoreSession session) throws ClientException;

    public PageClasseurFolder getFolder(String title, CoreSession session) throws ClientException;

    public void renameFolder(String idRef, String newName, CoreSession session) throws ClientException;

}
