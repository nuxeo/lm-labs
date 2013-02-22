package com.leroymerlin.common.core.utils;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * Class to slugify strings for SEO-friendly urls
 * @author jsk, www.maddemcode.com
 */
public class Slugify {
    
    public static String slugify(String input) {
        return slugify(input, true);
    }

    public static String slugify(String input, boolean toLower) {
        if (input == null || input.length() == 0) return "";
        String toReturn = normalize(input);
        toReturn = toReturn.replace(" ", "-");
        toReturn = toReturn.replace("?", "");
        toReturn = toReturn.replace("&", "");
        toReturn = toReturn.replace(";", "");
        toReturn = toReturn.replace(":", "");
        toReturn = toReturn.replace("$", "");
        toReturn = toReturn.replace("&", "");
        toReturn = toReturn.replace("+", "");
        toReturn = toReturn.replace("=", "");
        toReturn = toReturn.replace("?", "");
        toReturn = toReturn.replace("[", "");
        toReturn = toReturn.replace("]", "");
        toReturn = toReturn.replace("@", "_at_");
        if (toLower) {
            toReturn = toReturn.toLowerCase();
        }
        return toReturn;
    }

    private static String normalize(String input) {
        if (input == null || input.length() == 0) return "";
        return Normalizer.normalize(input, Form.NFD).replaceAll("[^\\p{ASCII}]","");
    }
}