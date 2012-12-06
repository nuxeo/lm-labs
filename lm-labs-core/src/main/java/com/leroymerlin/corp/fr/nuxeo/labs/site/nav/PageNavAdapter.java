package com.leroymerlin.corp.fr.nuxeo.labs.site.nav;

import static com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs.PAGENAV;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.model.PropertyException;

import com.leroymerlin.corp.fr.nuxeo.labs.base.AbstractPage;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteDocument;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Schemas;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

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
	
	public String getQueryTaggedPage() throws ClientException {
		SiteDocument siteDocument = Tools.getAdapter(SiteDocument.class, doc, getSession());
		String query = String.format("SELECT * FROM %s WHERE ecm:path STARTSWITH '%s' AND ecm:fulltext." + 
				Schemas.LABSTAGS.prefix() + ":tags = '%s'",
				LabsSiteConstants.Docs.PAGE.type(), siteDocument.getSite().getTree().getPathAsString(),
				createQueryTags2());
		return query;
	}

	@Override
	public List<Page> getTaggedPages() throws ClientException {
		List<Page> pages = new ArrayList<Page>();
		if (getTags().size() > 0){
			CoreSession session = getSession();
			SiteDocument siteDocument = Tools.getAdapter(SiteDocument.class, doc, session);
			if (siteDocument != null){
				String query = String.format("SELECT * FROM %s WHERE ecm:path STARTSWITH '%s' AND " + 
						Schemas.LABSTAGS.prefix() + ":tags IN (%s)",
						LabsSiteConstants.Docs.PAGE.type(), siteDocument.getSite().getTree().getPathAsString(),
						createQueryTags());
				DocumentModelList listDoc = session.query(query);
				Page page = null;
				for (DocumentModel docu : listDoc){
					page = Tools.getAdapter(Page.class, docu, session);
					if (page != null){
						pages.add(page);
					}
				}
			}
		}
		return pages;
	}
	
	private String createQueryTags2() throws ClientException{
		StringBuffer str = new StringBuffer("");
		Iterator<String> it = getTags().iterator();
		if (it.hasNext()){
			do{
				str.append("\"").append(it.next()).append("\"");
				if (it.hasNext()){
					str.append(" ");
				}
			}while(it.hasNext());
		}
		return str.toString();
	}
	
	private String createQueryTags() throws ClientException{
		StringBuffer str = new StringBuffer("");
		Iterator<String> it = getTags().iterator();
		if (it.hasNext()){
			do{
				str.append("'").append(it.next()).append("'");
				if (it.hasNext()){
					str.append(", ");
				}
			}while(it.hasNext());
		}
		return str.toString();
	}

	@Override
	public String getUserQuery() throws ClientException {
		return (String) doc.getPropertyValue(LabsSiteConstants.AdvancedSearch.USER_QUERY);
	}

	@Override
	public void setUserQuery(String userQuery) throws ClientException {
		doc.setPropertyValue(LabsSiteConstants.AdvancedSearch.USER_QUERY, userQuery);
	}
}
