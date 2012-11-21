package com.leroymerlin.corp.fr.nuxeo.labs.site.nav;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.LabstTest;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;
@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@Deploy({
    "org.nuxeo.ecm.platform.webapp.types"
})

@RepositoryConfig(init=PageNavRepositoryInit.class, cleanup=Granularity.METHOD)
public class PageNavAdapterTest extends LabstTest {

    @Inject
    private CoreSession session;

    @Test
    public void canCreatePageNav() throws Exception {
        assertTrue(session.exists(new PathRef(PageNavRepositoryInit.PAGENAV_CONTAINER_PATH + PageNavRepositoryInit.PAGENAV_NAME)));
    }
    
    @Test
    public void iCanCreateDocumentUsingAdapter() throws Exception {
    	DocumentModel docu = session.getDocument(new PathRef(PageNavRepositoryInit.PAGENAV_CONTAINER_PATH + PageNavRepositoryInit.PAGENAV_NAME));
        PageNav adapter = Tools.getAdapter(PageNav.class, docu, session);
        assertNotNull(adapter);
        DocumentModel doc = adapter.getDocument();
        assertNotNull(doc);
        assertTrue(session.exists(doc.getRef()));
    }
    
    @Test
    public void iCanGetDefaultEmptyListTags() throws Exception {
    	DocumentModel docu = session.getDocument(new PathRef(PageNavRepositoryInit.PAGENAV_CONTAINER_PATH + PageNavRepositoryInit.PAGENAV_NAME));
        PageNav adapter = Tools.getAdapter(PageNav.class, docu, session);
        assertNotNull(adapter.getTags());
        assertThat(adapter.getTags().size(), is(0));
    }

    @Test
    public void iCanGetAndSetTags() throws Exception {
    	DocumentModel docu = session.getDocument(new PathRef(PageNavRepositoryInit.PAGENAV_CONTAINER_PATH + PageNavRepositoryInit.PAGENAV_NAME));
        PageNav adapter = Tools.getAdapter(PageNav.class, docu, session);
        List<String> tags = new ArrayList<String>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");
        adapter.setTags(tags);
        docu = session.saveDocument(docu);
        session.save();
        docu = session.getDocument(docu.getRef());
        adapter = Tools.getAdapter(PageNav.class, docu, session);
        assertNotNull(adapter.getTags());
        assertThat(adapter.getTags().size(), is(3));
        assertThat(adapter.getTags().get(0), is("tag1"));
        assertThat(adapter.getTags().get(1), is("tag2"));
        assertThat(adapter.getTags().get(2), is("tag3"));
    }
    
}
