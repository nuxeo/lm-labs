package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.jndi.NamingContextFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@Deploy({
    "org.nuxeo.ecm.directory.api",
    "org.nuxeo.ecm.directory",
    "org.nuxeo.ecm.directory.sql",
    "com.leroymerlin.labs.core.test:OSGI-INF/test-directory-contrib.xml"

})
@RepositoryConfig(cleanup = Granularity.METHOD)
public class HtmlDocTest {

    private static final String TEST_PATHREF = "/myPage";
    private static final String TEST_DESCRIPTION = "Une page HTML";
    private static final String TEST_TITLE = "Mon titre de page";
    @Inject
    CoreSession session;
    
    @Inject
    DirectoryService directoryService;
    
    public HtmlDocTest() throws Exception {
        NamingContextFactory.setAsInitial();

        DataSource dataSource = PlatformFeature.createDataSource("jdbc:hsqldb:mem:directories");
        InitialContext initialCtx = new InitialContext();

        try {
            initialCtx.lookup("java:comp/env/jdbc/nxsqldirectory");
        } catch (NameNotFoundException e) {
            initialCtx.bind("java:comp/env/jdbc/nxsqldirectory", dataSource);
        }
    }

    @Test
    public void baseHTMLPageType() throws Exception {
        HtmlPage page = createTestPage();

        assertThat(page, is(notNullValue()));

        assertThat(page.getTitle(), is(TEST_TITLE));
        assertThat(page.getDescription(), is(TEST_DESCRIPTION));

        DocumentModel doc = page.getDocument();

        doc = session.createDocument(doc);
        session.save();

        doc = session.getDocument(new PathRef(TEST_PATHREF));
        assertThat(doc, is(notNullValue()));

        page = doc.getAdapter(HtmlPage.class);
        assertThat(page.getTitle(), is(TEST_TITLE));
        assertThat(page.getDescription(), is(TEST_DESCRIPTION));

    }

    @Test
    public void anHtmlPageMayHaveSomeSections() throws Exception {
        HtmlPage page = createTestPage();

        List<HtmlSection> sections = page.getSections();
        assertThat(sections.size(), is(0));

        page.addSection();
        sections = page.getSections();
        assertThat(sections.size(), is(1));

        session.createDocument(page.getDocument());
        session.save();

        page = retrieveTestPage();

        assertThat(page.getSections().size(), is(1));

    }

    @Test
    public void aSectionHaveTitleAndDesc() throws Exception {
        HtmlPage page = createApageWithSections();

        HtmlSection section = page.getSections().get(0);
        section.setTitle("Mon titre de section");
        section.setDescription("Ma description");

        assertThat(section.getTitle(), is("Mon titre de section"));
        assertThat(section.getDescription(), is("Ma description"));

        session.saveDocument(page.getDocument());
        session.save();

        page = retrieveTestPage();
        section = page.getSections().get(0);

        assertThat(section.getTitle(), is("Mon titre de section"));
        assertThat(section.getDescription(), is("Ma description"));

    }

    @Test
    public void aSectionHasRows() throws Exception {
        HtmlPage page = createApageWithSections();

        HtmlSection section = page.getSections().get(0);

        assertThat(section.getRows(), is(notNullValue()));
        assertThat(section.getRows().size(),is(0));
        section.addRow();
        assertThat(section.getRows().size(),is(1));

        session.saveDocument(page.getDocument());
        session.save();


        page = retrieveTestPage();

        section = page.getSections().get(0);
        List<HtmlRow> rows = section.getRows();
        assertThat(rows.size(),is(1));

    }

    @Test
    public void rowsHaveContent() throws Exception {
        HtmlPage page = createApageWithSectionsAndRow();
        HtmlSection section = page.getSections().get(0);
        HtmlRow row = section.getRows().get(0);

        row.addContent(4, "My Content 1");
        row.addContent(6, "My Content 2");
        row.addContent(6, "My Content 3");

        assertThat(row.getContents().size(),is(3));

        session.saveDocument(page.getDocument());
        session.save();

        page = retrieveTestPage();
        row = page.getSections().get(0).getRows().get(0);
        assertThat(row.getContents().size(),is(3));

        HtmlContent content = row.getContents().get(0);
        assertThat(content.getColNumber(),is(4));
        assertThat(content.getHtml(),is("My Content 1"));

    }

    @Test
    public void rowHaveClasses() throws Exception {
        HtmlPage page = createApageWithSections();
        HtmlSection section = page.getSections().get(0);

        HtmlRow row = section.addRow();
        assertThat(row.getCssClass(),is(nullValue()));

        row = section.addRow("acssclass");
        assertThat(row.getCssClass(),is("acssclass"));

        session.saveDocument(page.getDocument());
        session.save();

        page = retrieveTestPage();
        section = page.getSections().get(0);
        assertThat(section.row(0).getCssClass(),is(nullValue()));
        assertThat(section.row(1).getCssClass(),is("acssclass"));
    }

    @Test
    public void canSetRowClasses() throws Exception {
        HtmlPage page = createApageWithSections();
        HtmlSection section = page.getSections().get(0);

        HtmlRow row = section.addRow();
        assertThat(row.getCssClass(),is(nullValue()));

        row = section.addRow();
        row.setCssClass("acssclass1");
        assertThat(row.getCssClass(),is("acssclass1"));

        session.saveDocument(page.getDocument());
        session.save();

        page = retrieveTestPage();
        section = page.getSections().get(0);
        assertThat(section.row(0).getCssClass(),is(nullValue()));
        assertThat(section.row(1).getCssClass(),is("acssclass1"));
    }

    @Test
    public void iCanGetTheTestDirectory() throws Exception {
        assertNotNull(directoryService);
        Session dirSession = directoryService.open("columns_layout");
        assertNotNull(dirSession);
        dirSession.close();
    }
    
    @Test
    public void iCanGetColumnsLayout() throws Exception {
        Map<String, String> columnLayoutsSelect = HtmlRow.getColumnLayoutsSelect();
        assertFalse(columnLayoutsSelect.isEmpty());
        Session dirSession = directoryService.open("columns_layout");
        Long prev = null;
        for (String code : columnLayoutsSelect.keySet()) {
            DocumentModel entry = dirSession.getEntry(code);
            if (prev != null && prev.intValue() > ((Long) entry.getPropertyValue("columns_layout:order")).intValue()) {
                fail("directory entries not ordered.");
            }
            prev = new Long((Long) entry.getPropertyValue("columns_layout:order"));
        }
    }

    private HtmlPage retrieveTestPage() throws ClientException {
        DocumentModel doc = session.getDocument(new PathRef(TEST_PATHREF));
        return doc.getAdapter(HtmlPage.class);
    }

    private HtmlPage createApageWithSectionsAndRow() throws ClientException {
        HtmlPage page = createApageWithSections();
        page.getSections().get(0).addRow();
        return page;
    }

    private HtmlPage createApageWithSections() throws ClientException {
        HtmlPage page = createTestPage();
        page.addSection();
        DocumentModel doc = session.createDocument(page.getDocument());

        return doc.getAdapter(HtmlPage.class);

    }

    private HtmlPage createTestPage() throws ClientException {
        DocumentModel doc = session.createDocumentModel("/", "myPage",
                "HtmlPage");
        HtmlPage page = doc.getAdapter(HtmlPage.class);
        page.setTitle(TEST_TITLE);
        page.setDescription(TEST_DESCRIPTION);
        return page;

    }
}
