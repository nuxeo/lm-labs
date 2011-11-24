package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.io.File;
import java.io.FilenameFilter;

public class DirectoryFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        File current = new File(dir, name);
        if (current.isDirectory()){
            return true;
        }
        return false;
    }

}
