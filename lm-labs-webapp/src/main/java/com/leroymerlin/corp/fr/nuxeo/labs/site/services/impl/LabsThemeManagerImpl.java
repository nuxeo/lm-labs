package com.leroymerlin.corp.fr.nuxeo.labs.site.services.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

import com.leroymerlin.corp.fr.nuxeo.labs.site.services.LabsThemeManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.DirectoriesUtils;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteConstants.Directories;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;

public class LabsThemeManagerImpl extends DefaultComponent implements LabsThemeManager {
    
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
        return DirectoriesUtils.getDirList(Directories.THEMES);
    }

    public static List<String> getDirPageTemplates() {
        return DirectoriesUtils.getDirList(Directories.PAGE_TEMPLATES);
    }
    
    @Override
    public DocumentModelList getDirFontSizes() {
        return DirectoriesUtils.getDirDocumentModelList(Directories.FONT_SIZES);
    }
    
    @Override
    public DocumentModelList getDirFontFamilies() {
        return DirectoriesUtils.getDirDocumentModelList(Directories.FONT_FAMILIES);
    }
}
