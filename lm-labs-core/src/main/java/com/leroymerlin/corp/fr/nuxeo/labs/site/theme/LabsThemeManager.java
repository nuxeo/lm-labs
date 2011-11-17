package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;

import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.LabsTheme;

public interface LabsThemeManager {
    /**
     * Returns a list of available themes for this site
     * @return
     * @throws ClientException
     */
    List<LabsTheme> getThemes() throws ClientException;

    /**
     * Returns a named theme of the site
     * @param themeName
     * @return
     * @throws ClientException
     */
    LabsTheme getTheme(String themeName) throws ClientException;

    /**
     * Returns the current theme of the site
     * @return
     * @throws ClientException
     */
    LabsTheme getTheme() throws ClientException;

    /**
     * Sets the current theme
     * @param themeName
     * @throws ClientException
     */
    void setTheme(String themeName) throws ClientException;
}
