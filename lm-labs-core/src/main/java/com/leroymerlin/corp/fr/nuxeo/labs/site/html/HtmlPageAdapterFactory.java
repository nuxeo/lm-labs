package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

public class HtmlPageAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> itf) {
		if (HtmlPageImpl.DOCTYPE.equals(doc.getType())) {
			return new HtmlPageImpl(doc);
		}
		return null;
	}

}
