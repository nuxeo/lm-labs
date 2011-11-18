/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.publisher.LabsPublisher;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

/**
 * @author fvandaele
 *
 */
@WebAdapter(name = "labspublish", type = "LabsPublish")
@Produces("text/html; charset=UTF-8")
public class LabsPublishService extends DefaultAdapter {
    
    private static final String IMPOSSIBLE_TO_DRAFT = "Impossible to draft!";
    private static final String IMPOSSIBLE_TO_PUBLISH = "Impossible to publish!";
    private static final String PUBLISH = "publish";
    private static final String DRAFT = "draft";
    private static final String DELETE = "delete";
    private static final String UNDELETE = "undelete";
    private static final String NOT_DRAFT = "not drafted";
    private static final String NOT_PUBLISHED = "not published";

    private static final Log log = LogFactory.getLog(LabsPublishService.class);
    private static final Object IMPOSSIBLE_TO_DELETE = "Impossible to delete!";
    private static final Object NOT_DELETED = "not deleted";


    
    @PUT
    @Path("publish")
    public Object doPublish() {
        DocumentModel document = getDocument();
        try {
            if (LabsSiteConstants.State.DRAFT.getState().equals(document.getCurrentLifeCycleState())){
                LabsPublisher publisherAdapter = document.getAdapter(LabsPublisher.class);
                publisherAdapter.publish();
                return Response.ok(PUBLISH).build();
            }
        } catch (ClientException e) {
            log.error(IMPOSSIBLE_TO_PUBLISH, e);
        }
        return Response.ok(NOT_PUBLISHED).build();
    }
    
    @PUT
    @Path("draft")
    public Object doDraft() {
        DocumentModel document = getDocument();
        try {
            if (LabsSiteConstants.State.PUBLISH.getState().equals(document.getCurrentLifeCycleState())){
                LabsPublisher publisherAdapter = document.getAdapter(LabsPublisher.class);
                publisherAdapter.draft();
                return Response.ok(DRAFT).build();
            }
        } catch (ClientException e) {
            log.error(IMPOSSIBLE_TO_DRAFT, e);
        }
        return Response.ok(NOT_DRAFT).build();
    }
    
    @DELETE
    @Path("delete")
    public Object doDelete() {
        DocumentModel document = getDocument();
        try {
            if (!LabsSiteConstants.State.DELETE.getState().equals(document.getCurrentLifeCycleState())){
                LabsPublisher publisherAdapter = document.getAdapter(LabsPublisher.class);
                publisherAdapter.delete();
                return Response.ok(DELETE).build();
            }
        } catch (ClientException e) {
            log.error(IMPOSSIBLE_TO_DELETE, e);
        }
        return Response.ok(NOT_DELETED).build();
    }
    
    @GET
    @Path("undelete")
    public Object doUndelete() {
        DocumentModel document = getDocument();
        try {
            if (LabsSiteConstants.State.DELETE.getState().equals(document.getCurrentLifeCycleState())){
                LabsPublisher publisherAdapter = document.getAdapter(LabsPublisher.class);
                publisherAdapter.undelete();
                return Response.ok(UNDELETE).build();
            }
        } catch (ClientException e) {
            log.error(IMPOSSIBLE_TO_DELETE, e);
        }
        return Response.ok(NOT_DELETED).build();
    }
    
    @GET
    @Path("undelete/{ref}")
    public Object doUndeleteRef(@PathParam("ref") final String ref) {
        try {
            DocumentModel document = getContext().getCoreSession().getDocument(new IdRef(ref));
            if (LabsSiteConstants.State.DELETE.getState().equals(document.getCurrentLifeCycleState())){
                LabsPublisher publisherAdapter = document.getAdapter(LabsPublisher.class);
                publisherAdapter.undelete();
                return Response.ok(UNDELETE).build();
            }
        } catch (ClientException e) {
            log.error(IMPOSSIBLE_TO_DELETE, e);
        }
        return Response.ok(NOT_DELETED).build();
    }
    
    /**
     * @return
     */
    private DocumentModel getDocument() {
        DocumentObject dobj = (DocumentObject) getTarget();
        return dobj.getDocument();
    }

}
