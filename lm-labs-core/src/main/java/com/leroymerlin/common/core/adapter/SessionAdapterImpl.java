package com.leroymerlin.common.core.adapter;

import org.nuxeo.ecm.core.api.CoreSession;

public class SessionAdapterImpl implements SessionAdapter {
	
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
