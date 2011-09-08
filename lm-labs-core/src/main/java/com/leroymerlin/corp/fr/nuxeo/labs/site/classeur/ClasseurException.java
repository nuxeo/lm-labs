package com.leroymerlin.corp.fr.nuxeo.labs.site.classeur;

import org.nuxeo.ecm.core.api.ClientException;

public class ClasseurException extends ClientException {

    public ClasseurException(String message) {
        super(message);
    }

    public ClasseurException(String message, Exception e) {
        super(message,e);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
