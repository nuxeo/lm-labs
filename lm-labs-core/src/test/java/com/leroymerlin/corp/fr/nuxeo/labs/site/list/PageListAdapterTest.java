package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntriesLine;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Entry;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Header;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.UrlType;
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
    private static final boolean CHECKBOX = true;
    private static final Calendar CAL = Calendar.getInstance();
    private static final String TEXT = "text";
    
    @Inject
    private CoreSession session;

    @Test
    public void canCreatePageList() throws Exception {
        new PageListAdapter.Model(session, PATH_SEPARATOR, PAGE_LIST_TITLE).create();
        assertTrue(session.exists(new PathRef(PATH_SEPARATOR + PAGE_LIST_TITLE)));
    }

    @Test
    public void canAddHeader() throws Exception {
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
    
    @Test
    public void canCreateHeadersAndGetHeaders() throws Exception{
        PageListAdapter.Model model = new PageListAdapter.Model(session, PATH_SEPARATOR, PAGE_LIST_TITLE);
        PageList pageList = model.getAdapter();
        
        pageList = createPageListWithHeaders(model, pageList);
        Set<Header> headerList = pageList.getHeaderSet();
        assertNotNull(headerList);
        assertTrue(headerList.size() == 3);
    }
    
    @Test
    public void canResetHeaders() throws Exception{
        PageListAdapter.Model model = new PageListAdapter.Model(session, PATH_SEPARATOR, PAGE_LIST_TITLE);
        PageList pageList = model.getAdapter();
        
        pageList = createPageListWithHeaders(model, pageList);
        pageList.resetHeaders();
        Set<Header> headerList = pageList.getHeaderSet();
        assertNotNull(headerList);
        assertTrue(headerList.size() == 0);
    }

    /**
     * @param model
     * @param pageList
     * @return
     * @throws ClientException
     */
    private PageList createPageListWithHeaders(PageListAdapter.Model model, PageList pageList) throws ClientException {
        Header head = new Header();
        head.setName(NAME);
        head.setOrderPosition(0);
        List<Header> headers = new ArrayList<Header>();
        headers.add(head);
        head = new Header();
        head.setName(NAME);
        head.setOrderPosition(1);
        headers.add(head);
        head = new Header();
        head.setName(NAME);
        head.setOrderPosition(2);
        headers.add(head);
        pageList.setHeaders(headers);
        pageList = model.create();
        return pageList;
    }
    
    @Test
    public void canSaveLineAndGetLines() throws Exception {
        PageListAdapter.Model model = new PageListAdapter.Model(session, PATH_SEPARATOR, PAGE_LIST_TITLE);
        PageList pageList = model.getAdapter();
        pageList = model.create();
        
        Entry entry = new Entry();
        entry.setIdHeader(ID_HEADER);
        entry.setText(TEXT);
        entry.setDate(CAL);
        entry.setCheckbox(CHECKBOX);
        UrlType url = new UrlType("nameURL", "http://www.google.fr");
        entry.setUrl(url);
        
        EntriesLine line = new EntriesLine();
        line.getEntries().add(entry);
        
        entry = new Entry();
        entry.setIdHeader(ID_HEADER +1);
        entry.setText(TEXT);
        entry.setDate(CAL);
        entry.setCheckbox(CHECKBOX);
        url = new UrlType("nameURL", "http://www.google.fr4444");
        entry.setUrl(url);
        
        line.getEntries().add(entry);
        
        assertThat(pageList.getLines().size(), is(0));
        
        pageList.saveLine(line);

        assertThat(pageList.getLines().size(), is(1));
        assertThat(pageList.getLines().get(0).getEntries(), notNullValue());
        assertThat(pageList.getLines().get(0).getEntries().size(), is(2));
        assertThat(pageList.getLines().get(0).getEntries().get(0).getIdHeader(), is(ID_HEADER));
        assertThat(pageList.getLines().get(0).getEntries().get(0).getText(), is(TEXT));
        assertThat(pageList.getLines().get(0).getEntries().get(0).getDate(), is(CAL));
        assertThat(pageList.getLines().get(0).getEntries().get(0).isCheckbox(), is(CHECKBOX));
        assertThat(pageList.getLines().get(0).getEntries().get(0).getUrl(), notNullValue());
        assertThat(pageList.getLines().get(0).getEntries().get(0).getUrl().getName(), is("nameURL"));
        assertThat(pageList.getLines().get(0).getEntries().get(0).getUrl().getUrl(), is("http://www.google.fr"));
        assertThat(pageList.getLines().get(0).getDocRef(), notNullValue());
    }
    
    @Test
    public void canRemoveLine() throws Exception {
        PageListAdapter.Model model = new PageListAdapter.Model(session, PATH_SEPARATOR, PAGE_LIST_TITLE);
        PageList pageList = model.getAdapter();
        pageList = model.create();
        
        Entry entry = new Entry();
        entry.setIdHeader(ID_HEADER);
        entry.setText(TEXT);
        entry.setDate(CAL);
        entry.setCheckbox(CHECKBOX);
        UrlType url = new UrlType("nameURL", "http://www.google.fr");
        entry.setUrl(url);
        
        EntriesLine line = new EntriesLine();
        line.getEntries().add(entry);
        
        entry = new Entry();
        entry.setIdHeader(ID_HEADER +1);
        entry.setText(TEXT);
        entry.setDate(CAL);
        entry.setCheckbox(CHECKBOX);
        url = new UrlType("nameURL", "http://www.google.fr4444");
        entry.setUrl(url);
        
        line.getEntries().add(entry);
        
        pageList.saveLine(line);
        assertThat(pageList.getLines().size(), is(1));
        assertThat(pageList.getLines().get(0).getDocRef(), notNullValue());
        pageList.removeLine(pageList.getLines().get(0).getDocRef());
        assertThat(pageList.getLines().size(), is(0));
    }
    
    @Test
    public void canGetLine() throws Exception {
        PageListAdapter.Model model = new PageListAdapter.Model(session, PATH_SEPARATOR, PAGE_LIST_TITLE);
        PageList pageList = model.getAdapter();
        pageList = model.create();
        
        Entry entry = new Entry();
        entry.setIdHeader(ID_HEADER);
        entry.setText(TEXT);
        entry.setDate(CAL);
        entry.setCheckbox(CHECKBOX);
        UrlType url = new UrlType("nameURL", "http://www.google.fr");
        entry.setUrl(url);
        
        EntriesLine line = new EntriesLine();
        line.getEntries().add(entry);
        
        entry = new Entry();
        entry.setIdHeader(ID_HEADER +1);
        entry.setText(TEXT);
        entry.setDate(CAL);
        entry.setCheckbox(CHECKBOX);
        url = new UrlType("nameURL", "http://www.google.fr4444");
        entry.setUrl(url);
        
        line.getEntries().add(entry);
        
        pageList.saveLine(line);
        EntriesLine line2 = pageList.getLine(pageList.getLines().get(0).getDocRef());
        assertThat(line2, notNullValue());
        assertThat(line2.getEntries().size(), is(2));
    }
    
    @Test
    public void canGetEntryByHead() throws Exception {
        Entry entry = new Entry();
        entry.setIdHeader(ID_HEADER);
        entry.setText(TEXT);
        entry.setDate(CAL);
        entry.setCheckbox(CHECKBOX);
        UrlType url = new UrlType("nameURL", "http://www.google.fr");
        entry.setUrl(url);
        
        EntriesLine line = new EntriesLine();
        line.getEntries().add(entry);
        
        entry = new Entry();
        entry.setIdHeader(ID_HEADER +1);
        entry.setText(TEXT);
        entry.setDate(CAL);
        entry.setCheckbox(CHECKBOX);
        url = new UrlType("nameURL", "http://www.google.fr4444");
        entry.setUrl(url);
        
        line.getEntries().add(entry);
        
        Entry entryByIdHead = line.getEntryByIdHead(ID_HEADER + 1);
        assertThat(entryByIdHead, notNullValue());
        assertThat(entryByIdHead.getUrl(), notNullValue());
        assertThat(entryByIdHead.getUrl().getUrl(), is("http://www.google.fr4444"));
    }
}
