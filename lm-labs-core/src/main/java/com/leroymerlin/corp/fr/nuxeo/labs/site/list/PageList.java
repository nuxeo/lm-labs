package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.ChangeListener;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Header;

/**
 * @author fvandaele
 *
 */
public interface PageList extends Page, ChangeListener {
    
    DocumentModel getDocument();

    /**
     * Add a header to list of header
     * @param pHead
     * @throws ClientException
     */
    void addHeader(Header pHead) throws ClientException;

    /**
     * Set the list of header
     * @param headersToSave
     * @throws ClientException
     */
    void setHeaders(List<Header> headersToSave) throws ClientException;

    /**
     * Get the set of header
     * @return the set of header
     * @throws ClientException
     */
    Set<Header> getHeaderSet() throws ClientException;
    
    /**
     * reset the list of header
     * @throws ClientException
     */
    void resetHeaders() throws ClientException;
    
    /**
     * Get a list of EntriesLines
     * @return
     * @throws ClientException
     */
    List<EntriesLine> getLines() throws ClientException;
    
    /**
     * Save a EntriesLine
     * @param pLine
     * @throws ClientException
     */
    void saveLine(EntriesLine pLine) throws ClientException;
    
    /**
     * Remove a PageListLine by this reference
     * @param pRef
     * @throws ClientException
     */
    void removeLine(DocumentRef pRef) throws ClientException;
    
    /**
     * Get a EntriesLine by this reference
     * @param pRef
     * @return
     * @throws ClientException
     */
    EntriesLine getLine(DocumentRef pRef) throws ClientException;
    
    /**
     * If all the people are contributors for manage lines.
     * @return true if all the people are contributors for manage lines else false
     * @throws ClientException
     */
    boolean isAllContributors() throws ClientException;
    
    /**
     * Set true if all the people are contributors for manage lines else false.
     * @param isAllContributors
     * @throws ClientException
     */
    void setAllContributors(boolean isAllContributors) throws ClientException;

    /**
     * If the lines are commentable
     * @return true if the lines are commentable else false
     * @throws ClientException
     */
    boolean isCommentableLines() throws ClientException;

    /**
     * Set true if the lines are commentable else false.
     * @param isAllCommentablesLines
     * @throws ClientException
     */
    void setCommentableLines(boolean isAllCommentablesLines) throws ClientException;
    
    /**
     * Export excel of the pageList's data in pOut
     * @param pOut
     * @throws IOException
     */
    void exportExcel(OutputStream pOut) throws ClientException, IOException ;
    
}
