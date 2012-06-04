package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.DocumentModel;

public interface LabsAdapter extends LabsSession {
	
	DocumentModel getDocument();
}
