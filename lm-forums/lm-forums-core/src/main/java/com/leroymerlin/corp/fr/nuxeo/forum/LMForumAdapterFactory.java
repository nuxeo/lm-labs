package com.leroymerlin.corp.fr.nuxeo.forum;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

public class LMForumAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> klass) {
		if(doc.getType().equals("LMForum")) {
			return new LMForumImpl(doc);
		}
		return null;
	}

}
