/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.blocs;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;


/**
 * @author fvandaele
 *
 */
public interface ExternalURL{
    
    String getName() throws ClientException;
    
    void setName(String pName) throws ClientException;
    
    String getURL() throws ClientException;
    
    void setURL(String pURL) throws ClientException;
    
    int getOrder() throws ClientException;
    
    void setOrder(int pOrder) throws ClientException;
    
    DocumentModel getDocument();
}
