package com.leroymerlin.common.core.adapter;

import java.util.Map;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;

public interface FilesAdapter {

    static final String FILES_FILES = "files:files";
    static final String FILE = "file";
    static final String FILENAME = "filename";
    public enum AfterUpdate {
        SAVE_SESSION, NONE;
    };

    FilesAdapter addFile(Blob file, String filename, AfterUpdate operation) throws ClientException;

    FilesAdapter addFile(Blob file, String filename) throws ClientException;

    FilesAdapter removeFile(String filename) throws ClientException;

    Blob getFile(String filename) throws ClientException;

    Map<String, Blob> getFiles() throws ClientException;

}
