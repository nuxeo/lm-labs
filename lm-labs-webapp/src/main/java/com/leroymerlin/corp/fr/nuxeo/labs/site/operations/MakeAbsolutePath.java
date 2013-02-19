package com.leroymerlin.corp.fr.nuxeo.labs.site.operations;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

@Operation(id = MakeAbsolutePath.ID, category = Constants.CAT_FETCH, label = "", description = "")
public class MakeAbsolutePath extends AbstractMakeAbsolutePath {

    public static final String ID = "LabsSite.MakeAbsolutePath";

    @Context
    protected CoreSession session;
    
    @Param(name="endPath", required=true)
    protected String endPath;
    
    @Param(name="startPath", required=false)
    protected String startPath;

    @Param(name="docId", required=false)
    protected String docId;

    @Param(name="gadgetDocId", required=false)
    protected String gadgetDocId;
    
    @OperationMethod
    public String run() throws Exception {
        DocumentModel document;
        if (gadgetDocId != null) {
            document = session.getParentDocument(new IdRef(gadgetDocId));
            startPath = document.getPathAsString();
        } else if (docId != null) {
            document = session.getDocument(new IdRef(docId));
            startPath = document.getPathAsString();
        }
        return makeAbsolutePath(startPath, endPath);
    }
}
