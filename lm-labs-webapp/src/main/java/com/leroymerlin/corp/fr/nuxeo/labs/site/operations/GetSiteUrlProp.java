package com.leroymerlin.corp.fr.nuxeo.labs.site.operations;

import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

@Operation(id = GetSiteUrlProp.ID, category = Constants.CAT_FETCH, label = "",
description = "")
public class GetSiteUrlProp {


    public static final String ID = "LabsSite.GetSiteUrlProp";

    @Context
    protected CoreSession session;

    @Param(name="docId", required=true)
    protected String docId;
    
    @OperationMethod
    public String run() throws Exception {
        DocumentModel document = session.getDocument(new IdRef(docId));
        SiteDocument siteDocument = Tools.getAdapter(SiteDocument.class, document, session);
        
        return URIUtils.quoteURIPathComponent(siteDocument.getSite().getURL(), true);
    }
    
}
