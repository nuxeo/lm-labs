package com.leroymerlin.corp.fr.nuxeo.labs.site.operations;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;

@Operation(id = ListPageClasseurFolderFiles.ID, category = Constants.CAT_DOCUMENT, label = "Files List of a PageClasseur's folder", description = "Lists files of a PageClasseur's folder")
public class ListPageClasseurFolderFiles {

    protected static final String ID = "LabsSite.ListPageClasseurFolderFiles";

    @Context
    protected CoreSession session;

    @Param(name = "contextPath")
    protected String contextPath;

    @Param(name = "pageSize", required = false)
    protected Integer pageSize;

    @OperationMethod
    public DocumentModelList run() {
        Long targetPageSize = null;
        if (pageSize != null) {
            targetPageSize = Long.valueOf(pageSize.longValue());
        }
        return new DocumentModelListImpl();
    }

}
