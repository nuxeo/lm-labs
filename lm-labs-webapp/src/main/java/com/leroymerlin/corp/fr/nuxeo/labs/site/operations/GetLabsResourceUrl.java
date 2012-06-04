package com.leroymerlin.corp.fr.nuxeo.labs.site.operations;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

@Operation(id = GetLabsResourceUrl.ID, category = Constants.CAT_SERVICES, label = "",
description = "")
public class GetLabsResourceUrl {

    public static final String ID = "LabsSite.GetResourceUrl";

    @Context
    protected CoreSession session;

    @OperationMethod
    public String run(DocumentRef doc) throws Exception {
        DocumentModel resource = session.getDocument(doc);
        SiteDocument siteDocument = Tools.getAdapter(SiteDocument.class, resource, session);
        return siteDocument.getResourcePath();
    }

}
