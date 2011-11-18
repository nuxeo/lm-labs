package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.schema.DocumentType;
import org.nuxeo.ecm.core.schema.SchemaManager;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;

public abstract class AbstractLabsBase  implements LabsBase{

    protected DocumentModel doc;

    @Override
    public DocumentModel getDocument() {
        return doc;
    }

    @Override
    public void setTitle(String title) throws PropertyException,
            ClientException, IllegalArgumentException {
        if (title == null) {
            throw new IllegalArgumentException("title cannot be null.");
        }
        doc.setPropertyValue("dc:title", title);
    }

    @Override
    public String getTitle() throws PropertyException, ClientException {
        return (String) doc.getPropertyValue("dc:title");
    }

    @Override
    public void setDescription(String description) throws PropertyException,
            ClientException {
        setDescription(doc, description);
    }

    protected static void setDescription(DocumentModel document,
            String description) throws PropertyException, ClientException {
        if (description == null) {
            return;
        }
        document.setPropertyValue("dc:description", description);
    }

    @Override
    public String getDescription() throws PropertyException, ClientException {
        return (String) doc.getPropertyValue("dc:description");
    }
    
    public abstract String[] getAllowedSubtypes() throws ClientException;

    protected String[] getAllowedSubtypes(DocumentModel doc) throws ClientException {
        try {
            SchemaManager sm = Framework.getService(SchemaManager.class);
            DocumentType documentType = sm.getDocumentType(doc.getType());
            return documentType.getChildrenTypes();
        } catch (Exception e) {
            throw ClientException.wrap(e);
        }
    }
    
    @Override
    public boolean isAuthorizedToDisplay(String pUser, boolean pIsAnonymous) throws ClientException{
        String user = null;
        if (pIsAnonymous) {
            user = SecurityConstants.EVERYONE;
        } else {
            user = pUser;
        }
        if (LabsSiteUtils.isOnlyRead(doc, user)){
            return doc.getAdapter(LabsPublisher.class).isVisible();
        }
        return true;
    }

    @Override
    public boolean isDeleted() throws ClientException {
        return doc.getAdapter(LabsPublisher.class).isDeleted();
    }

    @Override
    public boolean isVisible() throws ClientException {
        return doc.getAdapter(LabsPublisher.class).isVisible();
    }

    @Override
    public boolean isAdministrator(String userName) throws ClientException {
        try {
            UserManager userManager = Framework.getService(UserManager.class);
            NuxeoPrincipal principal = userManager.getPrincipal(userName);
            return doc.getCoreSession().hasPermission(principal, doc.getRef(), SecurityConstants.EVERYTHING);
        } catch (Exception e) {
            throw ClientException.wrap(e);
        }
    }

}
