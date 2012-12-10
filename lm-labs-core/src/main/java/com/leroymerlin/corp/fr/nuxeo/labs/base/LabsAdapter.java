package com.leroymerlin.corp.fr.nuxeo.labs.base;

import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.common.core.adapter.SessionAdapter;

public interface LabsAdapter extends SessionAdapter {
	
	DocumentModel getDocument();
}
