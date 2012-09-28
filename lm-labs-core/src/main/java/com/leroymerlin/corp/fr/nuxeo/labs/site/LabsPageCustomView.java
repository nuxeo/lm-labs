package com.leroymerlin.corp.fr.nuxeo.labs.site;

import org.nuxeo.ecm.core.api.ClientException;

public interface LabsPageCustomView extends LabsCustomView {
    
    boolean setCustomView(String view) throws ClientException;

}
