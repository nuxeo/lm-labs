package com.leroymerlin.common.core.utils;


import java.io.File;

import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;

public class BlobUtils {

    public static FileBlob createBlob(String path, String filename,
            String mimeType) {
        FileBlob blob = new FileBlob(getFileFromPath(path));
        blob.setMimeType(mimeType);
        blob.setFilename(filename);
        return blob;
    }

    public static File getFileFromPath(String path) {
        File file = FileUtils.getResourceFileFromContext(path);
        assert file != null;
        assert file.length() > 0;
        return file;
    }
}
