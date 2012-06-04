package com.leroymerlin.corp.fr.nuxeo.labs.site.blocs;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(init=PageBlocsRepositoryInit.class, cleanup=Granularity.METHOD)
public class PageBlocsAdapterTest {
    
    private static final String TITRE_1 = "titre 1";
    
    @Inject
    private CoreSession session;

    @Rule public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void canCreatePageListe() throws Exception {
        assertTrue(session.exists(new PathRef("/page_blocs")));
    }
    
    @Test
    public void iCanSetTitle() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_blocs"));
        assertNotNull(doc);
        PageBlocs pageBlocs = Tools.getAdapter(PageBlocs.class, doc, session);
        assertNotNull(pageBlocs);
        pageBlocs.setTitle(TITRE_1);
        assertNotNull(pageBlocs.getTitle());
        assertEquals(TITRE_1, pageBlocs.getTitle());
    }
    
    @Test
    public void iCannotSetTitleToNull() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_blocs"));
        assertNotNull(doc);
        PageBlocs pageBlocs = Tools.getAdapter(PageBlocs.class, doc, session);
        assertNotNull(pageBlocs);
        thrown.expect(IllegalArgumentException.class);
        pageBlocs.setTitle(null);
    }
    
    @Test
    public void iCannotSetExternalURLs() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_blocs"));
        assertNotNull(doc);
        PageBlocs pageBlocs = Tools.getAdapter(PageBlocs.class, doc, session);
        assertNotNull(pageBlocs);
        pageBlocs.setTitle(TITRE_1);
    }
    
}
