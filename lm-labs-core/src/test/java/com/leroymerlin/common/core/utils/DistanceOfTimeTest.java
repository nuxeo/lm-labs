package com.leroymerlin.common.core.utils;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;

public class DistanceOfTimeTest {


    @Test
    public void lessThanAMinuteInWord() throws Exception {
        DateTime now = new DateTime();
        assertThat(DistanceOfTime.inWord(now.minusSeconds(2), now), is("moins d'une minute"));
        assertThat(DistanceOfTime.inWord(now.minusSeconds(8), now), is("moins d'une minute"));
        assertThat(DistanceOfTime.inWord(now.minusSeconds(13), now), is("moins d'une minute"));
        assertThat(DistanceOfTime.inWord(now.minusSeconds(25), now), is("moins d'une minute"));
        assertThat(DistanceOfTime.inWord(now.minusSeconds(49), now), is("moins d'une minute"));
        assertThat(DistanceOfTime.inWord(now.minusSeconds(60), now), is("une minute"));

        assertThat(DistanceOfTime.inWord(now.minusSeconds(2), now, true), is("moins de 5 secondes"));
        assertThat(DistanceOfTime.inWord(now.minusSeconds(8), now, true), is("moins de 10 secondes"));
        assertThat(DistanceOfTime.inWord(now.minusSeconds(13), now, true), is("moins de 20 secondes"));
        assertThat(DistanceOfTime.inWord(now.minusSeconds(25), now, true), is("30 secondes"));
        assertThat(DistanceOfTime.inWord(now.minusSeconds(49), now, true), is("moins d'une minute"));
        assertThat(DistanceOfTime.inWord(now.minusSeconds(60), now, true), is("une minute"));
    }

    @Test
    public void otherDistanceInWords() throws Exception {
        DateTime now = new DateTime();
        assertThat(DistanceOfTime.inWord(now.minusMinutes(10), now), is("10 minutes"));
        assertThat(DistanceOfTime.inWord(now.minusMinutes(50), now), is("près d'une heure"));

        assertThat(DistanceOfTime.inWord(now.minusHours(3), now), is("près de 3 heures"));
        assertThat(DistanceOfTime.inWord(now.minusHours(25), now), is("1 jour"));

        assertThat(DistanceOfTime.inWord(now.minusDays(4), now), is("4 jours"));
        assertThat(DistanceOfTime.inWord(now.minusDays(35), now), is("près d'un mois"));
        assertThat(DistanceOfTime.inWord(now.minusDays(76), now), is("3 mois"));

        assertThat(DistanceOfTime.inWord(now.minusDays(370), now), is("près d'un an"));
        assertThat(DistanceOfTime.inWord(now.minusYears(4).minusDays(20), now), is("plus de 4 ans"));
        assertThat(DistanceOfTime.inWord(now.minusYears(2).minusDays(20), now), is("plus de 2 ans"));


    }





}
