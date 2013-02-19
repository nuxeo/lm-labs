package com.leroymerlin.corp.fr.nuxeo.labs.site.operations;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractMakeAbsolutePath {

    protected static final String PARENT_PATH = "../";

    protected String makeAbsolutePath(final String startPath, final String endPath) {
        int count = StringUtils.countMatches(endPath, PARENT_PATH);
        if (count > 0 || !StringUtils.startsWith(endPath, "/")) {
            String basePath = startPath;
            String newEndPath = endPath;
            for (int i = 0; i < count; i++) {
                basePath = StringUtils.substringBeforeLast(basePath, "/");
                newEndPath = StringUtils.substringAfter(newEndPath, PARENT_PATH);
            }
            return basePath + "/" + newEndPath;
        }
        return endPath;
    }
}
