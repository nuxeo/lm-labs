package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.IdUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.common.core.security.LMPermission;
import com.leroymerlin.common.core.security.PermissionsHelper;
import com.leroymerlin.common.core.security.SecurityData;
import com.leroymerlin.common.core.security.SecurityDataHelper;
import com.leroymerlin.corp.fr.nuxeo.labs.base.AbstractLabsBase;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.LabsSecurityException;
import com.leroymerlin.corp.fr.nuxeo.labs.site.html.HtmlPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Rights;


public final class LabsSiteUtils {

    private static final String IMPOSSIBLE_TO_VERIFY_THE_PERMISSION = "Impossible to verify the permissions only 'Read'";
    private static final Log log = LogFactory.getLog(LabsSiteUtils.class);

    public static String beforeConversion = "\u00e0\u00c0\u00e2\u00c2\u00e4\u00c4\u00e1\u00c1\u00e9\u00c9\u00e8\u00c8\u00ea\u00ca\u00eb\u00cb\u00ec\u00cc\u00ee\u00ce\u00ef\u00cf\u00f2\u00d2\u00f4\u00d4\u00f6\u00d6\u00f9\u00d9\u00fb\u00db\u00fc\u00dc\u00e7\u00c7\u2019\u00f1 /\\.,;?!'\"$&%()[]{}@^\u00a7*:<>+=\u00b0#";
    public static String afterConversion =  "aAaAaAaAeEeEeEeEiIiIiIoOoOoOuUuUuUcC'n------------------------------";
    public static int maxSizeCharacterName = 64;

    public static final String COPYOF_PREFIX = "Copie de ";

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
    public static boolean isOnlyRead(final DocumentModel doc, final CoreSession session){
        boolean result = false;
        try {
            result = !session.hasPermission(doc.getRef(), SecurityConstants.READ_WRITE);
            if (result){
                result = session.hasPermission(doc.getRef(), SecurityConstants.READ);
            }
        } catch (Exception e) {
            log.error(IMPOSSIBLE_TO_VERIFY_THE_PERMISSION, e);
            result = false;
        }
        return result;
    }

