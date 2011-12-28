package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.security.SecurityConstants;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.State;

import com.leroymerlin.common.core.security.LMPermission;


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
        DocumentModel parent = session.getDocument(parentRef);
        PathRef pathRef = new PathRef(parent.getPathAsString() + "/" + name);
        if (session.exists(pathRef)) {
            DocumentModel child = session.getDocument(pathRef);
            if (State.DELETE.getState().equals(child.getCurrentLifeCycleState())) {
                return child;
            }
        }
        return null;
    }
    
    public static boolean existDeletedPageName(final String title, DocumentRef parentRef, CoreSession session) {
        try {
            if(getDeletedPageName(title, parentRef, session) != null) {
                return true;
            }
        } catch (ClientException e) {
            log.error(e, e);
        }
        return false;
    }
    
    public static List<LMPermission> extractPermissions(DocumentModel document) throws Exception{
        List<LMPermission> permissions = PermissionsHelper.getPermissions(document);
        @SuppressWarnings("deprecation")
        final String[] excludedUsers = { SecurityConstants.ADMINISTRATOR,
                SecurityConstants.ADMINISTRATORS, SecurityConstants.MEMBERS };
        CollectionUtils.filter(permissions, new Predicate() {
            public boolean evaluate(Object o) {
                return ((LMPermission) o).isGranted()
                        && !Arrays.asList(excludedUsers).contains(
                                ((LMPermission) o).getName());
            }
        });
        return permissions;
    }

}
