package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(init=PageClasseurRepositoryInit.class, cleanup=Granularity.METHOD)
public class PageClasseurAdapterTest {
    private static final String TITLE2 = "page_classeur2";
    private static final String DESCR3 = "Ma descr 3";
    private static final String TITLE3 = "Page Classeur 3";
    @Inject
    private CoreSession session;

    @Test
    public void canCreatePageListe() throws Exception {
        assertTrue(session.exists(new PathRef("/page_classeur")));
    }
    
    @Test
    public void iCanCreateDocumentUsingAdapter() throws Exception {
        PageClasseur adapter = new PageClasseurAdapter.Model(session, "/", TITLE2).create();
        assertNotNull(adapter);
        DocumentModel doc = adapter.getDocument();
        assertNotNull(doc);
        assertTrue(session.exists(doc.getRef()));
        assertEquals(TITLE2, adapter.getTitle());
    }
    @Test
    public void iCanCreateDocumentUsingAdapterAndSetDescription() throws Exception {
        PageClasseur adapter = new PageClasseurAdapter.Model(session, "/", TITLE3).desc(DESCR3).create();
        assertNotNull(adapter);
        DocumentModel doc = adapter.getDocument();
        assertNotNull(doc);
        assertTrue(session.exists(doc.getRef()));
        assertEquals(TITLE3, adapter.getTitle());
        assertEquals(DESCR3, adapter.getDescription());
    }
}
