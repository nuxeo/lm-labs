package com.leroymerlin.corp.fr.nuxeo.labs.site.operations;

import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

@Operation(id = MakeRelativePath.ID, category = Constants.CAT_FETCH, label = "", description = "")
public class MakeRelativePath {

    private static final String PARENT_PATH = "../";

    public static final String ID = "LabsSite.MakeRelativePath";

    @Context
    protected CoreSession session;

    @Param(name="startPath", required=true)
    protected String startPath;

    @Param(name="endPath", required=true)
    protected String endPath;

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
        String commonPrefix = StringUtils.getCommonPrefix(new String[] {startPath, endPath});
        if (StringUtils.equals(startPath, commonPrefix) || StringUtils.equals(endPath, commonPrefix)) {
            commonPrefix += '/';
        }
        int lastSlashPos = StringUtils.lastIndexOf(commonPrefix, "/");
        commonPrefix = StringUtils.substring(commonPrefix, 0, lastSlashPos+1);
        startPath = StringUtils.substringAfter(startPath, commonPrefix);
        endPath = StringUtils.substringAfter(endPath, commonPrefix);
        int countMatches = StringUtils.countMatches(startPath, "/");
        if (!startPath.isEmpty()) {
            countMatches++;
        }
        return StringUtils.repeat(PARENT_PATH, countMatches) + endPath;
    }
}
