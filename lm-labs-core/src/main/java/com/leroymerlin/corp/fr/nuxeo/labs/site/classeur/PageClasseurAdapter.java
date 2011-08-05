package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import static com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs.PAGECLASSEUR;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;

public class PageClasseurAdapter extends AbstractPage implements PageClasseur {

    public PageClasseurAdapter(DocumentModel doc) {
        this.doc = doc;
    }
    
    public static class Model {
        private final DocumentModel doc;
        
        /**
         * PageClasseur adapter = new PageClasseurAdapter.Model(session, "/", "titre").desc("description").create();
         * @param session
         * @param parentPath
         * @param title
         * @throws ClientException
         */
        public Model(CoreSession session, String parentPath, String title) throws ClientException {
            this.doc = session.createDocumentModel(parentPath, title, PAGECLASSEUR.type());
            this.doc.setPropertyValue("dc:title", title);
        }
        
        /**
         * Sets dc:description.
         * @param description
         * @return
         * @throws PropertyException
         * @throws ClientException
         */
        public Model desc(String description) throws PropertyException, ClientException {
            PageClasseurAdapter.setDescription(this.doc, description);
            return this;
        }
        
        /**
         * Creates document model in repository.
         * @return an adapter
         * @throws ClientException
         */
        public PageClasseur create() throws ClientException {
            return new PageClasseurAdapter(this.doc.getCoreSession().createDocument(this.doc));
        }
    }

    @Override
    public DocumentModel getDocument() {
        return this.doc;
    }
    
}
