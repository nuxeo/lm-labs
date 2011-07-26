package com.leroymerlin.corp.fr.nuxeo.labs.site.blocs;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.concordion.internal.command.AssertEqualsCommand;
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
@RepositoryConfig(init=PageBlocsRepositoryInit.class, cleanup=Granularity.METHOD)
public class PageBlocsAdapterTest {
    private static final String TITRE_1 = "titre 1";
    @Inject
    private CoreSession session;

    @Test
    public void canCreatePageListe() throws Exception {
        assertTrue(session.exists(new PathRef("/page_blocs")));
    }
    
    @Test
    public void iCanSetTitle() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_blocs"));
        assertNotNull(doc);
        PageBlocs pageBlocs = doc.getAdapter(PageBlocs.class);
        assertNotNull(pageBlocs);
        pageBlocs.setTitle(TITRE_1);
        assertNotNull(pageBlocs.getTitle());
        assertEquals(TITRE_1, pageBlocs.getTitle());
    }
}
