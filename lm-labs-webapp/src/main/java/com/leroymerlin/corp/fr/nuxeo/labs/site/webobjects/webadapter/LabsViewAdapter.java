package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.platform.picture.api.PictureView;
import org.nuxeo.ecm.platform.picture.api.adapters.MultiviewPicture;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

/**
 * @author vdu
 * This is a almost identical copy of lm-portal-picturebook-webapp ViewAdapter
 *
 */
@WebAdapter(name = "view", type = "viewAdapter", targetType = "Picture")
public class LabsViewAdapter extends DefaultAdapter {

    private static final int EXPIRE_DURATION_IN_HOUR = 2;

    @GET
    public Object doGet(@Context Request request) throws ClientException {
        return getVersion("Medium", request);
    }

    @GET
    @Path("{version}")
    public Object getVersion(@PathParam("version") String version,
            @Context Request request) throws ClientException {
        StringTokenizer st = new StringTokenizer(version, ".");
        version = st.nextToken();
        DocumentModel doc = this.getTarget()
                .getAdapter(DocumentModel.class);

        MultiviewPicture mvp = doc.getAdapter(MultiviewPicture.class);
        PictureView view = mvp.getView(version);
        if (view == null) {
            view = mvp.getView("Medium");
        }
        if (view == null) {
            return Response.ok().status(404);
        }

        Blob blob = (Blob) view.getContent();

        if (blob != null) {
            Calendar modified = (Calendar) doc.getPropertyValue("dc:modified");
            EntityTag etag = computeEntityTag(doc);
            Response.ResponseBuilder rb = request.evaluatePreconditions(
                    modified.getTime(), etag);
            if (rb != null) {
                return rb.build();
            }

            Calendar expire = Calendar.getInstance();
            expire.add(Calendar.HOUR, EXPIRE_DURATION_IN_HOUR);

            ResponseBuilder ok = Response.ok(blob);
            ResponseBuilder header = ok.header("Content-Disposition",
                    "inline;filename=" + view.getFilename());
            ResponseBuilder type2 = header.type(blob.getMimeType());
            ResponseBuilder lastModified = type2.lastModified(modified.getTime());
            ResponseBuilder expires = lastModified.expires(expire.getTime());
            ResponseBuilder tag = expires.tag(etag);
            return tag.build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
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
