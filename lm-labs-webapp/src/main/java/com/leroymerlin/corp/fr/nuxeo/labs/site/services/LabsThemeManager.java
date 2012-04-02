package com.leroymerlin.corp.fr.nuxeo.labs.site.services;

import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModelList;

public interface LabsThemeManager {
    
    List<String> getThemeList(String pathBase);
    
    List<String> getTemplateList(String pathBase);

    DocumentModelList getDirFontSizes();
    
    DocumentModelList getDirFontFamilies();
}
