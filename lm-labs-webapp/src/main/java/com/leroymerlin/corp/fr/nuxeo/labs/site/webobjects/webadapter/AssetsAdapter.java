/**
 * 
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

@WebAdapter(name = "assets", type = "assetsAdapter")
public class AssetsAdapter extends DefaultAdapter {

    private static final Log LOG = LogFactory.getLog(AssetsAdapter.class);

    public DocumentModel getAssetDoc() {
        try {
            PathRef ref = new PathRef("/default-domain/sites/"
                    + this.getPrevious().getName() + "/"
                    + LabsSiteConstants.Docs.ASSETS.docName());
            return ctx.getCoreSession().getDocument(ref);
        } catch (ClientException e) {
            LOG.error(e, e);
            throw WebException.wrap(e);
        }
    }

    @GET
    @Path("id")
    public String getAssetId() {
        return getAssetDoc().getId();
    }
}
