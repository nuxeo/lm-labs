package com.leroymerlin.corp.fr.nuxeo.labs.site.exception;

import org.nuxeo.ecm.webengine.WebException;

public class NotAuthorizedException extends WebException {

    private static final long serialVersionUID = 1L;

    public NotAuthorizedException(String message) {
        super(message, 202);
    }

    public NotAuthorizedException(String message, Throwable cause) {
        super(message, cause, 202);
    }
}