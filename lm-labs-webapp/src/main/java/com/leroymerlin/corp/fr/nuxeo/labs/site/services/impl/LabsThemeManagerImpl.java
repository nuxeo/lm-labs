package com.leroymerlin.corp.fr.nuxeo.labs.site.services.impl;

import java.util.List;

import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

import com.leroymerlin.corp.fr.nuxeo.labs.site.services.LabsThemeManager;
import com.leroymerlin.corp.fr.nuxeo.labs.site.utils.LabsSiteWebAppUtils;

public class LabsThemeManagerImpl extends DefaultComponent implements LabsThemeManager {
    
    @Override
    public void activate(ComponentContext context) throws Exception {
    }

    @Override
    public List<String> getThemeList(String pathBase) {
        return LabsSiteWebAppUtils.getFoldersUnderFolder(pathBase + LabsSiteWebAppUtils.DIRECTORY_THEME);
    }

    @Override
    public List<String> getTemplateList(String pathBase) {
        return LabsSiteWebAppUtils.getFoldersUnderFolder(pathBase + LabsSiteWebAppUtils.DIRECTORY_TEMPLATE);
    }

}
