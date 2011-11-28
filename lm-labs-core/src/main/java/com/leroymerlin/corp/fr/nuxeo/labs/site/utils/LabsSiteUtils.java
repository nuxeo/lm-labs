package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.security.SecurityConstants;


public final class LabsSiteUtils {
    
    private static final String IMPOSSIBLE_TO_VERIFY_THE_PERMISSION = "Impossible to verify the permissions only 'Read'";
    private static final Log log = LogFactory.getLog(LabsSiteUtils.class);

    private LabsSiteUtils() {
    }

    /**
     * Return true if the user/group has only permissions 'Read'
     * @param doc
     * @param user
     * @return true if the user/group has only permissions 'Read'
     */
    public static boolean isOnlyRead(final DocumentModel doc, final String user){
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

}
