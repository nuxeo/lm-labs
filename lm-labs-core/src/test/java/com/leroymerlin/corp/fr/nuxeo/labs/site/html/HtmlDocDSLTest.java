package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.ClientException;
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
@RepositoryConfig(cleanup = Granularity.METHOD)
public class HtmlDocDSLTest {
	@Inject
	CoreSession session;

	@Test
	public void canNavigateThruModel() throws Exception {
		HtmlPage page = createTestPage();

		page.addSection().addRow().addContent(3, "Hello World");

		assertThat(page.getSections().size(), is(1));

		assertThat(page.section(0).getRows().size(), is(1));
		assertThat(page.section(0).row(0).getContents().size(), is(1));
		HtmlContent content = page.section(0).row(0).content(0);
		assertThat(content.getColNumber(), is(3));
		assertThat(content.getHtml(), is("Hello World"));

	}

	@Test
	public void canEditSectionModel() throws Exception {
		HtmlPage page = createTestPage();

		// Insert Section
		page.addSection().addRow().addContent(1, "Section 1");
		page.addSection().addRow().addContent(1, "Section 2");
		page.addSection().addRow().addContent(1, "Section 3");

		assertThat(page.section(0).row(0).content(0).getHtml(), is("Section 1"));
		assertThat(page.section(1).row(0).content(0).getHtml(), is("Section 2"));
		assertThat(page.section(2).row(0).content(0).getHtml(), is("Section 3"));

		page.section(1).insertBefore().addRow().addContent(1, "Section 1 bis");
		assertThat(page.section(1).row(0).content(0).getHtml(),
				is("Section 1 bis"));
		assertThat(page.section(2).row(0).content(0).getHtml(), is("Section 2"));

		page.section(1).remove();

		assertThat(page.section(0).row(0).content(0).getHtml(), is("Section 1"));
		assertThat(page.section(1).row(0).content(0).getHtml(), is("Section 2"));
		assertThat(page.section(2).row(0).content(0).getHtml(), is("Section 3"));

	}

	@Test
	public void canEditRowModel() throws Exception {
		HtmlPage page = createTestPage();

		HtmlSection section = page.addSection();

		section.addRow().addContent(1, "Row 1");
		section.addRow().addContent(1, "Row 2");
		section.addRow().addContent(1, "Row 3");

		section.row(1).insertBefore().addContent(1, "Row 1 bis");

		assertThat(page.section(0).row(1).content(0).getHtml(), is("Row 1 bis"));
		assertThat(page.section(0).row(2).content(0).getHtml(), is("Row 2"));

		section.row(1).remove();

		assertThat(page.section(0).row(0).content(0).getHtml(), is("Row 1"));
		assertThat(page.section(0).row(1).content(0).getHtml(), is("Row 2"));
		assertThat(page.section(0).row(2).content(0).getHtml(), is("Row 3"));
	}

	@Test
	public void canEditContentModel() throws Exception {
		HtmlPage page = createTestPage();

		HtmlRow row = page.addSection().addRow();

		row.addContent(1, "Content 1");
		row.addContent(1, "Content 2");
		row.addContent(1, "Content 3");

		assertThat(page.section(0).row(0).content(0).getHtml(), is("Content 1"));
		assertThat(page.section(0).row(0).content(1).getHtml(), is("Content 2"));
		assertThat(page.section(0).row(0).content(2).getHtml(), is("Content 3"));

		row.content(1).insertBefore(1, "Content 1 bis");

		assertThat(page.section(0).row(0).content(1).getHtml(),
				is("Content 1 bis"));
		assertThat(page.section(0).row(0).content(2).getHtml(), is("Content 2"));
		
		row.content(1).remove();
		assertThat(page.section(0).row(0).content(1).getHtml(),
				is("Content 2"));
		
		page.section(0).row(0).content(1).setHtml("Content 2 modified");
		page.section(0).row(0).content(1).setColNumber(2);
		
		assertThat(page.section(0).row(0).content(1).getHtml(),
				is("Content 2 modified"));
		assertThat(page.section(0).row(0).content(1).getColNumber(),
				is(2));
		
		
		session.createDocument(page.getDocument());
		
		page = retrieveTestPage();
		
		assertThat(page.section(0).row(0).content(0).getHtml(), is("Content 1"));

		assertThat(page.section(0).row(0).content(1).getHtml(),
				is("Content 2 modified"));
		assertThat(page.section(0).row(0).content(1).getColNumber(),
				is(2));
		
		assertThat(page.section(0).row(0).content(2).getHtml(), is("Content 3"));

	}

	private HtmlPage createTestPage() throws ClientException {
		DocumentModel doc = session.createDocumentModel("/", "myPage",
				"HtmlPage");
		HtmlPage page = Tools.getAdapter(HtmlPage.class, doc, session);
		page.setTitle("Title");
		page.setDescription("Description");
		return page;

	}
	
	private HtmlPage retrieveTestPage() throws ClientException {
		DocumentModel doc = session.getDocument(new PathRef("/myPage"));
		return Tools.getAdapter(HtmlPage.class, doc, session);
	}
	

}
