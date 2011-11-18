package com.leroymerlin.corp.fr.nuxeo.labs.site.publisher;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class LabsPublisherAdapter implements LabsPublisher {

    private final DocumentModel doc;

    public LabsPublisherAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    @Override
    public void publish() throws ClientException{
        doc.followTransition(LabsSiteConstants.State.PUBLISH.getTransition());
    }
    
    @Override
    public void draft() throws ClientException{
        doc.followTransition(LabsSiteConstants.State.DRAFT.getTransition());
    }
    
    @Override
    public boolean isVisible() throws ClientException{
        return LabsSiteConstants.State.PUBLISH.getState().equals(doc.getCurrentLifeCycleState());
    }

    @Override
    public void delete() throws ClientException{
        doc.followTransition(LabsSiteConstants.State.DELETE.getTransition());
    }

    @Override
    public void undelete() throws ClientException{
        doc.followTransition(LabsSiteConstants.State.UNDELETE.getTransition());
    }
    
    @Override
    public boolean isDeleted() throws ClientException{
        return LabsSiteConstants.State.DELETE.getState().equals(doc.getCurrentLifeCycleState());
    }

}
