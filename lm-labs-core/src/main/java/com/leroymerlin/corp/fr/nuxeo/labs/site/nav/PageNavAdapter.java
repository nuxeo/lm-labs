package com.leroymerlin.corp.fr.nuxeo.labs.site.nav;

import static com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs.PAGENAV;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;

import com.leroymerlin.corp.fr.nuxeo.labs.base.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;

public class PageNavAdapter extends AbstractPage implements PageNav {

    public PageNavAdapter(DocumentModel doc) {
        super(doc);
    }

    public static class Model {
        private final DocumentModel doc;
        
        private CoreSession session;

        /**
         * PageNav adapter = new PageNavAdapter.Model(session, "/",
         * "titre").desc("description").create();
         *
         * @param session
         * @param parentPath
         * @param title
         * @throws ClientException
         */
        public Model(CoreSession session, String parentPath, String title)
                throws ClientException {
        	this.session = session;
            this.doc = session.createDocumentModel(parentPath, title,
                    PAGENAV.type());
            this.doc.setPropertyValue("dc:title", title);
        }

        /**
         * Sets dc:description.
         *
         * @param description
         * @return
         * @throws PropertyException
         * @throws ClientException
         */
        public Model desc(String description) throws PropertyException,
                ClientException {
            PageNavAdapter.setDescription(this.doc, description);
            return this;
        }

        /**
         * Creates document model in repository.
         *
         * @return an adapter
         * @throws ClientException
         */
        public PageNav create() throws ClientException {
            DocumentModel doc = this.session.createDocument(this.doc);
            PageNav adapter = doc.getAdapter(PageNav.class);
            adapter.setSession(session);
			return adapter;
        }
    }

	@Override
	public List<String> getTags() throws ClientException {
		@SuppressWarnings("unchecked")
        List<String> tags = (List<String>) doc.getPropertyValue(LabsSiteConstants.AdvancedSearch.LIST_TAGS);
        if (tags == null){
            tags = new ArrayList<String>();
        }
        return tags;
	}

	@Override
	public void setTags(List<String> tags) throws ClientException {
		doc.getProperty(LabsSiteConstants.AdvancedSearch.LIST_TAGS).setValue(tags);
	}
}
