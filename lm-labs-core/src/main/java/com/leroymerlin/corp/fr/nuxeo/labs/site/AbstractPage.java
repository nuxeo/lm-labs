package com.leroymerlin.corp.fr.nuxeo.labs.site;

import java.io.Serializable;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.notification.MailNotification;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public abstract class AbstractPage extends AbstractLabsBase implements Page {

    @Override
    public void setCommentable(boolean isCommentable) throws ClientException {
        doc.setPropertyValue("pg:commentable", isCommentable);
        doc.getAdapter(MailNotification.class).setAsToBeNotified();
    }

    @Override
    public void setTitle(String title) throws PropertyException,
            ClientException, IllegalArgumentException {
        super.setTitle(title);
        doc.getAdapter(MailNotification.class).setAsToBeNotified();
    }

    @Override
    public boolean isCommentable() throws ClientException {
        Serializable propertyValue = doc.getPropertyValue("pg:commentable");
        if (propertyValue instanceof Boolean) {
            return ((Boolean) propertyValue).booleanValue();
        }
        return false;
    }

    @Override
    public String getPath() throws ClientException {
        LabsSite site = getSite();
        if (site == null) {
            throw new IllegalArgumentException("This page is not in a LabsSite");
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
