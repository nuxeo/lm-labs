/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.labssite;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;

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

    @Override
    public void setDescription(String description) throws PropertyException, ClientException {
        if (description == null) {
            return;
        }
        doc.setPropertyValue("dc:description", description);
    }

    @Override
    public String getDescription() throws PropertyException, ClientException {
        return (String) doc.getPropertyValue("dc:description");
    }

}
