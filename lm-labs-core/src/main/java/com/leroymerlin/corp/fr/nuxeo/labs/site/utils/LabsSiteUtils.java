package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.LifeCycleConstants;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.query.sql.NXQL;


public final class LabsSiteUtils {
    
    private static final String IMPOSSIBLE_TO_VERIFY_THE_PERMISSION = "Impossible to verify the permissions only 'Read'";
    private static final Log log = LogFactory.getLog(LabsSiteUtils.class);

    private LabsSiteUtils() {
    }

    /**
     * Return true if the user/group has only permissions 'Read'
     * @param doc
     * @return true if the user/group has only permissions 'Read'
     */
    public static boolean isOnlyRead(final DocumentModel doc){
        boolean result = false;
        try {
            result = !doc.getCoreSession().hasPermission(doc.getRef(), SecurityConstants.READ_WRITE);
            if (result){
                result = doc.getCoreSession().hasPermission(doc.getRef(), SecurityConstants.READ);
            }
        } catch (Exception e) {
            log.error(IMPOSSIBLE_TO_VERIFY_THE_PERMISSION, e);
            result = false;
        }
        return result;
    }
    
    public static DocumentModel getDeletedPageName(String name, DocumentRef parentRef, CoreSession session) throws ClientException{
        
        StringBuilder sb = new StringBuilder("SELECT * From Document");
        sb.append(" WHERE ").append(NXQL.ECM_PARENTID).append(" = '").append(parentRef).append("'")
            .append(" AND ").append(NXQL.ECM_LIFECYCLESTATE).append(" = '").append(LifeCycleConstants.DELETED_STATE).append("'");
        DocumentModelList list = session.query(sb.toString());
        for (DocumentModel child : list) {
            if(name.equalsIgnoreCase(child.getName())){
                return child;
            }
        }
        return null;
    }
    
    public static boolean existDeletedPageName(String name, DocumentRef parentRef, CoreSession session) throws ClientException{
        if(getDeletedPageName(name, parentRef, session) == null){
            return false;
        }
        return true;
    }

}
