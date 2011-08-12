package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class BytesFormatUtilsTest {

    @Test
    public void formatZeroKilo() throws Exception {
        assertEquals("0 Ko", BytesFormatUtils.BytesInWord(0, "K", "fr_FR"));
    }

    @Test
    public void format1Kilo() throws Exception {
        assertEquals("1 Ko", BytesFormatUtils.BytesInWord(1024, "K", "fr_FR"));
    }

    @Test
    public void format1023octets() throws Exception {
        assertEquals("1 Ko", BytesFormatUtils.BytesInWord(1023, "K", "fr_FR"));
    }

    @Test
    public void format511octets() throws Exception {
        assertEquals("0 Ko", BytesFormatUtils.BytesInWord(511, "K", "fr_FR"));
    }

    @Test
    public void format512octets() throws Exception {
        assertEquals("1 Ko", BytesFormatUtils.BytesInWord(512, "K", "fr_FR"));
    }

    @Test
    public void formatZeroMeg() throws Exception {
        assertEquals("0 Mo", BytesFormatUtils.BytesInWord(0, "M", "fr_FR"));
    }

    @Ignore("TODO") @Test
    public void formatZeroMegEn() throws Exception {
        assertEquals("0 MB", BytesFormatUtils.BytesInWord(0, "M", "en_US"));
    }

}
