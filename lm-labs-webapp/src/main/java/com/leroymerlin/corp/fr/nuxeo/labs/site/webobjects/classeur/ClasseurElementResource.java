package com.leroymerlin.corp.fr.nuxeo.labs.site.webobjects.classeur;

import javax.ws.rs.DELETE;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.webengine.model.WebObject;

import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurFolder;
import com.leroymerlin.corp.fr.nuxeo.labs.site.notification.MailNotification;

@WebObject(type = "ClasseurElement")
public class ClasseurElementResource extends DocumentObject {

    private static final Log LOG = LogFactory.getLog(ClasseurElementResource.class);
    private PageClasseurFolder parent;

    @Override
    public void initialize(Object... args) {
        super.initialize(args);
        if (args.length > 1) {
            parent = (PageClasseurFolder) args[1];
        }
    }

    @DELETE
    public Response doDelete() {
        try {
            parent.getDocument().getAdapter(MailNotification.class).setAsToBeNotified();
            doc.getCoreSession().saveDocument(parent.getDocument());
        } catch (ClientException e) {
            LOG.error("Unable to set " + parent.getDocument().getPathAsString() + " as 'to be notified'.");
        }
        super.doDelete();
        return redirect(prev.getPrevious()
                .getPath());
    }
}
