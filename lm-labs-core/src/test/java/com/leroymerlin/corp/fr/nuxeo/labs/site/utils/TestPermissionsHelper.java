/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoGroup;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.leroymerlin.common.core.security.LMPermission;
import com.leroymerlin.common.core.security.PermissionsHelper;
import com.leroymerlin.common.core.security.SecurityData;
import com.leroymerlin.common.core.security.SecurityDataHelper;

/**
 * @author <a href="mailto:vincent.dutat@ext.leroymerlin.fr">Vincent Dutat</a>
 *
 */
@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class })
public class TestPermissionsHelper {

    private static final Log log = LogFactory.getLog(TestPermissionsHelper.class);

    private static final String FAKE_USERNAME2 = "CameronDiaz";

    private static final String FAKE_USERNAME1 = "TomCruise";

    private static final String TEST_GROUP = "FR-LM-MAIL-VILLENEUVE";

    private static final String TEST_GROUP_IN_PERMISSIONSLIST = "FR-LM-MAIL-CD-VILLENEUVE";

    private static final String TEST_DISPLAY_NAME = "ARNAUD RRMATERIAUX";

    private static final String TEST_USERNAME = "RR";

    @Inject
    UserManager um;

    private CoreSession session;

    @Before
    public void doBefore() throws Exception {
        List<String> groups = ImmutableList.<String> builder().add(TEST_GROUP).add(
                TEST_GROUP_IN_PERMISSIONSLIST).build();

        for (String groupName : groups) {
            NuxeoGroup group = um.getGroup(groupName);
            if (group == null) {
                DocumentModel groupDoc = um.getBareGroupModel();
                groupDoc.setPropertyValue("group:groupname", groupName);
                um.createGroup(groupDoc);
                assertNotNull(um.getGroup(groupName));
            }
        }

        NuxeoPrincipal user = um.getPrincipal(TEST_USERNAME);
        if (user == null) {
            DocumentModel userDoc = um.getBareUserModel();
            userDoc.setPropertyValue("user:username", TEST_USERNAME);
            userDoc.setPropertyValue("user:firstName", "ARNAUD");
            userDoc.setPropertyValue("user:lastName", "RRMATERIAUX");
            um.createUser(userDoc);
        }
    }

    @Inject
    public TestPermissionsHelper(CoreSession session) throws Exception {
        this.session = session;
        // System.out.println("current principal:" +
        // session.getPrincipal().getName());
        DocumentModel folder = session.createDocumentModel("/", "myfolder",
                "Folder");
        folder = session.createDocument(folder);
        session.save();

        SecurityData buildSecurityData = SecurityDataHelper.buildSecurityData(folder);
        buildSecurityData.addModifiablePrivilege(TEST_GROUP_IN_PERMISSIONSLIST,
                SecurityConstants.READ, true);
        buildSecurityData.addModifiablePrivilege(TEST_USERNAME,
                SecurityConstants.READ, true);
        SecurityDataHelper.updateSecurityOnDocument(folder, buildSecurityData);
    }

    @Test
    public void userManagerInjected() throws Exception {
        assertNotNull(um);
    }

    @Test
    public void someUsersAndGroupsExistOrNot() throws ClientException {
        assertTrue(PermissionsHelper.groupOrUserExists(TEST_GROUP));
        assertTrue(PermissionsHelper.groupOrUserExists("Administrator"));
        assertFalse(PermissionsHelper.groupOrUserExists(FAKE_USERNAME1));
    }

    @Test
    public void groupsHaveNoDisplayName() throws Exception {
        assertTrue("".equals(PermissionsHelper.getDisplayName(TEST_GROUP)));
    }

    @Test
    public void folderExists() throws Exception {
        assertTrue(session.exists(new PathRef("/myfolder")));
    }

    @Test
    public void groupVILLENEUVEisInPermissionsList() throws Exception {
        DocumentModel folder = session.getDocument(new PathRef("/myfolder"));
        assertNotNull(folder);
        List<LMPermission> permissions = PermissionsHelper.getPermissions(folder);
        assertTrue(permissions.size() > 0);
        assertTrue(CollectionUtils.exists(permissions, new Predicate() {

            public boolean evaluate(Object o) {
                return ((LMPermission) o).getName().equals(
                        TEST_GROUP_IN_PERMISSIONSLIST);
            }
        }));
    }

    @Test
    public void iCanAddPermissionsForTestUser() throws Exception {
        DocumentModel folder = session.getDocument(new PathRef("/myfolder"));
        assertNotNull(folder);
        PermissionsHelper.addPermission(folder, SecurityConstants.READ,
                TEST_USERNAME, true);
        List<LMPermission> permissions = PermissionsHelper.getPermissions(folder);

        assertTrue(permissions.size() > 0);
        assertTrue(CollectionUtils.exists(permissions, new Predicate() {

            public boolean evaluate(Object o) {
                return (((LMPermission) o).getName().equals(TEST_USERNAME) && ((LMPermission) o).getPermission().equals(
                        SecurityConstants.READ));
            }
        }));
    }

