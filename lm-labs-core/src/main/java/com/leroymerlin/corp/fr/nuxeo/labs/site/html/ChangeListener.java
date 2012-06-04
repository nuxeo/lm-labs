package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import org.nuxeo.ecm.core.api.ClientException;

public interface ChangeListener {
    public void onChange(Object obj) throws ClientException;
}
