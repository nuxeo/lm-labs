package com.leroymerlin.corp.fr.nuxeo.labs.site.html;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

public interface ChangeListener {
    public void onChange(Object obj, CoreSession session) throws ClientException;
}
