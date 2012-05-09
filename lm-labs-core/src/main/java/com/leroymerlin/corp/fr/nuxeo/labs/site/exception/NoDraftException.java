package com.leroymerlin.corp.fr.nuxeo.labs.site.exception;

import org.nuxeo.ecm.core.api.ClientException;


public class NoDraftException extends ClientException {

    private static final long serialVersionUID = 829907884555472415L;

    public NoDraftException() {
    }

    public NoDraftException(String message) {
        super(message);
    }

    public NoDraftException(String message, Exception cause) {
        super(message, cause);
    }


}
