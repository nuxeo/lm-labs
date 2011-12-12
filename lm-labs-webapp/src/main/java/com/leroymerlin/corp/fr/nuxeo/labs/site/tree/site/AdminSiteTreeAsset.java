package com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.ui.tree.ContentProvider;
import org.nuxeo.ecm.webengine.ui.tree.JSonTreeSerializer;

import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.AbstractDocumentTree;

public class AdminSiteTreeAsset extends AbstractDocumentTree {

    public AdminSiteTreeAsset(WebContext ctx, DocumentModel rootDoc) {
        super(ctx, rootDoc);
    }

    @Override
    protected ContentProvider getProvider(WebContext ctx) {
        return new SiteContentProvider(ctx.getCoreSession(), true);
    }

    @Override
    protected JSonTreeSerializer getSerializer(WebContext ctx) {
        return new AdminSiteTreeSerializer(ctx);
    }

}
