package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.io.Serializable;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public abstract class AbstractPage extends AbstractLabsBase implements Page {

    private static final String THIS_PAGE_IS_NOT_IN_A_LABS_SITE = "This page is not in a LabsSite";
    private static final String PG_DISPLAYABLE_DESCRIPTION = "pg:displayableDescription";
    private static final String PG_DISPLAYABLE_TITLE = "pg:displayableTitle";
    private static final String PG_COMMENTABLE = "pg:commentable";

    @Override
    public void setCommentable(boolean isCommentable) throws ClientException {
        doc.setPropertyValue(PG_COMMENTABLE, isCommentable);
    }

    @Override
    public boolean isCommentable() throws ClientException {
        Serializable propertyValue = doc.getPropertyValue(PG_COMMENTABLE);
        if (propertyValue instanceof Boolean) {
            return ((Boolean) propertyValue).booleanValue();
        }
        return false;
    }

    @Override
    public void setDisplayableTitle(boolean isDisplayableTitle) throws ClientException {
        doc.setPropertyValue(PG_DISPLAYABLE_TITLE, isDisplayableTitle);
    }

    @Override
    public boolean isDisplayableTitle() throws ClientException {
        Serializable propertyValue = doc.getPropertyValue(PG_DISPLAYABLE_TITLE);
        if (propertyValue instanceof Boolean) {
            return ((Boolean) propertyValue).booleanValue();
        }
        return false;
    }

    @Override
    public void setDisplayableDescription(boolean isDisplayableDescription) throws ClientException {
        doc.setPropertyValue(PG_DISPLAYABLE_DESCRIPTION, isDisplayableDescription);
    }

    @Override
    public boolean isDisplayableDescription() throws ClientException {
        Serializable propertyValue = doc.getPropertyValue(PG_DISPLAYABLE_DESCRIPTION);
        if (propertyValue instanceof Boolean) {
            return ((Boolean) propertyValue).booleanValue();
        }
        return false;
    }

    @Override
    public String getPath() throws ClientException {
        LabsSite site = getSite();
        if (site == null) {
            throw new IllegalArgumentException(THIS_PAGE_IS_NOT_IN_A_LABS_SITE);
        }
        return site.getURL()  + doc.getPathAsString()
                .replace(site.getTree()
                        .getPathAsString(), "");
    }

    private LabsSite getSite() throws ClientException {
        CoreSession session = doc.getCoreSession();
        DocumentModel parentDoc = doc;
        while (parentDoc != null && !parentDoc.getType()
                .equals(Docs.SITE.type())) {
            parentDoc = session.getDocument(parentDoc.getParentRef());
        }
        return parentDoc != null ? parentDoc.getAdapter(LabsSite.class) : null;
    }

    @Override
    public String[] getAllowedSubtypes() throws ClientException {
        return getAllowedSubtypes(doc);
    }

}
