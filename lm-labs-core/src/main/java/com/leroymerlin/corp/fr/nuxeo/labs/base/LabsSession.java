package com.leroymerlin.corp.fr.nuxeo.labs.base;

import org.nuxeo.ecm.core.api.CoreSession;

public interface LabsSession {
	
	CoreSession getSession();
	
	void setSession(CoreSession session);

}