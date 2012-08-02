package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.NuxeoGroup;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.common.core.security.LMPermission;
import com.leroymerlin.common.core.security.SecurityData;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.SecurityDataHelper;

//TODO move to lm-nuxeo-common and delete from lm-portal-common
/**
 * 
 */

/**
 * @author <a href="mailto:vincent.dutat@ext.leroymerlin.fr">Vincent Dutat</a>
 *
 */
public class PermissionsHelper {

    private static final Log log = LogFactory.getLog(PermissionsHelper.class);
    private List<String> orderedPermissions = null;
    private List<String> reverseOrderedPermissions = null;
    
    /**
     * @param orderedPermissions list of rights in descending order (ex: 'Everything', 'ReadWrite', 'Read');
     */
    public PermissionsHelper(final List<String> orderedPermissions) {
        this.orderedPermissions = new ArrayList<String>(orderedPermissions);
        this.reverseOrderedPermissions = new ArrayList<String>(this.orderedPermissions);
        Collections.reverse(this.reverseOrderedPermissions);
    }

    /**
     * Formats a display string. If <code>id</code> is a user name, cancatenates
     * user's first name and last name. Id <code>id</code> is a group name,
     * empty string.
     * 
     * @param id
     *            user or group name
     * @return the display name
     * @throws Exception
     *             If a user management operation fails
     */
    public static String getDisplayName(final String id) throws Exception {
        String displayName = StringUtils.EMPTY;
        UserManager userManager = Framework.getService(UserManager.class);
        NuxeoGroup group = userManager.getGroup(id);
        if (group != null) {
            // group
            // displayName = group.getName();
        } else {
            // user
            if (userManager.getPrincipal(id) != null) {
                if (!StringUtils.isEmpty(userManager.getPrincipal(id)
                        .getFirstName())
                        || !StringUtils.isEmpty(userManager.getPrincipal(id)
                                .getLastName())) {
                    displayName = userManager.getPrincipal(id)
                            .getFirstName() + " "
                            + userManager.getPrincipal(id)
                                    .getLastName();
                }
            }
        } 
        //
        return displayName;
    }