    public static DocumentModel getPageName(String name, DocumentRef parentRef, CoreSession session) throws ClientException {
        DocumentModel parent = session.getDocument(parentRef);
        PathRef pathRef = new PathRef(parent.getPathAsString() + "/" + doLabsSlugify(name));
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
        UserManager userManager = Framework.getService(UserManager.class);
        List<String> excludedUsersList = new ArrayList<String>();
        excludedUsersList.addAll(userManager.getAdministratorsGroups());
        excludedUsersList.add(userManager.getDefaultGroup());
        excludedUsersList.add(SecurityConstants.ADMINISTRATOR); // TODO
        final List<String> users = new ArrayList<String>(excludedUsersList);
        CollectionUtils.filter(permissions, new Predicate() {
            public boolean evaluate(Object o) {
                return ((LMPermission) o).isGranted()
                        && !users.contains(
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
    public static void unblockInherits(String permission, DocumentModel document, final CoreSession session) throws ClientException,Exception {
        if(StringUtils.isEmpty(permission)){
            SecurityData sd = SecurityDataHelper.buildSecurityData(document);
            sd.setBlockRightInheritance(false, null);
            SecurityDataHelper.updateSecurityOnDocument(document, sd);
            session.save();
            List<LMPermission> permissions = extractPermissions(document);
            deletePagePermissions(document, permissions, session);
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
                    setPermission(document, perm.permission, perm.getName(), Action.GRANT, true, session);
                }
            }
            if (Rights.READ.getRight().equals(permission)){
                for (LMPermission perm : permissionsRead) {
                    setPermission(document, perm.permission, perm.getName(), Action.GRANT, true, session);
                }
            }
        }

        final DocumentRef ref = document.getRef();
        UnrestrictedSessionRunner runner = new UnrestrictedSessionRunner(session){

            @Override
            public void run() throws ClientException {
                DocumentModel docu = session.getDocument(ref);
                try{
                    deleteDuplicate(docu, session);
                } catch (Exception e) {
                    throw new LabsSecurityException("Problem for change permissions", e);
                }
                session.save();
            }

        };
        runner.runUnrestricted();

        deleteDuplicate(document, session);
    }

    /**
     * @param document
     * @throws Exception
     */
    private static void deleteDuplicate(DocumentModel document, CoreSession session) throws Exception {
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
        deleteUnusedPermissions(document, permissionsAdmin, mapPerm, session);
        mapPerm = new HashMap<String, LMPermission>();
        deleteUnusedPermissions(document, permissionsWrite, mapPerm, session);
        mapPerm = new HashMap<String, LMPermission>();
        deleteUnusedPermissions(document, permissionsRead, mapPerm, session);
    }

    /**
     * @param document
     * @param permissions
     * @param mapPerm
     * @throws Exception
     */
    private static void deleteUnusedPermissions(DocumentModel document,
            List<LMPermission> permissions, Map<String, LMPermission> mapPerm, CoreSession session)
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
            setPermission(document, perm.permission, perm.getName(), Action.REMOVE, true, session);
        }
    }

    /**
     * @param document
     * @throws Exception
     */
    private static void deletePagePermissions(DocumentModel document, List<LMPermission> permissions, CoreSession session) throws Exception {
        for (LMPermission perm : permissions) {
            if(!perm.isInherited()){
                setPermission(document, perm.permission, perm.getName(), Action.REMOVE, true, session);
            }

        }
    }



    /**
     * @return
     * @throws LabsSecurityException
     */
    public static List<String> getListRights() throws LabsSecurityException {
        List<String> labsRightsString = new ArrayList<String>();

        for (Rights labsRight : Rights.values()) {
            labsRightsString.add(labsRight.getRight());
        }
        if (labsRightsString.isEmpty()) {
            throw new LabsSecurityException(THE_RIGHT_LIST_DONT_BE_NULL);
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
            final String id, Action action, boolean override, CoreSession session)
            throws IllegalStateException, LabsSecurityException, Exception {
        if (doc != null) {
            if (!PermissionsHelper.hasPermission(doc, permission, id)) {
                if (!PermissionsHelper.groupOrUserExists(id)) {
                    // // TODO throw exception to notify unknow group/user
                    // log.error("Failed to get principal:" + e);
                    throw new LabsSecurityException("Unknown group or user");
                    // return;
                }
                boolean granted = Action.GRANT.equals(action);
                if (granted) {
                    try {
                        List<String> labsRightsString = getListRights();
                        PermissionsHelper helper = new PermissionsHelper(
                                labsRightsString);
                        if (!override
                                && helper.hasHigherPermission(doc, permission,
                                        id)) {
                            throw new IllegalStateException(
                                    "message.security.permission.hasHigherPermission");
                        }
                        helper.grantPermission(doc, permission, id, override, session);
                        session.save();
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
                        session.save();
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

    public static DocumentModelList getChildrenPageDocuments(DocumentModel document, final CoreSession session) throws ClientException {
        @SuppressWarnings("serial")
        DocumentModelList children = session.getChildren(document.getRef(), null, new Filter() {
            @Override
            public boolean accept(DocumentModel document) {
                return (Tools.getAdapter(Page.class, document, session) != null);
            }}, null);
        return children;
    }

    /**
     * Slugify for the name of document
     * @param title the title/name of document
     * @param maxSize the maxsize of character
     * @return the slugified name
     */
    public static String doLabsSlugify(String title){
        String s = title;
        if (StringUtils.isEmpty(s)) {
            return IdUtils.generateStringId();
        }
        s = s.trim();
        if (s.length() > maxSizeCharacterName) {
            s = s.substring(0, maxSizeCharacterName).trim();
        }

//        for (int i = 0; i < beforeConversion.length(); i++) {
//             s = s.replace(beforeConversion.charAt(i), afterConversion.charAt(i));
//        }
        char[] charArray = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean beforeIsTiret = false;
        char tiret = '-';
        for (int i=0;i < charArray.length; i++){
            if(charArray[i] == tiret){
                if(!beforeIsTiret){
                    sb.append(tiret);
                    beforeIsTiret = true;
                }
            }
            else{
                sb.append(charArray[i]);
                beforeIsTiret = false;
            }
        }

        return sb.toString();
    }

    // TODO it looks like Nuxeo does NOT copy schemas of dynamically added facets !!! see NXP-8242. FIXED in 5.5.0-HF01
    /**
     * it looks like Nuxeo does NOT copy schemas of dynamically added facets !!! see NXP-8242. FIXED in 5.5.0-HF01
     * @param src
     * @param dest
     * @param name
     * @param session
     * @param withElementTemplate
     * @return
     * @throws ClientException
     */
    public static DocumentModel copyHierarchyPage(DocumentRef src, DocumentRef dest, String name, String title, CoreSession session, boolean withElementTemplate) throws ClientException{
        DocumentModel copyPage = copyPage(src, dest, name, title, session, withElementTemplate);
        if (!src.toString().equals(dest.toString())){
            for (DocumentModel document: session.getChildren(src)){
                copyHierarchyPage(document.getRef(), copyPage.getRef(), document.getName(), document.getTitle(), session, withElementTemplate);
            }
        }
        return copyPage;
    }

    // TODO it looks like Nuxeo does NOT copy schemas of dynamically added facets !!! see NXP-8242. FIXED in 5.5.0-HF01
    /**
     * it looks like Nuxeo does NOT copy schemas of dynamically added facets !!! see NXP-8242. FIXED in 5.5.0-HF01
     * @param src
     * @param dest
     * @param name
     * @param session
     * @param withElementTemplate
     * @return
     * @throws ClientException
     */
    public static DocumentModel copyPage(DocumentRef src, DocumentRef dest, String name, String title, CoreSession session, boolean withElementTemplate) throws ClientException{
        DocumentModel docDest = session.getDocument(dest);
        DocumentModel docSrc = session.getDocument(src);
        DocumentModel copy = session.createDocumentModel(docDest.getPathAsString(), LabsSiteUtils.doLabsSlugify(name), docSrc.getType());
        copy = session.createDocument(copy);
        for (String facetName: docSrc.getFacets()){
            if (withElementTemplate || !LabsSiteConstants.FacetNames.LABS_ELEMENT_TEMPLATE.equals(facetName)){
                copy.addFacet(facetName);
            }
        }
        for (String schemaName: docSrc.getSchemas()){
            copy.setProperties(schemaName, docSrc.getProperties(schemaName));
        }
        copy.setPropertyValue(AbstractLabsBase.DC_TITLE, title);
        copy = session.saveDocument(copy);
        session.save();
        return copy;
    }

    public static boolean isLabsSiteUrlAvailable(CoreSession session, final String url) {
        try {
            UnrestrictedSessionRunner unrestricted = new UnrestrictedSessionRunner(session) {
                @Override
                public void run() throws ClientException {
                    StringBuilder str = new StringBuilder();
                    str.append("SELECT * FROM Document WHERE ");
                    str.append(NXQL.ECM_PRIMARYTYPE).append(" = '").append(Docs.SITE.type()).append("'")
                    .append(" AND ")
                    .append("webc:url = '").append(url).append("'");
                    DocumentModelList sites = session.query(str.toString());
                    if (!sites.isEmpty()) {
                        throw new ClientException("URL " + url + " is already used.");
                    }
                }
            };
            unrestricted.runUnrestricted();
        } catch (Exception e) {
            log.error(e);
            return false;
        }
        return true;
    }

    public static DocumentModel createSidebarPage(DocumentModel doc, CoreSession session) throws ClientException {
        DocumentModel sidebar = session.createDocumentModel(
                doc.getPathAsString(),
                LabsSiteConstants.Docs.SIDEBAR.docName(), LabsSiteConstants.Docs.SIDEBAR.type());
        sidebar.setPropertyValue("dc:title", StringUtils.capitalize(LabsSiteConstants.Docs.SIDEBAR.docName()));
        sidebar = session.createDocument(sidebar);
        HtmlPage page = Tools.getAdapter(HtmlPage.class, sidebar, session);
        page.addSection().addRow().addContent(0, "");
        session.saveDocument(page.getDocument());
        return sidebar;
    }
}
