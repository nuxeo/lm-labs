package com.leroymerlin.corp.fr.nuxeo.labs.base;

import org.nuxeo.ecm.core.api.DocumentModel;

public interface LabsAdapter extends LabsSession {
	
	DocumentModel getDocument();
}
