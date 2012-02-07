package com.leroymerlin.corp.fr.nuxeo.freemarker;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.leroymerlin.corp.fr.nuxeo.labs.site.test.SiteFeatures;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@RunWith(FeaturesRunner.class)
@Features(SiteFeatures.class)
@RepositoryConfig(cleanup=Granularity.METHOD)
public class TableOfContentsTest {
    private Configuration cfg;

    @Before
    public void doBefore() throws IOException {
        cfg = new Configuration();
        // Specify the data source where the template files come from.
        // Here I set a file directory for it:
        cfg.setDirectoryForTemplateLoading(
        new File(FileUtils.getResourcePathFromContext("testTemplates/")));
        // Specify how templates will see the data-model. This is an advanced
        // topic...
        // but just use this:
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setSharedVariable("tableOfContents", new TableOfContentsDirective());

    }
    
    @Test
    public void iCanGetTableOfContentsWithOneAnchor() throws Exception {
        String ftl = getTemplateOutputFor("toc1test.ftl");
        assertFalse(ftl.contains("[[TOC]]"));
        Document document = Jsoup.parse(ftl);
        Elements tocDiv = document.select("#toc");
        assertFalse(tocDiv.html().contains("[[TOC]]"));
        Elements tocUl = tocDiv.select("ul.page-toc");
        assertFalse(tocUl.isEmpty());
        Elements tocLis = tocUl.select("> li");
        assertFalse(tocLis.isEmpty());
        assertEquals(1, tocLis.size());
        Elements aHrefs = tocLis.get(0).select("> a");
        assertFalse(aHrefs.isEmpty());
        assertEquals(1, aHrefs.size());
        assertEquals("#section_1", aHrefs.get(0).attr("href"));
        assertEquals("titre section_1", aHrefs.get(0).html());
    }

    @Test
    public void tabeOfContentsNotGeneratedAsRequestedWithClass() throws Exception {
        String ftl = getTemplateOutputFor("toc5test.ftl");
        assertTrue(ftl.contains("[[TOC]]"));
        Document document = Jsoup.parse(ftl);
        Elements tocViewDiv = document.select("#tocView");
        assertFalse(tocViewDiv.html().contains("[[TOC]]"));
        Elements tocEditDiv = document.select("#tocEdit");
        assertTrue(tocEditDiv.html().contains("[[TOC]]"));
    }

    @Test
    public void tabeOfContentsNotGeneratedAsRequestedWithClassOnParent() throws Exception {
        String ftl = getTemplateOutputFor("toc2test.ftl");
        assertTrue(ftl.contains("[[TOC]]"));
        Document document = Jsoup.parse(ftl);
        Elements tocViewDiv = document.select("#tocView");
        assertFalse(tocViewDiv.html().contains("[[TOC]]"));
        Elements tocEditDiv = document.select("#tocEdit");
        assertTrue(tocEditDiv.html().contains("[[TOC]]"));
    }

    @Test
    public void tabeOfContentsNotGeneratedAsRequestedWithCustomClass() throws Exception {
        String ftl = getTemplateOutputFor("toc3test.ftl");
        assertTrue(ftl.contains("[[TOC]]"));
        Document document = Jsoup.parse(ftl);
        Elements tocViewDiv = document.select("#tocView");
        assertFalse(tocViewDiv.html().contains("[[TOC]]"));
        Elements tocEditDiv = document.select("#tocEdit");
        assertTrue(tocEditDiv.html().contains("[[TOC]]"));
    }
    
    @Test
    public void tablOfContentsGeneratedWithCustomTagOnly() throws Exception {
        String ftl = getTemplateOutputFor("toc4test.ftl");
        System.out.println(ftl);
        assertTrue(ftl.contains("[[TOC]]"));
        assertFalse(ftl.contains("[[TDM]]"));
    }
    
    @Test
    public void iCanSetCustomClassOnUl() throws Exception {
        String ftl = getTemplateOutputFor("toc6test.ftl");
        assertFalse(ftl.contains("[[TOC]]"));
        Document document = Jsoup.parse(ftl);
        Elements tocDiv = document.select("#toc");
        assertFalse(tocDiv.html().contains("[[TOC]]"));
        Elements tocUl = tocDiv.select("ul.page-tdm");
        assertFalse(tocUl.isEmpty());
        Elements tocLis = tocUl.select("> li");
        assertFalse(tocLis.isEmpty());
        assertEquals(1, tocLis.size());
        Elements aHrefs = tocLis.get(0).select("> a");
        assertFalse(aHrefs.isEmpty());
        assertEquals(1, aHrefs.size());
        assertEquals("#section_1", aHrefs.get(0).attr("href"));
        assertEquals("titre section_1", aHrefs.get(0).html());
    }
    
    @SuppressWarnings({ "rawtypes" })
    private String getTemplateOutputFor(String templateName) throws IOException, TemplateException {
        Map root = new HashMap();
        Template temp = cfg.getTemplate(templateName);
        StringWriter out = new StringWriter();
        temp.process(root, out);
        out.flush();
        return out.getBuffer().toString();
    }
}
