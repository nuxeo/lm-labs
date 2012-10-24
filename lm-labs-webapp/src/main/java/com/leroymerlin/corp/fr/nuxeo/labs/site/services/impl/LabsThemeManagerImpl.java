package com.leroymerlin.corp.fr.nuxeo.labs.site.services.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;
import com.leroymerlin.corp.fr.nuxeo.labs.LabsWebAppActivator;
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
        Enumeration<URL> entries = LabsWebAppActivator.getDefault().getBundle().findEntries(
                LabsSiteWebAppUtils.DIRECTORY_THEME, "specific.less", true);

        List<String> folderList = new ArrayList<String>();
        while (entries.hasMoreElements()) {
            URL url = entries.nextElement();
            String[] nodes = url.getPath().split("/");
            if (nodes.length > 1) {
                String lastname = nodes[nodes.length - 2];
                folderList.add(lastname);
            }
        }
        return (List<String>) CollectionUtils.intersection(getDirThemes(),
                folderList);

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
