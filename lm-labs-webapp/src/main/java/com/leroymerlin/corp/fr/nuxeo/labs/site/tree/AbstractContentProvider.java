package com.leroymerlin.corp.fr.nuxeo.labs.site.tree;

import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.webengine.ui.tree.document.DocumentContentProvider;

public abstract class AbstractContentProvider extends DocumentContentProvider {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AbstractContentProvider(CoreSession session) {
        super(session);
    }

    abstract public Filter getDocFilter();

    @Override
    public Object[] getChildren(Object obj) {
        Object[] objects = super.getChildren(obj);
        List<Object> v = new Vector<Object>();
        for (Object o : objects) {
            DocumentModel doc = (DocumentModel) o;
            if(getDocFilter().accept(doc)) {
                v.add(doc);
            }
        }
        return v.toArray();
    }

    @Override
    public String getLabel(Object obj) {
        String label = super.getLabel(obj);
        return label == null ? null : StringEscapeUtils.escapeXml(label);
    }

    @Override
    public boolean isContainer(Object obj) {

        if (!super.isContainer(obj)) {
            return false;
        }
        if (obj instanceof DocumentModel) {
            DocumentModel doc = (DocumentModel) obj;
            try {
                return session.getChildren(doc.getRef(), null, null,
                        getDocFilter(), null).size() > 0;
            } catch (ClientException e) {
                return false;
            }
        }
        return false;

    }
}
