package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class PageListAdapter extends AbstractPage implements PageList {

    public PageListAdapter(DocumentModel doc) {
        this.doc = doc;
    }
    
    public static class Model {
        private DocumentModel doc;
        
        /**
         * PageList adapter = new PageListAdapter.Model(session, "/", "title").create();
         * @param session
         * @param parentPath
         * @param title
         * @throws ClientException
         */
        public Model(CoreSession session, String parentPath, String title) throws ClientException {
            this.doc = session.createDocumentModel(parentPath, title, Docs.PAGELIST.type());
        }
        
        /**
         * Creates document model in repository.
         * @return an adapter
         * @throws ClientException
         */
        public PageList create() throws ClientException {
            return new PageListAdapter(this.doc.getCoreSession().createDocument(this.doc));
        }
        
        public PageList getAdapter() throws ClientException{
            return this.doc.getAdapter(PageListAdapter.class);
        }
    }
}
