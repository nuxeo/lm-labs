package com.leroymerlin.common.freemarker;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.nuxeo.common.utils.FileUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class BytesFormatTest {

    private Configuration cfg;

    @Before
    public void doBefore() throws Exception {
        cfg = new Configuration();
        // Specify the data source where the template files come from.
        // Here I set a file directory for it:
        cfg.setDirectoryForTemplateLoading(
        new File(FileUtils.getResourcePathFromContext("testTemplates/")));
        // Specify how templates will see the data-model. This is an advanced
        // topic...
        // but just use this:
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setSharedVariable("bytesFormat", new BytesFormatTemplateMethod());
    }

    @Test
    public void test() throws Exception {
        assertEquals("2000 o", getTemplateOutputFor(2000, "", "fr_FR"));
        assertEquals("2 Ko", getTemplateOutputFor(2000, "K", "fr_FR"));
        assertEquals("2 Ko", getTemplateOutputFor(2500, "K", "fr_FR"));
        assertEquals("3 Ko", getTemplateOutputFor(2700, "K", "fr_FR"));
        assertEquals("1 Mo", getTemplateOutputFor(1024*1024, "M", "fr_FR"));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private String getTemplateOutputFor(long size, String format, String language) throws IOException, TemplateException {
        Map root = new HashMap();
        root.put("size", size);
        root.put("format", format);
        root.put("language", language);
        Template temp = cfg.getTemplate("bytesformattest.ftl");
        StringWriter out = new StringWriter();
        temp.process(root, out);
        out.flush();
        return out.getBuffer().toString();
    }
}
