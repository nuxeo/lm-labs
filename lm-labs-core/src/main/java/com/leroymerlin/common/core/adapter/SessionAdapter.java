package com.leroymerlin.common.core.adapter;

import org.nuxeo.ecm.core.api.CoreSession;

public interface SessionAdapter {
	
	CoreSession getSession();
	
	void setSession(CoreSession session);

}
