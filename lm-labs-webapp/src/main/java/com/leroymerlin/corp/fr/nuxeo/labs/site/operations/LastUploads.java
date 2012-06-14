package com.leroymerlin.corp.fr.nuxeo.labs.site.operations;

import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.operations.services.PaginableDocumentModelListImpl;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;


@Operation(id = LastUploads.ID, category = Constants.CAT_FETCH, label = "",
description = "")
public class LastUploads {
    
    public static final String ID = "LabsSite.LastUploads";

    @Context
    protected CoreSession session;

    @Context
    protected AutomationService as;

    @Context
    protected OperationContext context;

    @Param(name="docId", required=true)
    protected String docId;
    
    @Param(name = "pageSize", required = false)
    protected Integer pageSize;

    @OperationMethod
    public DocumentModelList run() throws Exception {
        Long targetPageSize = null;
        if (pageSize != null) {
            targetPageSize = Long.valueOf(pageSize.longValue());
        }
        DocumentModel document = session.getDocument(new IdRef(docId));
        return 
                new PaginableDocumentModelListImpl(
//        (DocumentModelList)
        LabsSiteWebAppUtils.getLatestUploadsPageProvider(document, targetPageSize, session)
//        .setCurrentPage(0)
        )
        ;
    }
    
}
