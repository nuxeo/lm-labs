package com.leroymerlin.corp.fr.nuxeo.labs.site.blocs;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

public class ExternalURLAdapter implements ExternalURL {
    
    static final String NAME = "exturl:name";
    static final String URL = "exturl:url";
    static final String ORDER = "exturl:order";
    
    protected DocumentModel doc;

    public ExternalURLAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public String getName() throws ClientException {
        return (String) doc.getPropertyValue(NAME);
    }

    @Override
    public void setName(String pName) throws ClientException {
        doc.setPropertyValue(NAME, pName);
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
    public int getOrder() throws ClientException {
        Serializable value = doc.getPropertyValue(ORDER);
        if (value != null && !StringUtils.isEmpty(value.toString())){
            return Integer.valueOf(value.toString()).intValue();
        }
        return -1;
    }

    @Override
    public void setOrder(int pOrder) throws ClientException {
        doc.setPropertyValue(ORDER, pOrder);
    }

    @Override
    public DocumentModel getDocument() {
        return doc;
    }
}
