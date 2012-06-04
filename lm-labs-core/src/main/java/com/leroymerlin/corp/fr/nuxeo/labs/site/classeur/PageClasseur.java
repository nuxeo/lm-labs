package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;

public interface PageClasseur extends Page {

    public DocumentModel getDocument();

    public List<PageClasseurFolder> getFolders();

    public PageClasseurFolder addFolder(String title) throws ClientException;

    public void removeFolder(String title) throws ClientException;

    public PageClasseurFolder getFolder(String title) throws ClientException;

    public void renameFolder(String idRef, String newName) throws ClientException;

}
