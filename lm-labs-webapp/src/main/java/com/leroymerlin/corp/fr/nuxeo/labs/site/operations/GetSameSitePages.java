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
import org.nuxeo.ecm.platform.query.api.PageProvider;

import com.leroymerlin.common.core.providers.DocumentModelListPageProvider;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;

@Operation(id = GetSameSitePages.ID, category = Constants.CAT_FETCH, label = "",
description = "")
public class GetSameSitePages {
    public static final String ID = "LabsSite.GetSameSitePages";

    @Context
    protected CoreSession session;

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
        SiteDocument siteDocument = document.getAdapter(SiteDocument.class);
        LabsSite site = siteDocument.getSite();
        DocumentModelList pages = session.query(String.format("SELECT * FROM Page WHERE ecm:path STARTSWITH '%s' AND ecm:mixinType <> 'HiddenInNavigation' AND ecm:isCheckedInVersion = 0 AND ecm:currentLifeCycleState != 'deleted'", site.getDocument().getPathAsString().replace("'", "\\'")));
        PageProvider<DocumentModel> pageProvider = new DocumentModelListPageProvider(pages);
        if (pageSize != null) {
            pageProvider.setPageSize(targetPageSize);
        }
        return new PaginableDocumentModelListImpl(pageProvider);
    }

}
