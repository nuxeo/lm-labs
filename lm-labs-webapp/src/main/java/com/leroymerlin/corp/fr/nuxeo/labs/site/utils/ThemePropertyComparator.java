package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.Comparator;
import java.util.Map;

import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.bean.ThemeProperty;

public class ThemePropertyComparator implements Comparator<String> {
    Map<String, ThemeProperty> properties;

    public ThemePropertyComparator(
            Map<String, ThemeProperty> properties) {
        this.properties = properties;
    }

    @Override
    public int compare(String key1, String key2) {
        ThemeProperty tp1 = (ThemeProperty) properties.get(key1);
        ThemeProperty tp2 = (ThemeProperty) properties.get(key2);

        return tp1.getOrderNumber() - tp2.getOrderNumber();
    }
}