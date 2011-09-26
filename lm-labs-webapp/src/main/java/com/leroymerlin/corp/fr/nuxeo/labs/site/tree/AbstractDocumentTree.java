package com.leroymerlin.corp.fr.nuxeo.labs.site.tree;

import net.sf.json.JSONArray;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.ui.tree.JSonTreeSerializer;
import org.nuxeo.ecm.webengine.ui.tree.TreeItem;
import org.nuxeo.ecm.webengine.ui.tree.document.DocumentTree;

public abstract class AbstractDocumentTree extends DocumentTree{


    public AbstractDocumentTree(WebContext ctx, DocumentModel rootDoc) {
        super(ctx, rootDoc);
    }

    @Override
    protected String enter(WebContext ctx, String path, JSonTreeSerializer serializer) {
        TreeItem item = tree.findAndReveal(path);
        if (item != null) {
            item.expand();
            if (!item.hasChildren()) {
                item.collapse();
            }
            JSONArray result = new JSONArray();
            if (item.isContainer()) {
                result = serializer.toJSON(item.getChildren());
            }
            return result.toString();
        } else {
            return null;
        }
    }
}
