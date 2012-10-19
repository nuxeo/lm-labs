package com.leroymerlin.corp.fr.nuxeo.labs.site.utils;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.directory.DirectoryException;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.runtime.api.Framework;

import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Directories;

public class DirectoriesUtils {

    private static final Log LOG = LogFactory.getLog(DirectoriesUtils.class);

    public static List<String> getDirList(Directories directory) {
        List<String> list = new LinkedList<String>();
        Session session = null;
        try {
            DirectoryService directoryService = Framework.getService(DirectoryService.class);
            session = directoryService.open(directory.dirName());
            Map<String, String> orderBy = new LinkedHashMap<String, String>();
            Map<String, Serializable> filter = Collections.emptyMap();
            Set<String> fulltext = Collections.emptySet();
            orderBy.put(directory.orderingField(), "asc");
            for (DocumentModel entry : session.query(filter, fulltext, orderBy)) {
                list.add((String) entry.getPropertyValue(directory.idField()));
            }
        } catch (Exception e) {
            LOG.error(e, e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (DirectoryException e) {
                    LOG.error("Unable to close session: " + e.getMessage());
                }
            }
        }
        return list;
    }


    public static DocumentModelList getDirDocumentModelList(Directories directory) {
        Map<String, Serializable> filter = Collections.emptyMap();
        return getDirDocumentModelList(directory, filter);
    }
    
    public static DocumentModelList getDirDocumentModelList(Directories directory, Map<String, Serializable> filter) {
        DocumentModelList list = new DocumentModelListImpl();
        Session session = null;
        try {
            DirectoryService directoryService = Framework.getService(DirectoryService.class);
            session = directoryService.open(directory.dirName());
            Map<String, String> orderBy = new LinkedHashMap<String, String>();
            Set<String> fulltext = Collections.emptySet();
            orderBy.put(directory.orderingField(), "asc");
            list = session.query(filter, fulltext, orderBy);
        } catch (Exception e) {
            LOG.error(e, e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (DirectoryException e) {
                    LOG.error("Unable to close session: " + e.getMessage());
                }
            }
        }
        return list;
    }

    public static Map<String, String> getDirMap(Directories directory) {
        Map<String, String> map = new LinkedHashMap<String, String>();
        Session session = null;
        try {
            DirectoryService directoryService = Framework.getService(DirectoryService.class);
            session = directoryService.open(directory.dirName());
            Map<String, String> orderBy = new LinkedHashMap<String, String>();
            Map<String, Serializable> filter = Collections.emptyMap();
            Set<String> fulltext = Collections.emptySet();
            orderBy.put(directory.orderingField(), "asc");
            for (DocumentModel entry : session.query(filter, fulltext, orderBy)) {
                map.put((String) entry.getPropertyValue(directory.labelField()), (String) entry.getPropertyValue(directory.idField()));
            }
        } catch (Exception e) {
            LOG.error(e, e);
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (DirectoryException e) {
                    LOG.error("Unable to close session: " + e.getMessage());
                }
            }
        }
        return map;
    }
}
