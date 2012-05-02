package com.leroymerlin.corp.fr.nuxeo.labs.site.publisher;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

public class DefaultPublisherAdapter implements LabsPublisher {

    private final DocumentModel doc;

    public DefaultPublisherAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public void publish() throws ClientException {
        // Do nothing
    }

    @Override
    public void draft() throws ClientException {
        // Do nothing
    }

    @Override
    public boolean isVisible() throws ClientException {
        return true;
    }

    @Override
    public void delete() throws ClientException {
        // Do nothing
    }

    @Override
    public void undelete() throws ClientException {
        // Do nothing
    }

    @Override
    public boolean isDeleted() throws ClientException {
        return false;
    }

    @Override
    public boolean isDraft() throws ClientException {
        return false;
    }

}
