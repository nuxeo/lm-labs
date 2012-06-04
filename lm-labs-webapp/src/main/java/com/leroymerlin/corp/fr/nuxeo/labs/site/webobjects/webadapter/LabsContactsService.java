/**
 *
 */
package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.webadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.exceptions.WebResourceNotFoundException;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.common.core.security.GroupUserSuggest;
import com.leroymerlin.corp.fr.nuxeo.labs.site.contact.ContactDto;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.ContactHelper;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

/**
 * @author ephongsavanh
 * 
 */
@WebAdapter(name = "labscontacts", type = "LabsContacts")
public class LabsContactsService extends DefaultAdapter {

    @GET
    public Template doGet() throws ClientException {
        List<ContactDto> contactsDtoList = getContactDtoList();
        return getView("displayArray").arg("contactsAdmin", contactsDtoList);
    }

    @GET
    @Path("contactAdmin")
    public Template doGetContactsAdmin() throws ClientException {
        List<ContactDto> contactsDtoList = getContactDtoList();
        return getView("contact").arg("contactsAdmin", contactsDtoList);
    }

    @GET
    @Path("add")
    public Response addContact(@QueryParam(value = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return Response.status(Status.NOT_MODIFIED).build();
        }

        try {
            DocumentModel document = getTarget().getAdapter(DocumentModel.class);
            LabsSite labsSite = Tools.getAdapter(LabsSite.class, document, ctx.getCoreSession());
            if (labsSite.addContact(id)) {
                return Response.ok().build();
            }
        } catch (Exception e) {
            throw WebException.wrap(e);
        }

        return Response.status(Status.NOT_MODIFIED).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("suggestedUsers/{string}")
    @Produces("plain/text")
    public Template getSuggestedUsers(@PathParam("string") String str) {
        final String pattern = StringEscapeUtils.unescapeJavaScript(str);
        Map<String, Object> params = LabsSiteWebAppUtils.getSuggestedUsers(pattern);
        if (((List<GroupUserSuggest>) params.get("suggests")).size() > 0) {
            return getView("selectUsers").args(params);
        } else {
            return getView("selectUsers").arg("errorMessage", "Aucun résultat trouvé.");
        }
    }

    @DELETE
    @Path("delete")
    public Response deleteContact(@QueryParam(value = "ldap") String ldap)
            throws IllegalStateException, Exception {
        if (StringUtils.isBlank(ldap)) {
            return Response.status(Status.NOT_MODIFIED).build();
        }

        try {
            DocumentModel document = getTarget().getAdapter(DocumentModel.class);
            LabsSite labsSite = Tools.getAdapter(LabsSite.class, document, ctx.getCoreSession());
            if (labsSite.deleteContact(ldap)) {
                return Response.ok().build();
            }
        } catch (Exception e) {
            throw WebException.wrap(e);
        }

        return Response.status(Status.NOT_MODIFIED).build();
    }

    private List<ContactDto> getContactDtoList() throws ClientException {
        DocumentModel document = getTarget().getAdapter(DocumentModel.class);
        LabsSite labsSite = Tools.getAdapter(LabsSite.class, document, ctx.getCoreSession());
        List<String> contacts;
        try {
            contacts = labsSite.getContacts();
        } catch (Exception e) {
            throw new WebResourceNotFoundException(e.getMessage(), e);
        }

        if (contacts == null) {
            return null;
        }

        UserManager userManager = null;
        try {
            userManager = Framework.getService(UserManager.class);
        } catch (Exception e) {
            throw WebException.wrap(e);
        }

        List<ContactDto> contactsDtoList = new ArrayList<ContactDto>();
        for (String ldap : contacts) {
            ContactDto contactDto = ContactHelper.constructContactFromLdap(
                    ldap, userManager);
            if (contactDto != null) {
                contactsDtoList.add(contactDto);
            }
        }

        return contactsDtoList;
    }
}
