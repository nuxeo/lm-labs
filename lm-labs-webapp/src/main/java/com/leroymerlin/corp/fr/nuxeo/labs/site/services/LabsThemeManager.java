package com.leroymerlin.corp.fr.nuxeo.labs.site.services;

import java.util.List;

public interface LabsThemeManager {
    
    List<String> getThemeList(String pathBase);
    
    List<String> getTemplateList(String pathBase);

}
