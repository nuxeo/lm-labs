package com.leroymerlin.common.core.utils;

import org.joda.time.DateTime;

public final class DistanceOfTime {

    private static Long ONE_SECOND_IN_MILLIS = 1000L;
    private static int ONE_MINUTE_IN_SECONDS = 60;

    private DistanceOfTime() {
    }

    public static String inWord(DateTime from) {
        return inWord(from, new DateTime());
    }

    public static String inWord(DateTime from, DateTime to) {
        return inWord(from, to, false);
    }

    public static String inWord(DateTime from, DateTime to,
            boolean includeSeconds) {
        // TODO Auto-generated method stub

        Long diff = (to.getMillis() / ONE_SECOND_IN_MILLIS)
                - (from.getMillis() / ONE_SECOND_IN_MILLIS);

        Double diffInSeconds = Math.floor(diff.doubleValue());
        Double diffInMinutes = Math.floor(diffInSeconds / ONE_MINUTE_IN_SECONDS);

        if (diffInMinutes <= 1) {
            if (!includeSeconds) {
                return diffInMinutes == 0 ? "moins d'une minute" : "une minute";
            }

            if (diffInSeconds <= 5) {
                return "moins de 5 secondes";
            } else if (diffInSeconds >= 6 && diffInSeconds <= 10) {
                return "moins de 10 secondes";
            } else if (diffInSeconds >= 11 && diffInSeconds <= 20) {
                return "moins de 20 secondes";
            } else if (diffInSeconds >= 21 && diffInSeconds <= 40) {
                return "30 secondes";
            } else if (diffInSeconds >= 41 && diffInSeconds <= 59) {
                return "moins d'une minute";
            } else {
                return "une minute";
            }

        } else if (diffInMinutes >= 2 && diffInMinutes <= 44) {
            return diffInMinutes.intValue() + " minutes";
        } else if (diffInMinutes >= 45 && diffInMinutes <= 89) {
            return "près d'une heure";
        } else if (diffInMinutes >= 90 && diffInMinutes <= 1439) {
            long diffInHours = Math.round(diffInMinutes / 60);
            return "près de " + diffInHours + " heures";
        } else if (diffInMinutes >= 1440 && diffInMinutes <= 2879) {
            return "1 jour";
        } else if (diffInMinutes >= 2880 && diffInMinutes <= 43199) {
            long diffInDays = Math.round(diffInMinutes / (60 * 24 ));
            return diffInDays + " jours";
        } else if (diffInMinutes >= 43200 && diffInMinutes <= 86399) {
            return "près d'un mois";
        } else if (diffInMinutes >= 86400 && diffInMinutes <= 525959) {
            long diffInMonth = Math.round(diffInMinutes / (60 * 24 * 30));
            return diffInMonth + " mois";
        } else if (diffInMinutes >= 525960 && diffInMinutes <= 1051919) {
            return "près d'un an";
        } else {
            long diffInYear = Math.round(Math.floor(diffInMinutes / (60 * 24 * 365)));
            return "plus de " + diffInYear  + " ans";
        }



    }

}
