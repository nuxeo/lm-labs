package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.opensocial.container.shared.webcontent.UserPref;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class GadgetUtils {

    public static String encode(List<UserPref> prefs) {

        Joiner joiner = Joiner.on(",");

        return "{"
                + joiner.join(Lists.transform(prefs,
                        new Function<UserPref, String>() {

                            @Override
                            public String apply(UserPref pref) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("\"")
                                        .append(pref.getName())
                                        .append("\":{");
                                sb.append("\"name\":\"")
                                        .append(appendGadgetValueJson(pref.getName()))
                                        .append("\",");
                                String actualValue = appendGadgetValueJson(pref.getActualValue());
                                sb.append("\"value\":\"")
                                        .append(actualValue)
                                        .append("\",");

                                String defaultValue = appendGadgetValueJson(pref.getDefaultValue());
                                sb.append("\"default\":\"")
                                        .append(defaultValue)
                                        .append("\"");
                                return sb.append("}")
                                        .toString();
                            }

                            private String appendGadgetValueJson(String name) {
                                if (StringUtils.isEmpty(name)) {
                                    return "";
                                }
                                return name.replaceAll("\"", "\\\\\"");
                            }

                        })) + "}";

    }

}
