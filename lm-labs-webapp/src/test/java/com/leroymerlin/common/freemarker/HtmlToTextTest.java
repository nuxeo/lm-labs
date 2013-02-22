package com.leroymerlin.common.freemarker;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.leroymerlin.common.freemarker.HtmlToText;

import freemarker.template.TemplateModelException;

public class HtmlToTextTest {

    @Test
    public void tagsAreRemoved() throws Exception {
        assertThat(runMethod("<p>Toto</p>"), is("Toto"));
    }

    @Test
    public void nonClosedTagsAreRemove() throws Exception {
        assertThat(runMethod("<p>Toto"), is("Toto"));
    }

    @Test
    public void moreComplexTest() throws Exception {
        assertThat(runMethod("Toto <span style=\"text-"), is("Toto"));
    }


    private List<String> getArgs(String arg) {
        return Arrays.asList(new String[] { arg });
    }

    private String runMethod(String arg) throws TemplateModelException {
        HtmlToText method = new HtmlToText();
        return (String) method.exec(getArgs(arg));
    }

}
