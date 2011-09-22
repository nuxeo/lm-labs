package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Header;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(cleanup=Granularity.METHOD)
public class PageListAdapterTest {
    private static final String WIDTH = "S8";
    private static final int ID_HEADER = 1;
    private static final String FONT_SIZE = "50";
    private static final String FONT_NAME = "fontName";
    private static final String TYPE = "type";
    private static final String NAME = "name";
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
        assertThat(pageList,is(notNullValue()));
        
        Header head = new Header();
        head.setName(NAME);
        head.setType(TYPE);
        head.setFontName(FONT_NAME);
        head.setFontSize(FONT_SIZE);
        head.setIdHeader(ID_HEADER);
        head.setWidth(WIDTH);
        pageList.addHeader(head);
        pageList = model.create();
        
        assertTrue(session.exists(new PathRef(PATH_SEPARATOR + PAGE_LIST_TITLE)));
        Set<Header> headerList = pageList.getHeaderSet();
        assertNotNull(headerList);
        assertTrue(headerList.size() == 1);
        for (Header header:headerList){
            assertTrue(NAME.equals(header.getName()));
            assertTrue(TYPE.equals(header.getType()));
            assertTrue(FONT_NAME.equals(header.getFontName()));
            assertTrue(FONT_SIZE == header.getFontSize());
            assertTrue(ID_HEADER == header.getIdHeader());
            assertTrue(WIDTH == header.getWidth());
        }
    }

}
