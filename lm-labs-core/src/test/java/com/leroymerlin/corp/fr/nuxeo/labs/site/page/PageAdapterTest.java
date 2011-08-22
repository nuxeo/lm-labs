package com.leroymerlin.corp.fr.nuxeo.labs.site.page;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.Page;
import com.leroymerlin.corp.fr.nuxeo.labs.site.classeur.PageClasseurRepositoryInit;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(init=PageClasseurRepositoryInit.class)
public class PageAdapterTest {

    private static final String TITRE_1 = "titre 1";

    private static final String DESCRIPTION_1 = "Ma description";
    
    @Inject private CoreSession session;

    @Rule public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void iCanGetGenericAdaptorForPageClasseur() throws Exception {
        DocumentModel pageClasseur = session.getDocument(new PathRef("/page_classeur"));
        Page adapter = pageClasseur.getAdapter(Page.class);
        assertNotNull(adapter);
    }

    @Test
    public void iCanSetTitle() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        assertNotNull(doc);
        Page page = doc.getAdapter(Page.class);
        assertNotNull(page);
        page.setTitle(TITRE_1);
        assertNotNull(page.getTitle());
        assertEquals(TITRE_1, page.getTitle());
    }
    
    @Test
    public void iCannotSetTitleToNull() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        assertNotNull(doc);
        Page page = doc.getAdapter(Page.class);
        assertNotNull(page);
        thrown.expect(IllegalArgumentException.class);
        page.setTitle(null);
    }
    
    @Test
    public void iCanSetDescription() throws Exception {
        DocumentModel doc = session.getDocument(new PathRef("/page_classeur"));
        Page page = doc.getAdapter(Page.class);
        assertNotNull(page);
        page.setDescription(DESCRIPTION_1);
        assertEquals(DESCRIPTION_1, page.getDescription());
    }
    
}
