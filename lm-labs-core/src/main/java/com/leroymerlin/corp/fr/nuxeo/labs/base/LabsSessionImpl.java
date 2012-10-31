package com.leroymerlin.corp.fr.nuxeo.labs.base;

import org.nuxeo.ecm.core.api.CoreSession;


public class LabsSessionImpl implements LabsSession {
	
	private CoreSession session = null;

	@Override
	public CoreSession getSession() {
		return session;
	}

	@Override
	public void setSession(CoreSession session) {
		this.session = session;
	}

}
