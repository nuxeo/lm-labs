/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

import com.leroymerlin.corp.fr.nuxeo.labs.site.LabsBase;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.blocs.ExternalURL;
import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteThemeManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.State;

/**
 * @author fvandaele
 *
 */
public interface LabsSite  extends LabsBase {

    /**
     * The last url part to get this site. This MUST be
     * unique amongst all sites
     * @return
     * @throws ClientException
     */
    String getURL() throws ClientException;

    /**
     * Set the url of the site. It SHOULD throw an exception
     * if the URL is not unique
     * @param pURL
     * @throws ClientException
     */
    void setURL(String pURL) throws ClientException;

    /**
     * Returns a Blob containing the banner of the site
     * @return
     * @throws ClientException
     */
    Blob getBanner() throws ClientException;

    /**
     * @param pBlob
     * @param session
     * @throws ClientException
     */
    void setBanner(Blob pBlob, CoreSession session) throws ClientException;


    DocumentModel getIndexDocument(CoreSession session) throws ClientException;


    /**
     * Returns all the page of the site. Pages are
     * document that are renderable with a web
     * view
     * @return
     * @throws ClientException
     */
    List<Page> getAllPages(CoreSession session) throws ClientException;
    
    /**
     * @param docType
     * @param llifecycleState can be <code>null</code>.
     * @param session
     * @return
     * @throws ClientException
     */
    Collection<DocumentModel> getPages(Docs docType, State lifecycleState, CoreSession session) throws ClientException;

    /**
     * Returns the base document of the tree
     *
     * @return
     * @throws ClientException
     */
    DocumentModel getTree(CoreSession session) throws ClientException;

    /**
     * Return the theme manager for this site
     * @return
     * @throws ClientException
     */
    SiteThemeManager getThemeManager() throws ClientException;

    /**
     * Returns the base document for assets management
     * @return
     */
    DocumentModel getAssetsDoc(CoreSession session) throws ClientException;

    List<Page> getAllDeletedPages(CoreSession session) throws ClientException;

    DocumentModelList getAllDeletedDocs(CoreSession session) throws ClientException;

    void setHomePageRef(String homePageRef) throws ClientException;

    String getHomePageRef() throws ClientException;

    DocumentModelList getLastUpdatedDocs(CoreSession session) throws ClientException;
    
    DocumentModelList getLastUpdatedNewsDocs(CoreSession session) throws ClientException;

    ArrayList<ExternalURL> getExternalURLs(CoreSession session) throws ClientException;
    
    ExternalURL createExternalURL(String title, CoreSession session) throws ClientException;

    boolean updateUrls(String oldUrl, String newUrl, CoreSession session) throws ClientException;

    List<String> getAdministratorsSite() throws Exception;

    List<String> getContacts() throws Exception;
    
    boolean addContact(String ldap) throws Exception;
    
    boolean deleteContact(String ldap) throws Exception;
    
    String getPiwikId() throws ClientException;
    
    void setPiwikId(String piwikId) throws ClientException;
    
    boolean isPiwikEnabled() throws ClientException;

    void setSiteTemplate(boolean value) throws ClientException;
    
    boolean isSiteTemplate() throws ClientException;

    Blob getSiteTemplatePreview() throws ClientException;
    
    boolean hasSiteTemplatePreview() throws ClientException;
    
    void setSiteTemplatePreview(Blob blob) throws ClientException;

    void applyTemplateSite(final DocumentModel templateSite, CoreSession session) throws ClientException, IllegalArgumentException;
    
    void setThemeName(String name) throws ClientException;
    
    String getThemeName() throws ClientException;
    
    List<Page> getSubscribedPages(CoreSession session) throws ClientException;
}
