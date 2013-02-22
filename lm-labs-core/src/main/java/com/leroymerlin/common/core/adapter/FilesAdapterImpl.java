package com.leroymerlin.common.core.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;

public class FilesAdapterImpl implements FilesAdapter {
    
    private DocumentModel doc;

    private static final Log log = LogFactory.getLog(FilesAdapterImpl.class);

    public FilesAdapterImpl(DocumentModel doc) {
        this.doc = doc;
    }

    public FilesAdapter addFile(Blob file, String filename)
            throws ClientException {
        return addFile(file, filename, AfterUpdate.SAVE_SESSION);
    }

    public FilesAdapter addFile(Blob file, String filename, AfterUpdate operation)
            throws ClientException {
        try {
            @SuppressWarnings("unchecked")
            ArrayList<Map<String, Serializable>> files = (ArrayList<Map<String, Serializable>>) doc.getPropertyValue(FILES_FILES);
            boolean isUpdate = false;
            for (Map<String, Serializable> map : files) {
                if (map.containsKey(FILENAME)
                        && filename.equals(map.get(FILENAME))) {
                    map.put(FILE, (Serializable) file);
                    isUpdate = true;
                    break;
                }

            }

            if (!isUpdate) {
                Map<String, Serializable> fileMap = new HashMap<String, Serializable>();
                fileMap.put(FILE, (Serializable) file);
                fileMap.put(FILENAME, filename);
                files.add(fileMap);
            }
            doc.setPropertyValue(FILES_FILES, files);
            CoreSession session = doc.getCoreSession();
            doc = session.saveDocument(doc);
            if (operation == AfterUpdate.SAVE_SESSION) {
                session.save();
            }
        } catch (PropertyException e) {
            log.error("No Property " + FILES_FILES + " for " + doc.getType());
        }
        return this;
    }

    public Blob getFile(String filename) throws ClientException {
        Map<String, Blob> files = getFiles();

        if (files.containsKey(filename) && files.get(filename) != null) {
            return (Blob) files.get(filename);
        }
        return null;
    }

    public FilesAdapter removeFile(String filename) throws ClientException {
        return addFile(null, filename);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Blob> getFiles() throws ClientException {
        ArrayList<Map<String, Serializable>> files = (ArrayList<Map<String, Serializable>>) doc.getPropertyValue(FILES_FILES);
        Map<String, Blob> blobs = new HashMap<String, Blob>();
        for (Map<String, Serializable> map : files) {
            if (map.containsKey(FILENAME)) {
                if (map.get(FILE) != null && map.get(FILENAME) != null) {
                    blobs.put(map.get(FILENAME)
                            .toString(), (Blob) map.get(FILE));
                }
            }
        }
        return blobs;
    }

}
