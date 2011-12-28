package com.leroymerlin.corp.fr.nuxeo;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.DefaultRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.labssite.LabsSite;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.piwik.Piwik;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(cleanup=Granularity.METHOD, init=DefaultRepositoryInit.class)
public class PiwikAdapterTest {

    @Inject
    private CoreSession session;

    @Inject
    SiteManager sm;

    @Test
	public void iCanGetAdapter() throws Exception {
    	LabsSite site = sm.createSite(session, "Mon titre", "myurl");
    	Piwik piwik = site.getDocument().getAdapter(Piwik.class);
    	assertNotNull(piwik);
	}

    @Test
	public void iCanGetNullPiwikId() throws Exception {
    	LabsSite site = sm.createSite(session, "Mon titre", "myurl");
    	Piwik piwik = site.getDocument().getAdapter(Piwik.class);
    	assertNull(piwik.getId());
	}
    
    @Test
	public void iCanSetPiwikId() throws Exception {
    	LabsSite site = sm.createSite(session, "Mon titre", "myurl");
    	Piwik piwik = site.getDocument().getAdapter(Piwik.class);
		piwik.setId("123");
        session.saveDocument(site.getDocument());
        assertEquals("123", piwik.getId());
	}
    
    @Test
	public void iCanResetPiwikId() throws Exception {
    	LabsSite site = sm.createSite(session, "Mon titre", "myurl");
    	Piwik piwik = site.getDocument().getAdapter(Piwik.class);
		piwik.setId(null);
        session.saveDocument(site.getDocument());
    	assertNull(piwik.getId());
	}
}
