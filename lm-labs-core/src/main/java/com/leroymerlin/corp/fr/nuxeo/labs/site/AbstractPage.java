package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;

public abstract class AbstractPage implements Page {

    protected DocumentModel doc;



    @Override
    public DocumentModel getDocument() {
        return doc;
    }

    @Override
    public void setTitle(String title) throws PropertyException, ClientException, IllegalArgumentException {
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
    public void setDescription(String description) throws PropertyException, ClientException {
        setDescription(doc, description);
    }

    protected static void setDescription(DocumentModel document, String description) throws PropertyException, ClientException {
        if (description == null) {
            return;
        }
        document.setPropertyValue("dc:description", description);
    }

    @Override
    public String getDescription() throws PropertyException, ClientException {
        return (String) doc.getPropertyValue("dc:description");
    }

    @Override
    public void setCommentaire(String commentaire) throws PropertyException, ClientException {
        setCommentaire(doc, commentaire);
    }

    protected static void setCommentaire(DocumentModel document, String commentaire) throws PropertyException, ClientException {
        if (commentaire == null) {
            return;
        }
        document.setPropertyValue("pg:commentaire", commentaire);
    }

    @Override
    public String getCommentaire() throws PropertyException, ClientException {
        return (String) doc.getPropertyValue("pg:commentaire");
    }

}
