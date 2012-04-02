package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.webengine.WebException;

import com.leroymerlin.common.core.security.LMPermission;
import com.leroymerlin.common.core.security.SecurityData;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Rights;


public final class LabsSiteUtils {
    
    private static final String IMPOSSIBLE_TO_VERIFY_THE_PERMISSION = "Impossible to verify the permissions only 'Read'";
    private static final Log log = LogFactory.getLog(LabsSiteUtils.class);

    private static final String THE_RIGHT_LIST_DONT_BE_NULL = "The right'list dont be null !";
    
    public enum Action {
        GRANT, REMOVE;
    }

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
    
    public static DocumentModel getPageName(String name, DocumentRef parentRef, CoreSession session) throws ClientException {
        DocumentModel parent = session.getDocument(parentRef);
        PathRef pathRef = new PathRef(parent.getPathAsString() + "/" + name);
        if (session.exists(pathRef)) {
            return session.getDocument(pathRef);
        }
        return null;
    }
    
    public static boolean pageNameExists(final String title, DocumentRef parentRef, CoreSession session) {
        try {
            if(getPageName(title, parentRef, session) != null) {
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
    
    /**
     * @param permission
     * @throws ClientException
     * @throws Exception
     */
    public static void unblockInherits(String permission, DocumentModel document) throws ClientException,Exception {
        CoreSession session = document.getCoreSession();
        if(StringUtils.isEmpty(permission)){
            SecurityData sd = SecurityDataHelper.buildSecurityData(document);         
            sd.setBlockRightInheritance(false, null);
            SecurityDataHelper.updateSecurityOnDocument(document, sd);
            session.save();
        }
        else{
            DocumentModel docParent = session.getDocument(document.getParentRef());
            List<LMPermission> permissions = extractPermissions(docParent);
            List<LMPermission> permissionsAdmin = new ArrayList<LMPermission>();
            List<LMPermission> permissionsWrite = new ArrayList<LMPermission>();
            List<LMPermission> permissionsRead = new ArrayList<LMPermission>();
            for (LMPermission perm : permissions) {
                if (Rights.EVERYTHING.getRight().equals(perm.getPermission())) {
                    permissionsAdmin.add(perm);
                } else if (Rights.WRITE.getRight().equals(perm.getPermission())) {
                    permissionsWrite.add(perm);
                } else if (Rights.READ.getRight().equals(perm.getPermission())) {
                    permissionsRead.add(perm);
                }
            }
            if (Rights.WRITE.getRight().equals(permission)){
                for (LMPermission perm : permissionsWrite) {
                    setPermission(document, perm.permission, perm.getName(), Action.GRANT, true);
                }
            }
            if (Rights.READ.getRight().equals(permission)){
                for (LMPermission perm : permissionsRead) {
                    setPermission(document, perm.permission, perm.getName(), Action.GRANT, true);
                }
            }
        }
        
        final DocumentRef ref = document.getRef();
        UnrestrictedSessionRunner runner = new UnrestrictedSessionRunner(document.getCoreSession()){
            
            @Override
            public void run() throws ClientException {
                DocumentModel docu = session.getDocument(ref);
                try{
                    deleteDuplicate(docu);
                } catch (Exception e) {
                    throw WebException.wrap("Problem for change permissions", e);
                }
                session.save();
            }
            
        };
        runner.runUnrestricted();
        
        deleteDuplicate(document);
    }

    /**
     * @param document
     * @throws Exception
     */
    private static void deleteDuplicate(DocumentModel document) throws Exception {
        List<LMPermission> permissions = extractPermissions(document);
        List<LMPermission> permissionsAdmin = new ArrayList<LMPermission>();
        List<LMPermission> permissionsWrite = new ArrayList<LMPermission>();
        List<LMPermission> permissionsRead = new ArrayList<LMPermission>();
        for (LMPermission perm : permissions) {
            if (Rights.EVERYTHING.getRight().equals(perm.getPermission())) {
                permissionsAdmin.add(perm);
            } else if (Rights.WRITE.getRight().equals(perm.getPermission())) {
                permissionsWrite.add(perm);
            } else if (Rights.READ.getRight().equals(perm.getPermission())) {
                permissionsRead.add(perm);
            }
        }
        Map<String, LMPermission> mapPerm = new HashMap<String, LMPermission>();
        deleteUnusedPermissions(document, permissionsAdmin, mapPerm);
        mapPerm = new HashMap<String, LMPermission>();
        deleteUnusedPermissions(document, permissionsWrite, mapPerm);
        mapPerm = new HashMap<String, LMPermission>();
        deleteUnusedPermissions(document, permissionsRead, mapPerm);
    }

    /**
     * @param document
     * @param permissions
     * @param mapPerm
     * @throws Exception
     */
    private static void deleteUnusedPermissions(DocumentModel document,
            List<LMPermission> permissions, Map<String, LMPermission> mapPerm)
            throws Exception {
        List<LMPermission> permToDelete = new ArrayList<LMPermission>();
        
        for (LMPermission perm : permissions) {
            if(mapPerm.containsKey(perm.getName())){
                if(!perm.isInherited()){
                    permToDelete.add(perm);
                }
                else{
                    permToDelete.add(mapPerm.get(perm.getName()));
                    mapPerm.put(perm.getName(), perm);
                }
            }
            else{
                mapPerm.put(perm.getName(), perm);
            }
        }
        
        for (LMPermission perm : permToDelete){
            setPermission(document, perm.permission, perm.getName(), Action.REMOVE, true);
        }
    }
    


    /**
     * @return
     */
    public static List<String> getListRights() {
        List<String> labsRightsString = new ArrayList<String>();

        for (Rights labsRight : Rights.values()) {
            labsRightsString.add(labsRight.getRight());
        }
        if (labsRightsString.isEmpty()) {
            throw new WebException(THE_RIGHT_LIST_DONT_BE_NULL);
        }
        return labsRightsString;
    }
    


    /**
     * Sets a permission on a element.
     * 
     * @param doc {@link DocumentModel}
     * @param permission permission name
     * @param id user or group name
     * @param action <code>Action.GRANT</code> or <code>Action.REMOVE</code>
     * @param override
     * @throws IllegalStateException when current user has a higher permission
     * @throws Exception
     */
    public static void setPermission(DocumentModel doc, final String permission,
            final String id, Action action, boolean override)
            throws IllegalStateException, Exception {
        if (doc != null) {
            if (!PermissionsHelper.hasPermission(doc, permission, id)) {
                if (!PermissionsHelper.groupOrUserExists(id)) {
                    // // TODO throw exception to notify unknow group/user
                    // log.error("Failed to get principal:" + e);
                    throw new WebException("Unknown group or user");
                    // return;
                }
                boolean granted = Action.GRANT.equals(action);
                if (granted) {
                    List<String> labsRightsString = getListRights();
                    try {
                        PermissionsHelper helper = new PermissionsHelper(
                                labsRightsString);
                        if (!override
                                && helper.hasHigherPermission(doc, permission,
                                        id)) {
                            throw new IllegalStateException(
                                    "message.security.permission.hasHigherPermission");
                        }
                        helper.grantPermission(doc, permission, id, override);
                        doc.getCoreSession().save();
                    } catch (ClientException e) {
                        // TODO throw exception to notify unknow group/user
                        log.error("Failed to save session:" + e);
                        return;
                    }
                }
            } else {
                if (Action.REMOVE.equals(action)) {
                    // if (username.equals(ctx.getPrincipal().getName()) &&
                    // SpacePermissionsHelper.Right.GESTIONNAIRE.name.equals(permission))
                    // {
                    // throw new WebException("permission removal forbidden (" +
                    // username + "/" + permission + ")");
                    // }
                    try {
                        PermissionsHelper.removePermission(doc, permission, id);
                        doc.getCoreSession().save();
                    } catch (Exception e) {
                        // TODO throw exception to notify unknown group/user
                        log.error("Failed to remove permission (" + id + "/"
                                + permission + ") on " + doc, e);
                        return;
                    }

                } else {
                    log.warn("principal " + id + " has already permission "
                            + permission);
                }
            }
        } else {
            // throw WebException.wrap("set permission on null document", new
            // NullPointerException());
            log.error("set permission on null document");
        }
    }
    
    public static DocumentModelList getChildrenPageDocuments(DocumentModel document) throws ClientException {
        @SuppressWarnings("serial")
        DocumentModelList children = document.getCoreSession().getChildren(document.getRef(), null, new Filter() {
            @Override
            public boolean accept(DocumentModel document) {
                return Docs.pageDocs().contains(Docs.fromString(document.getType()));
            }}, null);
        return children;
    }

}
