/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import java.util.List;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteTheme;

/**
 * @author fvandaele
 *
 */
public interface LabsSite extends Page {

    String getURL() throws ClientException;

    void setURL(String pURL) throws ClientException;

    DocumentModel getDocumentModel();

    Blob getLogo() throws ClientException;

    void setLogo(Blob pBlob) throws ClientException;


    List<Page> getAllPages() throws ClientException;

    /**
     * Returns the base document of the tree
     *
     * @return
     * @throws ClientException
     */
    DocumentModel getTree() throws ClientException;


    /**
     * Returns a list of available themes for this site
     * @return
     * @throws ClientException
     */
    List<SiteTheme> getThemes() throws ClientException;

    /**
     * Returns a named theme of the site
     * @param themeName
     * @return
     * @throws ClientException
     */
    SiteTheme getTheme(String themeName) throws ClientException;

    /**
     * Returns the current theme of the site
     * @return
     * @throws ClientException
     */
    SiteTheme getTheme() throws ClientException;

    /**
     * Sets the current theme
     * @param themeName
     * @throws ClientException
     */
    void setTheme(String themeName) throws ClientException;
}
