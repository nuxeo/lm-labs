package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;

public abstract class AbstractPage implements Page {
    
    protected DocumentModel doc;

    @Override
    public void setTitle(String title) throws PropertyException, ClientException, IllegalArgumentException {
        if (title == null) {
            throw new IllegalArgumentException("title cannot be null.");
        }
        doc.setPropertyValue("dc:title", title);
    }

    @Override
    public String getTitle() throws PropertyException, ClientException {
        return (String) doc.getPropertyValue("dc:title");
    }

    @Override
    public void setDescription(String description) throws PropertyException, ClientException {
        if (description == null) {
            return;
        }
        doc.setPropertyValue("pg:commentaire", description);
    }

    @Override
    public String getDescription() throws PropertyException, ClientException {
        return (String) doc.getPropertyValue("pg:commentaire");
    }

}
