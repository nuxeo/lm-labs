package com.leroymerlin.corp.fr.nuxeo.forum;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Docs;

public class LMForumAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> klass) {
		if(doc.getType().equals(Docs.PAGEFORUM.type())) {
			return new LMForumImpl(doc);
		}
		return null;
	}

}
