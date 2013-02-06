package com.leroymerlin.corp.fr.nuxeo.labs.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
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
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

public abstract class AbstractLabsBase extends LabsAdapterImpl implements LabsBase, LabsElementTemplate{

	private static final String THE_CORE_SESSION_SETTED = "The coreSession setted in this adpater is null. The coreSession comes from document : ";
	public static final String DC_TITLE = "dc:title";
	public static final String DC_DESCRIPTION = "dc:description";

    public static final String PROPERTY_ELEMENT_TEMPLATE_PREVIEW = Schemas.LABS_ELEMENT_TEMPLATE.prefix() + ":preview";
    
    protected static final Log LOG = LogFactory.getLog(AbstractLabsBase.class);

    public AbstractLabsBase(DocumentModel document) {
		super(document);
	}
    
    @Override
    public CoreSession getSession() {
    	CoreSession coreSession = super.getSession();
		if (coreSession == null){
			LOG.error(THE_CORE_SESSION_SETTED + "(" + doc.getRef() + "/" + doc.getType() + "/" + this.getClass().getName() + ")");
			doc.getCoreSession();
		}
		return coreSession;
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
    
    public abstract String[] getAllowedSubtypes() throws ClientException;

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
    public boolean isAuthorizedToDisplay() throws ClientException{
        if (LabsSiteUtils.isOnlyRead(doc, getSession())){
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
        return Tools.getAdapter(LabsTemplate.class, doc, getSession());
    }

    @Override
    public void addFacetTemplate() {
        if (!doc.hasSchema(Schemas.LABSTEMPLATE.getName())){
            doc.addFacet(FacetNames.LABSTEMPLATE);
        }
    }

    @Override
    public boolean isAdministrator(String userName) throws ClientException {
        try {
            UserManager userManager = Framework.getService(UserManager.class);
            NuxeoPrincipal principal = userManager.getPrincipal(userName);
            return getSession().hasPermission(principal, doc.getRef(), SecurityConstants.EVERYTHING);
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isContributor(String userName) throws ClientException {
        try {
            UserManager userManager = Framework.getService(UserManager.class);
            NuxeoPrincipal principal = userManager.getPrincipal(userName);
            return getSession().hasPermission(principal, doc.getRef(), SecurityConstants.READ_WRITE);
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isElementTemplate() throws ClientException{
        return doc.hasSchema(Schemas.LABS_ELEMENT_TEMPLATE.getName());
    }
    
    @Override
    public void setElementTemplate(boolean isPageTemplate) throws ClientException{
    	if (isPageTemplate && !doc.hasSchema(Schemas.LABS_ELEMENT_TEMPLATE.getName())){
            doc.addFacet(FacetNames.LABS_ELEMENT_TEMPLATE);
        }
    	if (!isPageTemplate && doc.hasSchema(Schemas.LABS_ELEMENT_TEMPLATE.getName())){
    		doc.removeFacet(FacetNames.LABS_ELEMENT_TEMPLATE);
    	}
    }

    @Override
    public void setElementPreview(Blob blob) throws ClientException{
    	if (doc.hasSchema(Schemas.LABS_ELEMENT_TEMPLATE.getName())){
    		doc.setPropertyValue(PROPERTY_ELEMENT_TEMPLATE_PREVIEW,
                (Serializable) blob);
    	}
    }

    @Override
    public Blob getElementPreview() throws ClientException{
    	if (doc.hasSchema(Schemas.LABS_ELEMENT_TEMPLATE.getName())){
    		return (Blob) doc.getPropertyValue(PROPERTY_ELEMENT_TEMPLATE_PREVIEW);
    	}
    	return null;
    }
    
    @Override
    public boolean hasElementPreview() throws ClientException{
        return getElementPreview() != null;
    }

}
