package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import java.util.Map;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

import com.leroymerlin.corp.fr.nuxeo.labs.site.theme.bean.ThemeProperty;

public interface SiteTheme {

    String getName() throws ClientException;
    
    void setName(String name) throws ClientException;

    int getLogoPosX() throws ClientException;
    
    void setLogoPosX(int pos) throws ClientException;

    int getLogoPosY() throws ClientException;
    
    void setLogoPosY(int pos) throws ClientException;

    int getLogoResizeRatio() throws ClientException;

    void setLogoResizeRatio(int pos) throws ClientException;

    int getLogoAreaHeight() throws ClientException;

    void setLogoAreaHeight(int height) throws ClientException;

    int getLogoWidth() throws ClientException;

    Blob getBanner() throws ClientException;
    
    void setBanner(Blob blob) throws ClientException;

    Blob getLogo() throws ClientException;
    
    void setLogo(Blob blob) throws ClientException;

    DocumentModel getDocument();

    String getStyle() throws ClientException;
    
    void setStyle(String style) throws ClientException;

    Map<String, ThemeProperty> getProperties() throws ClientException;
    
    void setProperties(Map<String, ThemeProperty> properties) throws ClientException;

    long getLastRead() throws ClientException;
    
    void setLastRead(long lastRead) throws ClientException;
}
