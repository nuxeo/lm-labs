package com.leroymerlin.corp.fr.nuxeo.labs.site.services.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.directory.DirectoryException;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

import com.leroymerlin.corp.fr.nuxeo.labs.site.services.LabsThemeManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Directories;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;

public class LabsThemeManagerImpl extends DefaultComponent implements LabsThemeManager {

    private static final Log LOG = LogFactory.getLog(LabsThemeManagerImpl.class);
    
    @Override
    public void activate(ComponentContext context) throws Exception {
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getThemeList(String pathBase) {
        return (List<String>) CollectionUtils.intersection(getDirThemes(), LabsSiteWebAppUtils.getFoldersUnderFolder(pathBase + LabsSiteWebAppUtils.DIRECTORY_THEME));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getTemplateList(String pathBase) {
        return (List<String>) CollectionUtils.intersection(getDirPageTemplates(), LabsSiteWebAppUtils.getFoldersUnderFolder(pathBase + LabsSiteWebAppUtils.DIRECTORY_TEMPLATE));
    }

    public static List<String> getDirThemes() {
        return getDirList(Directories.THEMES);
    }

    public static List<String> getDirPageTemplates() {
        return getDirList(Directories.PAGE_TEMPLATES);
    }

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
}
