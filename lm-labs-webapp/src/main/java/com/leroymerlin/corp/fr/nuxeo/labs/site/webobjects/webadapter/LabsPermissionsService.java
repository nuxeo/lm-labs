/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.directory.SizeLimitExceededException;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.common.core.security.GroupUserSuggest;
import com.leroymerlin.common.core.security.LMPermission;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.GroupsUsersSuggestHelper;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Rights;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.PermissionsHelper;

/**
 * @author fvandaele
 * 
 */
@WebAdapter(name = "labspermissions", type = "LabsPermissions")
public class LabsPermissionsService extends DefaultAdapter {

    public enum Action {
        GRANT, REMOVE;
    }

    private static final Log log = LogFactory.getLog(LabsPermissionsService.class);

    private static final String THE_RIGHT_LIST_DONT_BE_NULL = "The right'list dont be null !";

    @GET
    public Object doGet() {
        List<LMPermission> permissions = extractPermissions(getDocument());
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
        return getView("displayArray").arg("permissionsAdmin", permissionsAdmin).arg(
                "permissionsWrite", permissionsWrite).arg("permissionsRead",
                permissionsRead);
    }

    @GET
    @Path("permAdmin")
    public Object doGetPermissionsAdmin() {
        List<LMPermission> permissions = extractPermissions(getDocument());
        List<LMPermission> permissionsAdmin = new ArrayList<LMPermission>();
        Map<String, String> usernameAndEmail = new HashMap<String, String>(
                permissionsAdmin.size());
        UserManager userManager = null;
        try {
            userManager = Framework.getService(UserManager.class);
        } catch (Exception e) {
            throw WebException.wrap(e);
        }

        for (LMPermission perm : permissions) {
            if (Rights.EVERYTHING.getRight().equals(perm.getPermission())) {
                permissionsAdmin.add(perm);
                try {
                    NuxeoPrincipal principal = userManager.getPrincipal(perm.getName());
                    usernameAndEmail.put(perm.getName(), principal.getEmail());
                } catch (ClientException e) {
                    throw WebException.wrap(e);
                }
            }
        }

        return getView("admin_contact").arg("permissionsAdmin",
                permissionsAdmin).arg("usernameAndEmail", usernameAndEmail);
    }

    private List<LMPermission> extractPermissions(DocumentModel document) {
        try {
            return LabsSiteUtils.extractPermissions(document);
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
    }

    /**
     * @return
     */
    private DocumentModel getDocument() {
        DocumentObject dobj = (DocumentObject) getTarget();
        DocumentModel document = dobj.getDocument();
        return document;
    }

    @GET
    @Path("suggestedUsers/{string}")
    @Produces("plain/text")
    public Object getSuggestedUsers(@PathParam("string") String str) {
        final String pattern = StringEscapeUtils.unescapeJavaScript(str);
        Map<String, Object> params = new HashMap<String, Object>();
        List<GroupUserSuggest> suggests;
        try {
            suggests = GroupsUsersSuggestHelper.getSuggestions(pattern);
            params.put("suggests", suggests);
        } catch (SizeLimitExceededException e) {
            params.put("errorMessage",
                    "Trop de resultats, veuillez affiner votre recherche.");
        } catch (ClientException e) {
            params.put("errorMessage", e.getMessage());
        }
        return getView("selectUsers").args(params);
    }

    @GET
    @Path("haspermission")
    public Response hasPermission(
            @QueryParam(value = "permission") String permission,
            @QueryParam(value = "id") String id) {
        DocumentModel document = getDocument();
        try {
            return Response.ok(
                    Boolean.toString(PermissionsHelper.hasPermission(document,
                            permission, id))).build();
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
    }

    @GET
    @Path("higherpermission")
    public Response hasHigherPermission(
            @QueryParam(value = "permission") String permission,
            @QueryParam(value = "id") String id) {
        List<String> labsRightsString = getListRights();
        DocumentModel document = getDocument();
        try {
            PermissionsHelper helper = new PermissionsHelper(labsRightsString);
            return Response.ok(
                    Boolean.toString(helper.hasHigherPermission(document,
                            permission, id))).build();
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
    }

    /**
     * @return
     */
    private List<String> getListRights() {
        List<String> labsRightsString = new ArrayList<String>();

        for (Rights labsRight : Rights.values()) {
            labsRightsString.add(labsRight.getRight());
        }
        if (labsRightsString.isEmpty()) {
            throw new WebException(THE_RIGHT_LIST_DONT_BE_NULL);
        }
        return labsRightsString;
    }

    @POST
    @Path("add")
    public Response postPermission(
            @FormParam(value = "permission") String permission,
            @FormParam(value = "id") String id,
            @FormParam(value = "override") String overrideStr) {
        DocumentModel document = getDocument();
        try {
            boolean override = BooleanUtils.toBoolean(overrideStr);
            assert StringUtils.isNotEmpty(permission);
            assert StringUtils.isNotEmpty(id);
            setPermission(document, permission, id, Action.GRANT, override);
            return Response.ok().build();

        } catch (IllegalStateException e1) {
            return Response.ok(ctx.getMessage(e1.getMessage()),
                    MediaType.TEXT_PLAIN).build();
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
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
    public void setPermission(DocumentModel doc, final String permission,
            final String id, Action action, boolean override)
            throws IllegalStateException, Exception {
        if (doc != null) {
            CoreSession session = ctx.getCoreSession();
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

    @DELETE
    @Path("delete")
    public Response deletePermission(
            @QueryParam(value = "permission") String permission,
            @QueryParam(value = "id") String id) throws IllegalStateException,
            Exception {
        DocumentModel document = getDocument();
        assert StringUtils.isNotEmpty(permission);
        assert StringUtils.isNotEmpty(id);
        setPermission(document, permission, id, Action.REMOVE, false);
        return Response.ok().build();
    }

}
