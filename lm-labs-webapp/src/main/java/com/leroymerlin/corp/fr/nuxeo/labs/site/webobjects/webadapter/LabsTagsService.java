/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

/**
 * @author ephongsavanh
 * 
 */
@WebAdapter(name = "labstags", type = "LabsTags")
public class LabsTagsService extends DefaultAdapter {

    @POST
    public Response changeLabsTags() {

        try {
            FormData form = ctx.getForm();
            String labstags = form.getString("labsTags");
            String[] split = labstags.split(",");
            if (split.length ==1 && StringUtils.isEmpty(split[0])){
                split = new String[0];
            }
            List<String> list = new ArrayList<String>(Arrays.asList(split));
            DocumentModel document = getDocument();
            CoreSession session = ctx.getCoreSession();
            Page tags = Tools.getAdapter(Page.class, document, session);
            if (tags != null) {
                tags.setLabsTags(list);
                session.saveDocument(document);
                session.save();
                return Response.ok().build();
            }
        } catch (Exception e) {
            throw WebException.wrap(e);
        }

        return Response.status(Status.NOT_MODIFIED).build();
    }
    
    private DocumentModel getDocument() {
        DocumentObject dobj = (DocumentObject) getTarget();
        DocumentModel document = dobj.getDocument();
        return document;
    }

}
