/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;

/**
 * @author fvandaele
 *
 */
public class LabsSiteAdapter extends AbstractPage implements LabsSite {
    
    static final String URL = "webcontainer:url";

    public LabsSiteAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public DocumentModel getDocumentModel() {
        return doc;
    }

    @Override
    public String getURL() throws ClientException {
        return (String) doc.getPropertyValue(URL);
    }

    @Override
    public void setURL(String pURL) throws ClientException {
        doc.setPropertyValue(URL, pURL);
    }

}
