package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

@WebAdapter(name = "blob", type = "BlobService")
public class BlobService extends DefaultAdapter {

    private static final Log LOG = LogFactory.getLog(BlobService.class);
    
    @GET
    public Object doGet(@Context Request request) {
        DocumentModel doc = this.getTarget().getAdapter(DocumentModel.class);
        return getBlob(doc, request, "attachment");
    }
    
    protected Object getBlob(DocumentModel doc, Request request, String contentDisposition) {
        try {
            Blob blob = doc.getAdapter(BlobHolder.class).getBlob();
            Calendar modified = (Calendar) doc.getPropertyValue("dc:modified");
            EntityTag etag = computeEntityTag(doc);
            Response.ResponseBuilder rb = request.evaluatePreconditions(
                    modified.getTime(), etag);
            if (rb != null) {
                return rb.build();
            }
            Calendar expire = Calendar.getInstance();
            String mimeType = blob.getMimeType() == null ? "image/jpeg"
                    : blob.getMimeType();
            String filename = blob.getFilename() == null ? "file" : blob.getFilename();
            return Response.ok(blob)
                    .header("Content-Disposition", contentDisposition + ";filename=\"" + filename + "\"")
                    .type(mimeType)
                    .lastModified(modified.getTime())
                    .expires(expire.getTime())
                    .tag(etag)
                    .build();
        } catch (NullPointerException e) {
            LOG.error(e, e);
        } catch (ClientException e) {
            LOG.error(e, e);
        }
        return Response.status(Status.NOT_FOUND).build();
    }

    protected EntityTag computeEntityTag(DocumentModel doc)
            throws PropertyException, ClientException {
        return new EntityTag(
                computeDigest(((Calendar) doc.getPropertyValue("dc:modified")).toString()));
    }

    private String computeDigest(String content) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] digest = md.digest(content.getBytes());
            BigInteger bi = new BigInteger(digest);
            return bi.toString(16);
        } catch (Exception e) {
            return "";
        }
    }

}
