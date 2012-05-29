package com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.ui.tree.TreeItem;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.AbstractJSONSerializer;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class AdminSiteTreeSerializer extends AbstractJSONSerializer {

    private WebContext ctx;

    private static final Log LOG = LogFactory.getLog(AdminSiteTreeSerializer.class);

    public AdminSiteTreeSerializer() {
        super();
    }

    public AdminSiteTreeSerializer(WebContext ctx) {
        this();
        this.ctx = ctx;
    }

    @Override
    protected String getBasePath(WebContext ctx) throws ClientException {
        StringBuilder sb = new StringBuilder(ctx.getModulePath());
        LabsSite site = (LabsSite) ctx.getProperty("site");
        sb.append("/" + site.getURL());
        return sb.toString();
    }

    @Override
    protected JSONObject item2JSON(TreeItem item, JSONArray children) {
        JSONObject json = new JSONObject();

        String text = (ctx != null && LabsSiteConstants.Docs.ASSETS.type().equals(
                getText(item))) ? ctx.getMessage("label.admin.asset.rootNode")
                : getText(item);
        json.element("data", text);

        JSONObject attrs = new JSONObject();
        DocumentModel doc = (DocumentModel) item.getObject();
        attrs.put("id", doc.getId());
        attrs.put("rel", doc.getType());
        json.element("attr", (Object) attrs);
        JSONObject metadata = new JSONObject();
        try {
            metadata.put("lifecyclestate", doc.getCurrentLifeCycleState());
        } catch (ClientException e) {
            LOG.error("Unable to get current life cycle of document "
                    + doc.getPathAsString() + ": " + e.getCause());
        }
        try {
            SiteDocument siteAdapter = doc.getAdapter(SiteDocument.class);
            if (siteAdapter != null) {
                CoreSession session = WebEngine.getActiveContext().getCoreSession();
                LabsSite site = siteAdapter.getSite(session);
                DocumentModel tree = site.getTree(session);
                if (doc.getAdapter(Page.class) == null
                        || (Docs.WELCOME.docName().equals(doc.getName()) && session.getParentDocumentRef(
                                doc.getRef()).equals(tree.getRef()))) {
                    metadata.put("url", siteAdapter.getSite(session).getURL());
                } else {
                    metadata.put("url", siteAdapter.getResourcePath(session));
                }
                if (site.getHomePageRef().equals(doc.getId())) {
                    metadata.put("ishomepage", "true");
                }
            }
        } catch (ClientException e) {
            LOG.error("Unable to get URL of page " + doc.getPathAsString()
                    + ": " + e.getCause());
        }
        json.element("metadata", (Object) metadata);
        if (item.isContainer()) {
            if (item.hasChildren()) {
                json.element("children", children);
            }
            json.element("state", item.isExpanded() ? "open" : "closed");
        }
        return json;
    }

    private String getText(TreeItem item) {
        return item.getLabel()
        // + " (" + ((DocumentModel) item.getObject()).getId() + ")"
        ;
    }

}
