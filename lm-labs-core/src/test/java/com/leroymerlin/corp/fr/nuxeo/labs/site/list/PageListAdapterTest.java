package com.leroymerlin.corp.fr.nuxeo.labs.site.list;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
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
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.EntryType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.Header;
import com.leroymerlin.corp.fr.nuxeo.labs.site.list.bean.UrlType;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(cleanup=Granularity.METHOD)
public class PageListAdapterTest {
    private static final String FORMAT_DATE = "dd/MM/yy";
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
    public void canGetFormatDateDefault() throws Exception {
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
        
        Set<Header> headerList = pageList.getHeaderSet();
        assertNotNull(headerList);
        assertTrue(headerList.size() == 1);
        for (Header header:headerList){
            assertTrue("dd MMMMM yyyy".equals(header.getFormatDate()));
        }
    }

    @Test
    public void canSetAndGetFormatDate() throws Exception {
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
        head.setFormatDate(FORMAT_DATE);
        pageList.addHeader(head);
        pageList = model.create();
        
        Set<Header> headerList = pageList.getHeaderSet();
        assertNotNull(headerList);
        assertTrue(headerList.size() == 1);
        for (Header header:headerList){
            assertTrue(FORMAT_DATE.equals(header.getFormatDate()));
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
        pageList.setHeaders(headers, session);
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
        
        assertThat(pageList.getLines(session).size(), is(0));
        
        pageList.saveLine(line, null, session);

        assertThat(pageList.getLines(session).size(), is(1));
        assertThat(pageList.getLines(session).get(0).getEntries(), notNullValue());
        assertThat(pageList.getLines(session).get(0).getEntries().size(), is(2));
        assertThat(pageList.getLines(session).get(0).getEntries().get(0).getIdHeader(), is(ID_HEADER));
        assertThat(pageList.getLines(session).get(0).getEntries().get(0).getText(), is(TEXT));
        assertThat(pageList.getLines(session).get(0).getEntries().get(0).getDate(), is(CAL));
        assertThat(pageList.getLines(session).get(0).getEntries().get(0).isCheckbox(), is(CHECKBOX));
        assertThat(pageList.getLines(session).get(0).getEntries().get(0).getUrl(), notNullValue());
        assertThat(pageList.getLines(session).get(0).getEntries().get(0).getUrl().getName(), is("nameURL"));
        assertThat(pageList.getLines(session).get(0).getEntries().get(0).getUrl().getUrl(), is("http://www.google.fr"));
        assertThat(pageList.getLines(session).get(0).getDocLine(), notNullValue());
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
        
        pageList.saveLine(line, null, session);
        assertThat(pageList.getLines(session).size(), is(1));
        assertThat(pageList.getLines(session).get(0).getDocLine(), notNullValue());
        pageList.removeLine(pageList.getLines(session).get(0).getDocLine().getRef(), session);
        assertThat(pageList.getLines(session).size(), is(0));
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
        
        pageList.saveLine(line, null, session);
        EntriesLine line2 = pageList.getLine(pageList.getLines(session).get(0).getDocLine().getRef(), session);
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
    
    @Test
    public void canIsAllContibutorsDefault() throws Exception {
        PageListAdapter.Model model = new PageListAdapter.Model(session, PATH_SEPARATOR, PAGE_LIST_TITLE);
        PageList pageList = model.getAdapter();
        pageList = model.create();
        assertFalse(pageList.isAllContributors());
    }
    
    @Test
    public void canSetAndIsAllContibutors() throws Exception {
        PageListAdapter.Model model = new PageListAdapter.Model(session, PATH_SEPARATOR, PAGE_LIST_TITLE);
        PageList pageList = model.getAdapter();
        pageList = model.create();
        pageList.setAllContributors(true, session);
        assertTrue(pageList.isAllContributors());
    }
    
    @Test
    public void canIsAllCommentablesLinesDefault() throws Exception {
        PageListAdapter.Model model = new PageListAdapter.Model(session, PATH_SEPARATOR, PAGE_LIST_TITLE);
        PageList pageList = model.getAdapter();
        pageList = model.create();
        assertFalse(pageList.isCommentableLines());
    }
    
    @Test
    public void canSetAndIsAllCommentablesLines() throws Exception {
        PageListAdapter.Model model = new PageListAdapter.Model(session, PATH_SEPARATOR, PAGE_LIST_TITLE);
        PageList pageList = model.getAdapter();
        pageList.setCommentableLines(true);
        pageList = model.create();
        assertTrue(pageList.isCommentableLines());
    }
    
    @Test
    public void canExportExcel() throws Exception {
        PageListAdapter.Model model = new PageListAdapter.Model(session, PATH_SEPARATOR, PAGE_LIST_TITLE);
        PageList pageList = model.getAdapter();
        pageList = model.create();
        
        pageList.setHeaders(createHeadersOnPageList(), session);

        pageList.saveLine(createLine("texte1"), null, session);
        pageList.saveLine(createLine("texte2"), null, session);
        pageList.saveLine(createLine("texte3"), null, session);
        pageList.saveLine(createLine("texte4"), null, session);
 
        File testFile = null;
        //For local tests
//        File testFile = new File("/home/fvandaele/exportTest.xls");
        try {
            testFile = File.createTempFile("exportTest", ".xls");
//            testFile.createNewFile();
            assertTrue(testFile.exists());
            long size = testFile.length();
            assertTrue(size == 0);
            pageList.exportExcel(new FileOutputStream(testFile), session);
            assertTrue(size != testFile.length());
        }
        finally{
            if(testFile != null){
                testFile.delete();
            }
        }
    }

    /**
     * @return
     */
    private EntriesLine createLine(String pText) {
        EntriesLine line = new EntriesLine();
        
        Entry entry = null;
        UrlType url = null;
        
        //Create entry
        entry = new Entry();
        entry.setIdHeader(0);
        entry.setText(pText);
        line.getEntries().add(entry);
        
        entry = new Entry();
        entry.setIdHeader(1);
        entry.setDate(CAL);
        line.getEntries().add(entry);
        
        entry = new Entry();
        entry.setIdHeader(2);
        entry.setCheckbox(CHECKBOX);
        line.getEntries().add(entry);
        
        entry = new Entry();
        entry.setIdHeader(3);
        url = new UrlType("nameURL", "http://www.google.fr4444");
        entry.setUrl(url);
        line.getEntries().add(entry);
        return line;
    }

    /**
     * @param pageList
     * @throws ClientException
     */
    private List<Header> createHeadersOnPageList() throws ClientException {
        List<Header> headers = new ArrayList<Header>();
        int idHead = 0;
        Header head = new Header();
        head.setName("du texte");
        head.setType(EntryType.SELECT.name());
        head.setFontName(FONT_NAME);
        head.setFontSize(FONT_SIZE);
        head.setIdHeader(idHead++);
        head.setWidth(WIDTH);
        head.setOrderPosition(idHead);
        headers.add(head);
        
        head = new Header();
        head.setName("de la date");
        head.setType(EntryType.DATE.name());
        head.setFontName(FONT_NAME);
        head.setFontSize(FONT_SIZE);
        head.setIdHeader(idHead++);
        head.setWidth(WIDTH);
        head.setOrderPosition(idHead);
        headers.add(head);
        
        head = new Header();
        head.setName("de la case à cocher");
        head.setType(EntryType.CHECKBOX.name());
        head.setFontName(FONT_NAME);
        head.setFontSize(FONT_SIZE);
        head.setIdHeader(idHead++);
        head.setWidth(WIDTH);
        head.setOrderPosition(idHead);
        headers.add(head);
        
        head = new Header();
        head.setName("de l'url");
        head.setType(EntryType.URL.name());
        head.setFontName(FONT_NAME);
        head.setFontSize(FONT_SIZE);
        head.setIdHeader(idHead++);
        head.setWidth(WIDTH);
        head.setOrderPosition(idHead);
        headers.add(head);
        
        return headers;
    }
}
