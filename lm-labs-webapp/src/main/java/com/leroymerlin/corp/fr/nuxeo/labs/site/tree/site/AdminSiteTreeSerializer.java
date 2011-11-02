package com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.ui.tree.TreeItem;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.AbstractJSONSerializer;
import com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.SitesRoot;

public class AdminSiteTreeSerializer extends AbstractJSONSerializer {

    private static final Log LOG = LogFactory.getLog(AdminSiteTreeSerializer.class);

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
        json.element("data", getText(item));
        JSONObject attrs = new JSONObject();
        attrs.put("id", ((DocumentModel) item.getObject()).getId());
        attrs.put("rel", ((DocumentModel) item.getObject()).getType());
        json.element("attr", (Object) attrs);
        JSONObject metadata = new JSONObject();
        try {
            metadata.put("lifecyclestate", ((DocumentModel) item.getObject()).getCurrentLifeCycleState());
        } catch (ClientException e) {
            LOG.error("Unable to get current life cycle of document " + ((DocumentModel) item.getObject()).getPathAsString() + ": " + e.getCause());
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
//                + " (" + ((DocumentModel) item.getObject()).getId() + ")"
                ;
    }

}
