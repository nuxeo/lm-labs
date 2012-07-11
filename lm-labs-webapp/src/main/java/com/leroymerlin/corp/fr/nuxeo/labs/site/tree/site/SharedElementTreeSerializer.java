package com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.ui.tree.TreeItem;

import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public class SharedElementTreeSerializer extends AdminSiteTreeSerializer {

    private static final Log LOG = LogFactory.getLog(SharedElementTreeSerializer.class);

    public SharedElementTreeSerializer() {
        super();
    }

    public SharedElementTreeSerializer(WebContext ctx) {
        this();
        this.ctx = ctx;
    }

    @Override
    protected JSONObject item2JSON(TreeItem item, JSONArray children) {
        JSONObject json = new JSONObject();
        json.element("data", getText(item));

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
            CoreSession session = WebEngine.getActiveContext().getCoreSession();
            SiteDocument siteAdapter = Tools.getAdapter(SiteDocument.class, doc, session);
            if (siteAdapter != null) {
                LabsSite site = siteAdapter.getSite();
                if (Tools.getAdapter(Page.class, doc, session) == null) {
                    if (LabsSiteConstants.Docs.PAGECLASSEURFOLDER.type().equals(doc.getType())){
                        metadata.put("url", "#");
                    }
                    else if(!StringUtils.isEmpty(siteAdapter.getResourcePath())){
                        metadata.put("url", siteAdapter.getResourcePath() + "/@blob");
                        //TODO
                        //metadata.put("srcImg", "/nuxeo" + doc.getProperty("common", "icon"));
                    }
                    else{
                        metadata.put("url", URIUtils.quoteURIPathComponent(siteAdapter.getSite().getURL(), true));
                    }
                } else {
                    metadata.put("url", siteAdapter.getResourcePath());
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
        String text = item.getLabel();
        if (ctx != null){
            if(LabsSiteConstants.Docs.ASSETS.type().equals(text)){
                return ctx.getMessage("label.sharedelement.asset.rootNode");
            }
            if(LabsSiteConstants.Docs.TREE.type().equals(text)){
                return ctx.getMessage("label.sharedelement.tree.rootNode");
            }
        }
        return text;
    }

}
