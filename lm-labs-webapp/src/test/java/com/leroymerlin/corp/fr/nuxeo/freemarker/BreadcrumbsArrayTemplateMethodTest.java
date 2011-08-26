package com.leroymerlin.corp.fr.nuxeo.freemarker;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nuxeo.common.utils.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class BreadcrumbsArrayTemplateMethodTest {

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
        cfg.setSharedVariable("breadcrumbsDocs", new BreadcrumbsArrayTemplateMethod());

    }

    @Test
    public void dateInWordsTemplateMethosWorksWithDate() throws Exception {
        assertThat(getTemplateOutputFor(new DateTime().minusMinutes(2).toDate()), is("2 minutes"));
    }

    @Ignore("TODO") @Test
    public void testExec() {
        fail("Not yet implemented");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private String getTemplateOutputFor(Object date) throws IOException, TemplateException {
        Map root = new HashMap();
        root.put("date", date);
        Template temp = cfg.getTemplate("breadcrumbstest.ftl");
        StringWriter out = new StringWriter();
        temp.process(root, out);
        out.flush();
        return out.getBuffer().toString();
    }
}
