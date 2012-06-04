package com.leroymerlin.corp.fr.nuxeo.labs.site.operations;

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
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.platform.query.api.PageProvider;

import com.leroymerlin.common.core.providers.DocumentModelListPageProvider;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

@Operation(id = GetChildrenPages.ID, category = Constants.CAT_FETCH, label = "",
description = "")
public class GetChildrenPages {
    public static final String ID = "LabsSite.GetChildrenPages";

    @Context
    protected CoreSession session;

    @Param(name="docId", required=false)
    protected String docId;
    
    @Param(name="docPath", required=false)
    protected String docPath;
    
    @Param(name = "pageSize", required = false)
    protected Integer pageSize;

    @OperationMethod
    public DocumentModelList run() throws Exception {
        Long targetPageSize = null;
        if (pageSize != null) {
            targetPageSize = Long.valueOf(pageSize.longValue());
        }
        DocumentModel document;
        if (docPath != null) {
            document = session.getDocument(new PathRef(docPath));
        } else if (docId != null) {
            document = session.getDocument(new IdRef(docId));
        } else {
            throw new IllegalArgumentException("missing docId or docPath.");
        }
        SiteDocument siteDocument = Tools.getAdapter(SiteDocument.class, document, session);
        PageProvider<DocumentModel> pageProvider = new DocumentModelListPageProvider(siteDocument.getChildrenPageDocuments());
        if (pageSize != null) {
            pageProvider.setPageSize(targetPageSize);
        }
        return new PaginableDocumentModelListImpl(pageProvider);
    }
    
}
