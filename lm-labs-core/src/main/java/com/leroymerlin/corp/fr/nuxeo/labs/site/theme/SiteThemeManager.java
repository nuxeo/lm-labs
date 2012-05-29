package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.SiteTheme;

public interface SiteThemeManager {
    /**
     * Returns a list of available themes for this site
     * @return
     * @throws ClientException
     */
    List<SiteTheme> getThemes(CoreSession session) throws ClientException;

    /**
     * Returns a named theme of the site
     * @param themeName
     * @return
     * @throws ClientException
     */
    SiteTheme getTheme(String themeName, CoreSession session) throws ClientException;

    /**
     * Returns the current theme of the site
     * @return
     * @throws ClientException
     */
    SiteTheme getTheme(CoreSession session) throws ClientException;

    /**
     * Sets the current theme
     * @param themeName
     * @throws ClientException
     */
    void setTheme(String themeName, CoreSession session) throws ClientException;
}
