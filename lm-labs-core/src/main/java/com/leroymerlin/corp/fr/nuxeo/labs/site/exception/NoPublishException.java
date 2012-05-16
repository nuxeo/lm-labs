package com.leroymerlin.corp.fr.nuxeo.labs.site.exception;

import org.nuxeo.ecm.core.api.ClientException;


public class NoPublishException extends ClientException {

    private static final long serialVersionUID = 829907884555472415L;

    public NoPublishException() {
    }

    public NoPublishException(String message) {
        super(message);
    }

    public NoPublishException(String message, Exception cause) {
        super(message, cause);
    }


}
