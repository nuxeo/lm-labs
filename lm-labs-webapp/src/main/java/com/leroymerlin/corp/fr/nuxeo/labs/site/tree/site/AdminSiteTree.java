package com.leroymerlin.corp.fr.nuxeo.labs.site.tree.site;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.ui.tree.ContentProvider;
import org.nuxeo.ecm.webengine.ui.tree.JSonTreeSerializer;

import com.leroymerlin.corp.fr.nuxeo.labs.site.tree.AbstractDocumentTree;

public class AdminSiteTree extends AbstractDocumentTree {

    public AdminSiteTree(WebContext ctx, DocumentModel rootDoc) {
        super(ctx, rootDoc);
    }

    @Override
    protected ContentProvider getProvider(WebContext ctx) {
        return new AdminSiteContentProvider(ctx.getCoreSession());
    }

    @Override
    protected JSonTreeSerializer getSerializer(WebContext ctx) {
        return new AdminSiteTreeSerializer();
    }

}
