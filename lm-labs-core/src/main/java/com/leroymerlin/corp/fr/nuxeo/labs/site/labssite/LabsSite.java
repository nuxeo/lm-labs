/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;

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
}
