package com.leroymerlin.common.core.adapter;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

public class FilesAdapterFactory implements DocumentAdapterFactory {

    public static final String SCHEMA_FILES = "files";

    public Object getAdapter(DocumentModel doc, Class itf) {
        if (doc.hasSchema(SCHEMA_FILES)) {
            return new FilesAdapterImpl(doc);
        }
        return null;
    }

}
