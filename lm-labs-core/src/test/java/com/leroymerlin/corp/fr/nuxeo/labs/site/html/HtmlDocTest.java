package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
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
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.Tools;

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

        page = Tools.getAdapter(HtmlPage.class, doc, session);
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

        row = section.addRow("acssclass", null);
        assertThat(row.getCssClass(),is("acssclass"));

        session.saveDocument(page.getDocument());
        session.save();

        page = retrieveTestPage();
        section = page.getSections().get(0);
        assertThat(section.row(0).getCssClass(),is(nullValue()));
        assertThat(section.row(1).getCssClass(),is("acssclass"));
    }

    @Test
    public void rowHaveUserClasses() throws Exception {
        HtmlPage page = createApageWithSections();
        HtmlSection section = page.getSections().get(0);
        

        List<String> userClass = new ArrayList<String>();
        userClass.add("class1");
        userClass.add("class2");
        HtmlRow row = section.addRow("acssclass", userClass);
        assertThat(row.getUserClass(),is(notNullValue()));
        assertThat(row.getUserClass().size(),is(2));
        
        session.saveDocument(page.getDocument());
        session.save();

        page = retrieveTestPage();
        section = page.getSections().get(0);
        assertThat(section.row(0).getUserClass(),is(notNullValue()));
        assertThat(row.getUserClass().size(),is(2));
        assertThat(row.getUserClass().get(0),is("class1"));
        assertThat(row.getUserClass().get(1),is("class2"));
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
    
    @Test
    public void iCanMoveUpSection() throws Exception {
    	HtmlPage page = createTestPage();
        createListSectionsInHtmlPage(page);
        DocumentModel doc = session.createDocument(page.getDocument());
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);

        assertNotNull(page);
        List<HtmlSection> sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(4));
		assertNotNull(sections.get(0));
        assertThat(sections.get(0).getTitle(), is("titre1"));
		assertNotNull(sections.get(1));
        assertThat(sections.get(1).getTitle(), is("titre2"));
		assertNotNull(sections.get(2));
        assertThat(sections.get(2).getTitle(), is("titre3"));
		assertNotNull(sections.get(3));
        assertThat(sections.get(3).getTitle(), is("titre4"));
        
        page.moveUp(2);
        doc = session.saveDocument(page.getDocument());
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);
        
        assertNotNull(page);
        sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(4));
		assertNotNull(sections.get(0));
        assertThat(sections.get(0).getTitle(), is("titre1"));
		assertNotNull(sections.get(1));
        assertThat(sections.get(1).getTitle(), is("titre3"));
		assertNotNull(sections.get(2));
        assertThat(sections.get(2).getTitle(), is("titre2"));
		assertNotNull(sections.get(3));
        assertThat(sections.get(3).getTitle(), is("titre4"));
    }
    
    @Test
    public void iCantMoveUpFirstSection() throws Exception {
    	HtmlPage page = createTestPage();
        createListSectionsInHtmlPage(page);
        DocumentModel doc = session.createDocument(page.getDocument());
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);

        assertNotNull(page);
        List<HtmlSection> sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(4));
		assertNotNull(sections.get(0));
        assertThat(sections.get(0).getTitle(), is("titre1"));
		assertNotNull(sections.get(1));
        assertThat(sections.get(1).getTitle(), is("titre2"));
		assertNotNull(sections.get(2));
        assertThat(sections.get(2).getTitle(), is("titre3"));
		assertNotNull(sections.get(3));
        assertThat(sections.get(3).getTitle(), is("titre4"));
        
        page.moveUp(0);
        doc = session.saveDocument(page.getDocument());
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);
        
        assertNotNull(page);
        sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(4));
		assertNotNull(sections.get(0));
        assertThat(sections.get(0).getTitle(), is("titre1"));
		assertNotNull(sections.get(1));
        assertThat(sections.get(1).getTitle(), is("titre2"));
		assertNotNull(sections.get(2));
        assertThat(sections.get(2).getTitle(), is("titre3"));
		assertNotNull(sections.get(3));
        assertThat(sections.get(3).getTitle(), is("titre4"));
    }

	private void createListSectionsInHtmlPage(HtmlPage page)
			throws ClientException {
		HtmlSection section = page.addSection();
        section.setTitle("titre1");
        section = page.addSection();
        section.setTitle("titre2");
        section = page.addSection();
        section.setTitle("titre3");
        section = page.addSection();
        section.setTitle("titre4");
	}
    
    @Test
    public void iCanMoveDownSection() throws Exception {
    	HtmlPage page = createTestPage();
        createListSectionsInHtmlPage(page);
        DocumentModel doc = session.createDocument(page.getDocument());
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);

        assertNotNull(page);
        List<HtmlSection> sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(4));
		assertNotNull(sections.get(0));
        assertThat(sections.get(0).getTitle(), is("titre1"));
		assertNotNull(sections.get(1));
        assertThat(sections.get(1).getTitle(), is("titre2"));
		assertNotNull(sections.get(2));
        assertThat(sections.get(2).getTitle(), is("titre3"));
		assertNotNull(sections.get(3));
        assertThat(sections.get(3).getTitle(), is("titre4"));
        
        page.moveDown(2);
        doc = session.saveDocument(page.getDocument());
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);
        
        assertNotNull(page);
        sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(4));
		assertNotNull(sections.get(0));
        assertThat(sections.get(0).getTitle(), is("titre1"));
		assertNotNull(sections.get(1));
        assertThat(sections.get(1).getTitle(), is("titre2"));
		assertNotNull(sections.get(2));
        assertThat(sections.get(2).getTitle(), is("titre4"));
		assertNotNull(sections.get(3));
        assertThat(sections.get(3).getTitle(), is("titre3"));
    }
    
    @Test
    public void iCantMoveDownLastSection() throws Exception {
    	HtmlPage page = createTestPage();
        createListSectionsInHtmlPage(page);
        DocumentModel doc = session.createDocument(page.getDocument());
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);

        assertNotNull(page);
        List<HtmlSection> sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(4));
		assertNotNull(sections.get(0));
        assertThat(sections.get(0).getTitle(), is("titre1"));
		assertNotNull(sections.get(1));
        assertThat(sections.get(1).getTitle(), is("titre2"));
		assertNotNull(sections.get(2));
        assertThat(sections.get(2).getTitle(), is("titre3"));
		assertNotNull(sections.get(3));
        assertThat(sections.get(3).getTitle(), is("titre4"));
        
        page.moveDown(3);
        doc = session.saveDocument(page.getDocument());
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);
        
        assertNotNull(page);
        sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(4));
		assertNotNull(sections.get(0));
        assertThat(sections.get(0).getTitle(), is("titre1"));
		assertNotNull(sections.get(1));
        assertThat(sections.get(1).getTitle(), is("titre2"));
		assertNotNull(sections.get(2));
        assertThat(sections.get(2).getTitle(), is("titre3"));
		assertNotNull(sections.get(3));
        assertThat(sections.get(3).getTitle(), is("titre4"));
    }
    
    @Test
    public void iCanMoveDownRow() throws Exception {
    	HtmlPage page = createTestPage();
    	HtmlSection section = page.addSection();
    	DocumentModel doc = createRowsForMove(page, section);
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);

        assertNotNull(page);
        List<HtmlSection> sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(1));
		HtmlSection htmlsection = sections.get(0);
		testCreatedRowsForMove(htmlsection);
        
        htmlsection.moveDown(2);
        doc = session.saveDocument(page.getDocument());
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);
        
        assertNotNull(page);
        sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(1));
		htmlsection = sections.get(0);
		assertNotNull(htmlsection);
		assertNotNull(htmlsection.getRows());
        assertThat(htmlsection.getRows().size(), is(4));

        assertNotNull(htmlsection.getRows().get(0));
        assertNotNull(htmlsection.getRows().get(0).content(0));
        assertNotNull(htmlsection.getRows().get(0).content(0).getHtml());
        assertThat(htmlsection.getRows().get(0).content(0).getHtml(), is("html1"));

        assertNotNull(htmlsection.getRows().get(1));
        assertNotNull(htmlsection.getRows().get(1).content(0));
        assertNotNull(htmlsection.getRows().get(1).content(0).getHtml());
        assertThat(htmlsection.getRows().get(1).content(0).getHtml(), is("html2"));

        assertNotNull(htmlsection.getRows().get(2));
        assertNotNull(htmlsection.getRows().get(2).content(0));
        assertNotNull(htmlsection.getRows().get(2).content(0).getHtml());
        assertThat(htmlsection.getRows().get(2).content(0).getHtml(), is("html4"));

        assertNotNull(htmlsection.getRows().get(3));
        assertNotNull(htmlsection.getRows().get(3).content(0));
        assertNotNull(htmlsection.getRows().get(3).content(0).getHtml());
        assertThat(htmlsection.getRows().get(3).content(0).getHtml(), is("html3"));
    }
    
    @Test
    public void iCanMoveUpRow() throws Exception {
    	HtmlPage page = createTestPage();
    	HtmlSection section = page.addSection();
    	DocumentModel doc = createRowsForMove(page, section);
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);

        assertNotNull(page);
        List<HtmlSection> sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(1));
		HtmlSection htmlsection = sections.get(0);
		testCreatedRowsForMove(htmlsection);
        
        htmlsection.moveUp(2);
        doc = session.saveDocument(page.getDocument());
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);
        
        assertNotNull(page);
        sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(1));
		htmlsection = sections.get(0);
		assertNotNull(htmlsection);
		assertNotNull(htmlsection.getRows());
        assertThat(htmlsection.getRows().size(), is(4));

        assertNotNull(htmlsection.getRows().get(0));
        assertNotNull(htmlsection.getRows().get(0).content(0));
        assertNotNull(htmlsection.getRows().get(0).content(0).getHtml());
        assertThat(htmlsection.getRows().get(0).content(0).getHtml(), is("html1"));

        assertNotNull(htmlsection.getRows().get(1));
        assertNotNull(htmlsection.getRows().get(1).content(0));
        assertNotNull(htmlsection.getRows().get(1).content(0).getHtml());
        assertThat(htmlsection.getRows().get(1).content(0).getHtml(), is("html3"));

        assertNotNull(htmlsection.getRows().get(2));
        assertNotNull(htmlsection.getRows().get(2).content(0));
        assertNotNull(htmlsection.getRows().get(2).content(0).getHtml());
        assertThat(htmlsection.getRows().get(2).content(0).getHtml(), is("html2"));

        assertNotNull(htmlsection.getRows().get(3));
        assertNotNull(htmlsection.getRows().get(3).content(0));
        assertNotNull(htmlsection.getRows().get(3).content(0).getHtml());
        assertThat(htmlsection.getRows().get(3).content(0).getHtml(), is("html4"));
    }
    
    @Test
    public void iCantMoveDownLastRow() throws Exception {
    	HtmlPage page = createTestPage();
    	HtmlSection section = page.addSection();
    	DocumentModel doc = createRowsForMove(page, section);
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);

        assertNotNull(page);
        List<HtmlSection> sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(1));
		HtmlSection htmlsection = sections.get(0);
		testCreatedRowsForMove(htmlsection);
        
        htmlsection.moveDown(3);
        doc = session.saveDocument(page.getDocument());
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);
        
        assertNotNull(page);
        sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(1));
		htmlsection = sections.get(0);
		testCreatedRowsForMove(htmlsection);
    }

	private void testCreatedRowsForMove(HtmlSection htmlsection) {
		assertNotNull(htmlsection);
		assertNotNull(htmlsection.getRows());
        assertThat(htmlsection.getRows().size(), is(4));

        assertNotNull(htmlsection.getRows().get(0));
        assertNotNull(htmlsection.getRows().get(0).content(0));
        assertNotNull(htmlsection.getRows().get(0).content(0).getHtml());
        assertThat(htmlsection.getRows().get(0).content(0).getHtml(), is("html1"));

        assertNotNull(htmlsection.getRows().get(1));
        assertNotNull(htmlsection.getRows().get(1).content(0));
        assertNotNull(htmlsection.getRows().get(1).content(0).getHtml());
        assertThat(htmlsection.getRows().get(1).content(0).getHtml(), is("html2"));

        assertNotNull(htmlsection.getRows().get(2));
        assertNotNull(htmlsection.getRows().get(2).content(0));
        assertNotNull(htmlsection.getRows().get(2).content(0).getHtml());
        assertThat(htmlsection.getRows().get(2).content(0).getHtml(), is("html3"));

        assertNotNull(htmlsection.getRows().get(3));
        assertNotNull(htmlsection.getRows().get(3).content(0));
        assertNotNull(htmlsection.getRows().get(3).content(0).getHtml());
        assertThat(htmlsection.getRows().get(3).content(0).getHtml(), is("html4"));
	}
    
    @Test
    public void iCantMoveUpFirstRow() throws Exception {
    	HtmlPage page = createTestPage();
    	HtmlSection section = page.addSection();
    	DocumentModel doc = createRowsForMove(page, section);
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);

        assertNotNull(page);
        List<HtmlSection> sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(1));
		HtmlSection htmlsection = sections.get(0);
		testCreatedRowsForMove(htmlsection);
        
        htmlsection.moveUp(0);
        doc = session.saveDocument(page.getDocument());
        session.save();
        
        doc = session.getDocument(doc.getRef());
        page = Tools.getAdapter(HtmlPage.class, doc, session);
        
        assertNotNull(page);
        sections = page.getSections();
		assertNotNull(sections);
        assertThat(sections.size(), is(1));
		htmlsection = sections.get(0);
		testCreatedRowsForMove(htmlsection);
    }

	private DocumentModel createRowsForMove(HtmlPage page, HtmlSection section)
			throws ClientException {
		section.addRow();
    	section.addRow();
    	section.addRow();
    	section.addRow();
    	List<HtmlRow> rows = section.getRows();
		rows.get(0).addContent(0, "html1");
    	rows.get(1).addContent(0, "html2");
    	rows.get(2).addContent(0, "html3");
    	rows.get(3).addContent(0, "html4");
        DocumentModel doc = session.createDocument(page.getDocument());
		return doc;
	}

    private HtmlPage retrieveTestPage() throws ClientException {
        DocumentModel doc = session.getDocument(new PathRef(TEST_PATHREF));
        return Tools.getAdapter(HtmlPage.class, doc, session);
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

        return Tools.getAdapter(HtmlPage.class, doc, session);

    }

    private HtmlPage createTestPage() throws ClientException {
        DocumentModel doc = session.createDocumentModel("/", "myPage",
                "HtmlPage");
        HtmlPage page = Tools.getAdapter(HtmlPage.class, doc, session);
        page.setTitle(TEST_TITLE);
        page.setDescription(TEST_DESCRIPTION);
        return page;

    }
}