    /**
     * @param id
     *            user or group name
     * @return <code>true</code> if user or group ID exists otherwise
     *         <code>false</code>
     */
    public static boolean groupOrUserExists(final String id) {
        UserManager userManager;
        if (SecurityConstants.EVERYONE.equals(id)) {
            return true;
        }
        try {
            userManager = Framework.getService(UserManager.class);
            NuxeoPrincipal user = userManager.getPrincipal(id);
            if (user == null) {
                NuxeoGroup group = userManager.getGroup(id);
                if (group == null) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * TODO javadoc
     * 
     * @param doc
     *            document model
     * @return a list of {@link LMPermission}
     * @throws Exception
     */
    public static List<LMPermission> getPermissions(DocumentModel doc) throws Exception {
        List<LMPermission> permissions = new ArrayList<LMPermission>();

        SecurityData sd = SecurityDataHelper.buildSecurityData(doc);
        createLMPermissions(permissions, sd.getCurrentDocGrant(), true,
                false);
        if (!sd.isBlockRightInheritance())
            createLMPermissions(permissions, sd.getParentDocsGrant(), true,
                    true);

        Collections.sort(permissions);
        return permissions;
    }

    private static List<LMPermission> createLMPermissions(
            List<LMPermission> permissions,
            Map<String, List<String>> currentDocGrant, boolean isGranted,
            boolean isInherited) throws Exception {

        for (String user : currentDocGrant.keySet()) {
            for (String perm : currentDocGrant.get(user)) {
                permissions.add(new LMPermission(user, null,
                        perm, isGranted, isInherited));
            }

        }
        ;
        return permissions;
    }

    public static void addPermission(DocumentModel doc, String permission,
            String id, boolean grant) throws ClientException {
        final String logPrefix = "<addPermission> ";
        log.debug(logPrefix + doc.getName() + ", " + permission + ", " + id
                + ", " + grant);
        SecurityData securityData = SecurityDataHelper.buildSecurityData(doc);
        securityData.addModifiablePrivilege(id, permission, grant);
        SecurityDataHelper.updateSecurityOnDocument(doc, securityData);
        log.debug(logPrefix + "added to " + doc.getName() + "("
                + doc.getDocumentType()
                        .getName() + ", " + doc.getId() + ")");
    }

    /**
     * Remove a permission.
     * 
     * @param doc
     *            {@link DocumentModel}
     * @param permission
     *            permission name
     * @param id
     *            user or group name
     * @throws ClientException
     *             when a user management operation fails
     */
    public static boolean removePermission(final DocumentModel doc,
            String permission, String id) throws ClientException {
        final String logPrefix = "<removePermission> ";
        log.debug(logPrefix + doc.getName() + ", " + permission + ", " + id);

        SecurityData securityData = SecurityDataHelper.buildSecurityData(doc);
        boolean deleted = securityData.removeModifiablePrivilege(id,
                permission, true);
        if (deleted) {
            SecurityDataHelper.updateSecurityOnDocument(doc, securityData);
            log.debug(logPrefix + "removed from " + doc.getName() + "("
                    + doc.getDocumentType()
                            .getName() + ", " + doc.getId() + ")");
        } else {
            log.warn(logPrefix + "permission not removed !");
        }
        return deleted;
    }

    /**
     * TODO javadoc
     * 
     * @param doc
     *            {@link DocumentModel}
     * @param permission
     *            permission name
     * @param id
     *            user or group name
     * @return <code>true</code> if supplied document model has supplied permission,
     *         otherwise <code>false</code>
     * @throws Exception
     *             when could not get core session from document model
     * @throws ClientException
     *             when {@link DocumentModel} could not be retrieved
     */
    public static boolean hasPermission(final DocumentModel doc,
            final String permission, final String id) throws 
            ClientException, Exception {
        return CollectionUtils.exists(getPermissions(doc),
                PredicateUtils.allPredicate(new Predicate[] { new Predicate() {
                    public boolean evaluate(Object o) {
                        return id.equals(((LMPermission) o).getName());
                    }
                }, new Predicate() {
                    public boolean evaluate(Object o) {
                        return permission.equals(((LMPermission) o).getPermission());
                    }
                }, new Predicate() {
                    public boolean evaluate(Object o) {
                        return ((LMPermission) o).isGranted();
                    }
                }
                // TODO vdu exclude inherited permissions (# 170)
                }));
    }

    /**
     * TODO javadoc
     * 
     * @param doc
     *            {@link DocumentModel}
     * @param permission
     *            permission name
     * @param id
     *            user or group name
     * @throws Exception
     *             when could not get core session from space
     * @throws ClientException
     *             when space's {@link DocumentModel} could not be retrieved
     */
    // TODO unit test
    public void grantPermission(final DocumentModel doc,
            final String permission, final String id, final boolean override, CoreSession session)
            throws ClientException, Exception {
        final String logPrefix = "<grantPermission> ";
        log.debug(logPrefix + permission + ", " + id + ", " + override);
        if (override) {
            Predicate predicate = new Predicate() {
                public boolean evaluate(Object o) {
                    return id.equals(((LMPermission) o).getName());
                }
            };
            log.debug("removing all permissions for user " + id);
            List<LMPermission> userPerms = new ArrayList<LMPermission>(getPermissions(doc));
            CollectionUtils.filter(userPerms, predicate);
            for (LMPermission perm : userPerms) {
                log.debug("removing " + perm.getPermission());
                if (removePermission(doc, perm.getPermission(), id)) {
                    session.save();
                }
            }
            PermissionsHelper.addPermission(doc, permission, id, true);
        } else {
            if (!hasHigherPermission(doc, permission, id)) {
                removeLowerPermissions(doc, permission, id, session);
                PermissionsHelper.addPermission(doc, permission, id, true);
            }
        }
    }
    

    /**
     * TODO javadoc
     * 
     * @param doc
     *            {@link DocumentModel}
     * @param right
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Collection<String> getGroupsWithRight(final DocumentModel doc, final String right) throws Exception {
        final UserManager userManager = Framework.getService(UserManager.class);
        Predicate predicate = new Predicate() {
            public boolean evaluate(Object o) {
                NuxeoGroup group;
                try {
                    group = userManager.getGroup(((LMPermission) o).getName());
                } catch (ClientException e) {
                    return false;
                }
                return (right.equals(((LMPermission) o).getPermission()) && group != null);
            }
        };
        List<LMPermission> predList = getPermissions(doc);
        CollectionUtils.filter(predList, predicate);
        Collection<String> transList = CollectionUtils.collect(predList,
                TransformerUtils.invokerTransformer("getName"));
        return transList;
    }

    /**
     * TODO javadoc
     * 
     * @param doc
     *            {@link DocumentModel}
     * @param right
     * @return
     * @throws ClientException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Collection<String> getUsersWithRight(final DocumentModel doc, final String right) throws ClientException,
            Exception {
        final UserManager userManager = Framework.getService(UserManager.class);
        Predicate predicate = new Predicate() {
            public boolean evaluate(Object o) {
                NuxeoGroup group;
                // TODO 5.4.3
//                DocumentModelList groups = new DocumentModelListImpl();
//                boolean isGroup = false;
//                LMPermission lmPermission = (LMPermission) o;
                try {
                    group = userManager.getGroup(((LMPermission) o).getName());
                    // TODO 5.4.3
//                    groups = userManager.searchGroups(lmPermission.getName());
//                    for (DocumentModel group : groups) {
//                    	if (((String) group.getPropertyValue(userManager.getGroupIdField())).equals(lmPermission.getName())) {
//                    		isGroup = true;
//                    		break;
//                    	}
//                    }
                } catch (ClientException e) {
                    return false;
                }
                return (right.equals(((LMPermission) o).getPermission()) && group == null);
                // TODO 5.4.3
//                return (right.equals(lmPermission.getPermission()) && !isGroup);
            }
        };
        List<LMPermission> predList = getPermissions(doc);
        CollectionUtils.filter(predList, predicate);
        Collection<String> transList = CollectionUtils.collect(predList,
                TransformerUtils.invokerTransformer("getName"));
        return transList;
    }

    /**
     * TODO javadoc
     * 
     * @param doc
     *            {@link DocumentModel}
     * @param princ
     *            Principal
     * @return
     * @throws Exception
     */
    // TODO unit test
    public static boolean hasEverythingRight(final DocumentModel doc, final Principal princ) throws Exception {
        final String logPrefix = "<hasEverythingRight> ";
        log.debug(logPrefix + doc.getName() + ", " + princ);
        final NuxeoPrincipal currentPrinc = (NuxeoPrincipal) princ;
        Collection<String> gestionnaireGroups = getGroupsWithRight(doc, SecurityConstants.EVERYTHING);
        Collection<String> gestionnaireUsers = getUsersWithRight(doc, SecurityConstants.EVERYTHING);
        Predicate predicate = new Predicate() {
            public boolean evaluate(Object o) {
                return currentPrinc.isMemberOf((String) o);
            }
        };
        CollectionUtils.filter(gestionnaireGroups, predicate);
        return (!gestionnaireGroups.isEmpty() || gestionnaireUsers.contains(currentPrinc.getName()));
    }

    /**
     * TODO javadoc
     * 
     * @param doc
     *            {@link DocumentModel}
     * @param excludedId
     * @return
     * @throws IllegalArgumentException
     *             when <code>id</code> is a group and current user is not
     *             member of it.
     * @throws Exception
     */
    // TODO unit test
    public static boolean currentUserHasOtherEverythingRightThan(
            final DocumentModel doc, final String excludedId, CoreSession session) throws Exception {
        final String logPrefix = "<currentUserHasOtherGestionnairePermissionThan> ";
        log.debug(logPrefix + doc.getName() + ", " + excludedId);
        final NuxeoPrincipal currentPrinc = (NuxeoPrincipal) session.getPrincipal();
        UserManager userManager = Framework.getService(UserManager.class);
        NuxeoGroup excludedIdGroup = userManager.getGroup(excludedId);
        Collection<String> gestionnaireGroups = getGroupsWithRight(doc, SecurityConstants.EVERYTHING);
        Collection<String> gestionnaireUsers = getUsersWithRight(doc, SecurityConstants.EVERYTHING);
        if (excludedIdGroup != null) {
            // group
            if (!currentPrinc.isMemberOf(excludedIdGroup.getName()) && !gestionnaireUsers.contains(currentPrinc.getName())) {
                throw new IllegalArgumentException(
                        "current user is not member of specified group.");
            }
            gestionnaireGroups.remove(excludedId);
        } else {
            // user
            gestionnaireUsers.remove(excludedId);
        }
        Predicate predicate = new Predicate() {
            public boolean evaluate(Object o) {
                return currentPrinc.isMemberOf((String) o);
            }
        };
        CollectionUtils.filter(gestionnaireGroups, predicate);
        boolean retCode = (!gestionnaireGroups.isEmpty() || gestionnaireUsers.contains(currentPrinc.getName()));
        return retCode;
    }

    /**
     * TODO javadoc
     * 
     * @param doc
     *            {@link DocumentModel}
     * @param permission
     *            permission name
     * @param id
     *            user or group name
     * @return
     */
    // TODO unit test
    public boolean hasHigherPermission(final DocumentModel doc,
            final String permission, final String id) {
        final String logPrefix = "<hasHigherPermission> ";
        log.debug(logPrefix + permission + ", " + id);
        try {
            Iterator<String> it = reverseOrderedPermissions.listIterator(reverseOrderedPermissions.indexOf(permission));
            if (it.hasNext()) {
                it.next();
                while (it.hasNext()) {
                    String perm = it.next();
                    if (hasPermission(doc, perm, id)) {
                        log.debug("  has higher permission: " + perm + "/" + id);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
        }
        log.debug(logPrefix + "returns false");
        return false;
    }

    /**
     * TODO javadoc
     * 
     * @param space
     *            {@link DocumentModel}
     * @param permission
     *            permission name
     * @param id
     *            user or group name
     * @throws ClientException
     *             when could not save core session OR when 
     *             {@link DocumentModel} could not be retrieved
     * @throws Exception
     *             when could not get core session from space
     * @throws SpaceException
     *             when unable to get space provider's name of supplied space,
     */
    // TODO unit test
    private void removeLowerPermissions(final DocumentModel doc,
            final String permission, final String id, CoreSession session) throws ClientException, Exception {
        final String logPrefix = "<removeLowerPermissions> ";
        log.debug(logPrefix + permission + ", " + id);
        Iterator<String> it = orderedPermissions.listIterator(orderedPermissions.indexOf(permission));
        if (it.hasNext()) {
            it.next();
            while (it.hasNext()) {
                String perm = it.next();
                if (hasPermission(doc, perm, id)) {
                    log.debug("  removing " + perm + "/" + id + " first");
                    if (removePermission(doc, perm, id)) {
                        session.save();
                    }
                }
            }
        }
    }

    /**
     * TODO javadoc
     * 
     * @param doc
     *            {@link DocumentModel}
     * @param permission
     *            permission name
     * @param id
     *            user or group name
     * @return
     */
    // TODO unit test
    public boolean hasHigherOrEqualPermission(final DocumentModel doc,
            final String permission, final String id) {
        final String logPrefix = "<hasHigherOrEqualPermission> ";
        log.debug(logPrefix + permission + ", " + id);
        try {
            for (Iterator<String> it = reverseOrderedPermissions.listIterator(reverseOrderedPermissions.indexOf(permission)); it.hasNext();) {
                String perm = it.next();
                if (hasPermission(doc, perm, id)) {
                    log.debug("  has higher or equal permission: " + perm + "/"
                            + id);
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

}
