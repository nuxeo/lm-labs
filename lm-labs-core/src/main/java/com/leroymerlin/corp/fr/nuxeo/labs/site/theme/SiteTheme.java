package com.leroymerlin.corp.fr.nuxeo.labs.site.theme;

import java.util.Map;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface SiteTheme {

    String getName() throws ClientException;
    
    void setName(String name) throws ClientException;

    int getLogoPosX() throws ClientException;
    
    void setLogoPosX(int pos) throws ClientException;

    int getLogoPosY() throws ClientException;
    
    void setLogoPosY(int pos) throws ClientException;

    int getLogoResizeRatio() throws ClientException;

    void setLogoResizeRatio(int pos) throws ClientException;

    int getLogoWidth() throws ClientException;

    Blob getBanner() throws ClientException;
    
    void setBanner(Blob blob) throws ClientException;

    Blob getLogo() throws ClientException;
    
    void setLogo(Blob blob) throws ClientException;

    DocumentModel getDocument();

    String getStyle() throws ClientException;
    
    void setStyle(String style) throws ClientException;

    Map<String, String> getProperties() throws ClientException;
    
    void setProperties(Map<String, String> properties) throws ClientException;
}
