package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.platform.types.Type;
import org.nuxeo.ecm.platform.types.TypeManager;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labstemplate.LabsTemplate;
import com.leroymerlin.corp.fr.nuxeo.labs.site.publisher.LabsPublisher;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.FacetNames;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteUtils;

public abstract class AbstractLabsBase  implements LabsBase{

    public static final String DC_TITLE = "dc:title";

	public static final String DC_DESCRIPTION = "dc:description";
    
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
        doc.setPropertyValue(DC_TITLE, title);
    }

    @Override
    public String getTitle() throws PropertyException, ClientException {
        return (String) doc.getPropertyValue(DC_TITLE);
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
        document.setPropertyValue(DC_DESCRIPTION, description);
    }

    @Override
    public String getDescription() throws PropertyException, ClientException {
        return (String) doc.getPropertyValue(DC_DESCRIPTION);
    }
    
    public abstract String[] getAllowedSubtypes(CoreSession session) throws ClientException;

    protected String[] getAllowedSubtypes(DocumentModel doc) throws ClientException {
        try {
            TypeManager typeManager = Framework.getService(TypeManager.class);
            Collection<Type> allowedSubTypes = typeManager.getAllowedSubTypes(doc.getType(), doc);
            List<String> list = new ArrayList<String>(allowedSubTypes.size());
            for (Type type : allowedSubTypes) {
                list.add(type.getId());
            }
            return list.toArray(new String[0]);
        } catch (Exception e) {
            throw ClientException.wrap(e);
        }
    }
    
    @Override
    public boolean isAuthorizedToDisplay(CoreSession session) throws ClientException{
        if (LabsSiteUtils.isOnlyRead(doc, session)){
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
    public boolean isDraft() throws ClientException {
        return doc.getAdapter(LabsPublisher.class).isDraft();
    }

    @Override
    public LabsTemplate getTemplate() throws ClientException {
        return doc.getAdapter(LabsTemplate.class);
    }

    @Override
    public void addFacetTemplate() {
        if (!doc.hasSchema(Schemas.LABSTEMPLATE.getName())){
            doc.addFacet(FacetNames.LABSTEMPLATE);
        }
    }

    @Override
    public boolean isAdministrator(String userName, CoreSession session) throws ClientException {
        try {
            UserManager userManager = Framework.getService(UserManager.class);
            NuxeoPrincipal principal = userManager.getPrincipal(userName);
            return session.hasPermission(principal, doc.getRef(), SecurityConstants.EVERYTHING);
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isContributor(String userName, CoreSession session) throws ClientException {
        try {
            UserManager userManager = Framework.getService(UserManager.class);
            NuxeoPrincipal principal = userManager.getPrincipal(userName);
            return session.hasPermission(principal, doc.getRef(), SecurityConstants.READ_WRITE);
        } catch (Exception e) {
            return false;
        }
    }

}
