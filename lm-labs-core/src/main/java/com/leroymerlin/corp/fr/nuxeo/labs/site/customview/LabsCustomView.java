package com.leroymerlin.corp.fr.nuxeo.labs.site.customview;

import org.nuxeo.ecm.core.api.ClientException;

public interface LabsCustomView {
    
    public static final String PAGE_DEFAULT_VIEW = "default";
    
    String getContentView() throws ClientException;

}
