package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.notification.MailNotification;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;

public abstract class AbstractPage extends AbstractLabsBase implements Page {

    private static final String THIS_PAGE_IS_NOT_IN_A_LABS_SITE = "This page is not in a LabsSite";
    private static final String PG_COMMENTABLE = Schemas.PAGE.prefix() + ":commentable";
    private static final String PG_DISPLAYABLE_PARAMETERS = Schemas.PAGE.prefix() + ":displayableParameters";

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

    @Override
    public List<String> getNotDisplayableParameters() throws ClientException {
        @SuppressWarnings("unchecked")
        List<String> propertyValue = (List<String>) doc.getPropertyValue(PG_DISPLAYABLE_PARAMETERS);
        if (propertyValue == null){
            propertyValue = new ArrayList<String>();
        }
        return propertyValue;
    }

    @Override
    public void setNotDisplayableParameters(List<String> properties) throws ClientException {
        doc.getProperty(PG_DISPLAYABLE_PARAMETERS).setValue(properties);
    }

    @Override
    public boolean isDisplayable(String fieldName) throws ClientException {
        List<String> parameters = getNotDisplayableParameters();
        return !parameters.contains(fieldName);
    }

    @Override
    public Calendar getLastNotified() throws ClientException {
        try {
            return doc.getAdapter(MailNotification.class).getLastNotified();
        } catch (Exception e) {
            return null;
        }
    }

}
