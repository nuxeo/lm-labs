package com.leroymerlin.corp.fr.nuxeo.piwik;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

public class PiwikAdapterFactory implements DocumentAdapterFactory {

	@Override
	public Object getAdapter(DocumentModel doc, Class<?> klass) {
		if (doc.hasSchema(PiwikAdapter.SCHEMA_NAME)) {
			return new PiwikAdapter(doc);
		}
		return null;
	}

}
