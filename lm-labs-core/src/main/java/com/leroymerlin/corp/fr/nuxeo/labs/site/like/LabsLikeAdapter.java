package com.leroymerlin.corp.fr.nuxeo.labs.site.like;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.rating.api.LikeService;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.base.LabsLike;

public class LabsLikeAdapter implements LabsLike {

    private static final Log LOG = LogFactory.getLog(LabsLikeAdapter.class);
    
    private static final List<String> EMPTY_USERS_LIST = new ArrayList<String>();

    public LabsLikeAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    protected DocumentModel doc;
    
    @Override
    public boolean isLiked(String username) throws ClientException {
        try {
            return getService().hasUserLiked(username, doc);
        } catch (Exception e) {
            throw ClientException.wrap(e);
        }
    }

    @Override
    public LabsLike like(String username) throws ClientException {
        try {
            getService().like(username, doc);
        } catch (Exception e) {
            throw ClientException.wrap(e);
        }
        return this;
    }

    @Override
    public List<String> getUsersLiked() throws ClientException {
        // TODO
        return EMPTY_USERS_LIST;
    }

    @Override
    public long getLikesCount() throws ClientException {
        try {
            return getService().getLikesCount(doc);
        } catch (Exception e) {
            throw ClientException.wrap(e);
        }
    }
    
    @Override
    public DocumentModel getDocument() throws ClientException {
        return doc;
    }

    protected LikeService getService() throws Exception {
        return Framework.getService(LikeService.class);
    }
}
