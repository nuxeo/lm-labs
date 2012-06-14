package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.exception.NullException;

public class LabsAdapterImpl extends LabsSessionImpl implements LabsAdapter {
	
	private static final Log LOG = LogFactory.getLog(LabsAdapterImpl.class);
	
	static int cpt = 0;
	
	public LabsAdapterImpl(DocumentModel document){
		this.doc = document;
	}

	protected DocumentModel doc;
	
	@Override
	public DocumentModel getDocument() {
		return doc;
	}

	@Override
	public CoreSession getSession() {
		CoreSession session = super.getSession();
		if (session == null){
			LOG.error("COMPTEUR doc.getCoreSession activé ! Nombre d'utilisation : " + cpt++ + " - " + this.getClass().getName());
			NullException nunu = new NullException("COMPTEUR" + this.getClass().getName());
			nunu.printStackTrace();
			return doc.getCoreSession();
		}
		return session;
	}
}