    @Test
    public void iCanGetDisplayNameOfTestUser() throws Exception {
        DocumentModel folder = session.getDocument(new PathRef("/myfolder"));
        assertNotNull(folder);
        List<LMPermission> permissions = PermissionsHelper.getPermissions(folder);
        assertTrue(CollectionUtils.exists(permissions, new Predicate() {

            public boolean evaluate(Object o) {
                return (((LMPermission) o).getName().equals(TEST_USERNAME) && TEST_DISPLAY_NAME.equals(((LMPermission) o).getDisplayName()));
            }
        }));
    }

    @Test
    public void testGroupHasNoDisplayName() throws Exception {
        DocumentModel folder = session.getDocument(new PathRef("/myfolder"));
        assertNotNull(folder);
        List<LMPermission> permissions = PermissionsHelper.getPermissions(folder);
        assertTrue(CollectionUtils.exists(permissions, new Predicate() {

            public boolean evaluate(Object o) {
                return (((LMPermission) o).getName().equals(
                        TEST_GROUP_IN_PERMISSIONSLIST) && "".equals(((LMPermission) o).getDisplayName()));
            }
        }));
    }

    @Test
    public void hasSomePermissions() throws Exception {
        DocumentModel folder = session.getDocument(new PathRef("/myfolder"));
        assertNotNull(folder);

        assertTrue(PermissionsHelper.hasPermission(folder,
                SecurityConstants.READ, TEST_GROUP_IN_PERMISSIONSLIST));
        assertTrue(PermissionsHelper.hasPermission(folder,
                SecurityConstants.READ, TEST_USERNAME));

        printPermissions(PermissionsHelper.getPermissions(folder));

        Collection<String> groups = PermissionsHelper.getGroupsWithRight(
                folder, SecurityConstants.READ);
        assertTrue(groups.size() > 0);
        assertTrue(groups.contains(TEST_GROUP_IN_PERMISSIONSLIST));

        Collection<String> users = PermissionsHelper.getUsersWithRight(folder,
                SecurityConstants.READ);
        assertTrue(users.size() > 0);
        assertTrue(users.contains(TEST_USERNAME));
    }

    @Test
    public void iCanRemovePermissions() throws Exception {
        DocumentModel folder = session.getDocument(new PathRef("/myfolder"));
        assertNotNull(folder);
        List<LMPermission> permissions = PermissionsHelper.getPermissions(folder);
        int nbr = permissions.size();
        assertTrue(PermissionsHelper.removePermission(folder,
                SecurityConstants.READ, TEST_GROUP_IN_PERMISSIONSLIST));
        assertFalse(PermissionsHelper.hasPermission(folder,
                SecurityConstants.READ, TEST_GROUP_IN_PERMISSIONSLIST));
        permissions = PermissionsHelper.getPermissions(folder);
        nbr--;
        assertTrue(nbr == permissions.size());

        assertTrue(PermissionsHelper.removePermission(folder,
                SecurityConstants.READ, TEST_USERNAME));
        assertFalse(PermissionsHelper.hasPermission(folder,
                SecurityConstants.READ, TEST_USERNAME));
        permissions = PermissionsHelper.getPermissions(folder);
        nbr--;
        assertTrue(nbr == permissions.size());

        assertFalse(PermissionsHelper.removePermission(folder,
                SecurityConstants.READ, FAKE_USERNAME2));
        permissions = PermissionsHelper.getPermissions(folder);
        assertTrue(nbr == permissions.size());
    }

    @SuppressWarnings("deprecation")
    @Test
    public void currentUserStillHasEverythingRight() throws Exception {
        DocumentModel folder = session.getDocument(new PathRef("/myfolder"));
        assertNotNull(folder);
        assertTrue(PermissionsHelper.currentUserHasOtherEverythingRightThan(
                folder, TEST_GROUP_IN_PERMISSIONSLIST, session));
        PermissionsHelper.addPermission(folder, SecurityConstants.EVERYTHING,
                SecurityConstants.ADMINISTRATORS, true);
        assertTrue(PermissionsHelper.currentUserHasOtherEverythingRightThan(
                folder, SecurityConstants.ADMINISTRATORS, session));
    }

    /**
     * @param permissions
     */
    private void printPermissions(List<LMPermission> permissions) {
        log.debug("====");
        for (LMPermission lmPermission : permissions) {
            log.debug("====" + lmPermission.getName() + "/"
                    + lmPermission.getDisplayName() + "/"
                    + lmPermission.getPermission());
        }
    }

    @Test
    public void hasHigherOrEqualPermission() throws Exception {
        DocumentModel docu = session.createDocumentModel("/", "myfolder",
                "Folder");
        docu = session.createDocument(docu);
        session.save();

        List<String> orderedPermissions = new ArrayList<String>();
        orderedPermissions.add(SecurityConstants.EVERYTHING);
        orderedPermissions.add(SecurityConstants.READ_WRITE);
        orderedPermissions.add(SecurityConstants.READ);
        PermissionsHelper helper = new PermissionsHelper(orderedPermissions);
        PermissionsHelper.addPermission(docu, SecurityConstants.READ, "toto",
                true);

        assertTrue(PermissionsHelper.hasPermission(docu,
                SecurityConstants.READ, "toto"));
        assertTrue(!helper.hasHigherOrEqualPermission(docu,
                SecurityConstants.READ_WRITE, "toto"));
    }

    // @AfterClass public static void uninstall() throws NamingException {
    // if (NuxeoContainer.isInstalled()) {
    // NuxeoContainer.uninstall();
    // }
    // System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
    // }
}
