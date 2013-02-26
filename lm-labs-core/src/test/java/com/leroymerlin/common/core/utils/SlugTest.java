package com.leroymerlin.common.core.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

public class SlugTest {

    @Test
    public void canSlugSomeTitle() throws Exception {
        assertThat(Slugify.slugify("Un titre comme \u00e7a ?"), is("un-titre-comme-ca-"));
    }

    @Ignore @Test
    public void canSlugSimplequote() throws Exception {
        assertFalse(Slugify.slugify("'titre'").contains("'"));
    }

    @Ignore @Test
    public void canSlugDoublequote() throws Exception {
        assertFalse(Slugify.slugify("\"titre\"").contains("\""));
    }

    @Test
    public void canSlugSomeTitleNoToLower() throws Exception {
        assertThat(Slugify.slugify("Un titre comme \u00e7a ?", false), is("Un-titre-comme-ca-"));
    }
}
