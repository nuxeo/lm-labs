package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.usermanager.UserManager;

import com.leroymerlin.corp.fr.nuxeo.labs.site.contact.ContactDto;

public class ContactHelper {
    private ContactHelper() {
    }

    public static ContactDto constructContactFromLdap(String ldap,
            UserManager userManager) throws ClientException {
        ContactDto contact = null;
        for (DocumentModel user : userManager.searchUsers(ldap)) {
            String username = (String) user.getProperty(
                    userManager.getUserSchemaName(), "username");
            String firstName = (String) user.getProperty(
                    userManager.getUserSchemaName(), "firstName");
            String lastName = (String) user.getProperty(
                    userManager.getUserSchemaName(), "lastName");
            NuxeoPrincipal principal = userManager.getPrincipal(username);

            contact = new ContactDto(ldap, firstName + " " + lastName,
                    principal.getEmail());

            break;
        }

        return contact;
    }
}
