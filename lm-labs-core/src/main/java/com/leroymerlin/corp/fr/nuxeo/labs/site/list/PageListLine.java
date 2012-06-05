package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SubDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;


public interface PageListLine extends SubDocument {

    /**
     * Set a line with a EntriesLine
     * @param pLine
     * @throws ClientException
     */
    void setLine(EntriesLine pLine) throws ClientException;
    
    /**
     * Get a Entriesline
     * @return a Entriesline
     * @throws ClientException
     */
    EntriesLine getLine() throws ClientException;
    
    /**
     * Remove the current line
     * @throws ClientException
     */
    void removeLine() throws ClientException;
    
    /**
     * Returns the comments on the line
     * @return
     * @throws ClientException
     */
    List<DocumentModel> getComments() throws ClientException;

    /**
     * Set the comment's number
     * @param nbComments
     */
    void setNbComments(int nbComments) throws ClientException;

    /**
     * Get the comment's number
     * @return
     */
    int getNbComments() throws ClientException;
    
    void addComment() throws ClientException;
    
    void removeComment() throws ClientException;
    
    void hide() throws ClientException;
    
    void show() throws ClientException;
    
    boolean isVisible() throws ClientException;
    
}
