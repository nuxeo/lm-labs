/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

/**
 * @author fvandaele
 *
 */
@WebAdapter(name = "labspublish", type = "LabsPublish")
@Produces("text/html; charset=UTF-8")
public class LabsPublishService extends DefaultAdapter {
    
    private static final String IMPOSSIBLE_TO_DRAFT = "Impossible to draft";
    private static final String IMPOSSIBLE_TO_PUBLISH = "Impossible to publish";
    private static final String PUBLISH = "publish";
    private static final String DRAFT = "draft";
//    private static final String VISIBLE = "visible";
//    private static final String NO_VISIBLE = "no visible";
    private static final String NOT_DRAFT = "not draft";
    private static final String NOT_PUBLISHED = "not published";

    private static final Log log = LogFactory.getLog(LabsPublishService.class);


    
    @GET
    @Path("publish")
    public Object doPublish() {
        DocumentModel document = getDocument(null);
        return publish(document);
    }

    @GET
    @Path("publishSite")
    public Object doPublishSite() {
        DocumentModel document = getDocument(LabsSiteConstants.Docs.SITE.type());
        return publish(document);
    }
    
    /**
     * @param document
     * @return
     */
    private Object publish(DocumentModel document) {
        try {
            if (LabsSiteConstants.State.DRAFT.getState().equals(document.getCurrentLifeCycleState())){
                Page page = document.getAdapter(Page.class);
                page.publish();
                return Response.ok(PUBLISH).build();
            }
        } catch (ClientException e) {
            log.error(IMPOSSIBLE_TO_PUBLISH, e);
        }
        return Response.ok(NOT_PUBLISHED).build();
    }
    
    @GET
    @Path("draft")
    public Object doDraft() {
        DocumentModel document = getDocument(null);
        return draft(document);
    }
    
    @GET
    @Path("draftSite")
    public Object doDraftSite() {
        DocumentModel document = getDocument(LabsSiteConstants.Docs.SITE.type());
        return draft(document);
    }

    /**
     * @param document
     * @return
     */
    private Object draft(DocumentModel document) {
        try {
            if (LabsSiteConstants.State.PUBLISH.getState().equals(document.getCurrentLifeCycleState())){
                Page page = document.getAdapter(Page.class);
                page.draft();
                return Response.ok(DRAFT).build();
            }
        } catch (ClientException e) {
            log.error(IMPOSSIBLE_TO_DRAFT, e);
        }
        return Response.ok(NOT_DRAFT).build();
    }
    
    /**
     * @return
     */
    private DocumentModel getDocument(String pTypeDoc) {
        DocumentModel document = null;
        DocumentObject dobj = (DocumentObject) getTarget();
        if (StringUtils.isEmpty(pTypeDoc)){
            document = dobj.getDocument();
        }
        else if(LabsSiteConstants.Docs.SITE.type().equals(pTypeDoc)){
            document = dobj.getDocument();
            SiteDocument adapter = document.getAdapter(SiteDocument.class);
            try {
                document = adapter.getSite().getDocument();
            } catch (ClientException e) {
                log.error("Impossible to get site !", e);
            }
        }
        return document;
    }

}
