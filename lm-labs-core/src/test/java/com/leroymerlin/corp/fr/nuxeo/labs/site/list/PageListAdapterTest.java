package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(cleanup=Granularity.METHOD)
public class PageListAdapterTest {
    private static final String PATH_SEPARATOR = "/";
    private static final String PAGE_LIST_TITLE = "page_liste";
    @Inject
    private CoreSession session;

    @Test
    public void canCreatePageList() throws Exception {
        new PageListAdapter.Model(session, PATH_SEPARATOR, PAGE_LIST_TITLE).create();
        assertTrue(session.exists(new PathRef(PATH_SEPARATOR + PAGE_LIST_TITLE)));
    }

    @Test
    public void canCreateHeaderList() throws Exception {
        PageListAdapter.Model model = new PageListAdapter.Model(session, PATH_SEPARATOR, PAGE_LIST_TITLE);
        PageList pageList = model.getAdapter();
        //pageList.setDatalistRow("toto");
        model.create();
        assertTrue(session.exists(new PathRef(PATH_SEPARATOR + PAGE_LIST_TITLE)));
    }

}
