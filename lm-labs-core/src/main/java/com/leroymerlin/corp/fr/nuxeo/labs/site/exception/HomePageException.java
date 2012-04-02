package com.leroymerlin.corp.fr.nuxeo.labs.site.exception;

import org.nuxeo.ecm.core.api.ClientException;


public class HomePageException extends ClientException {

    private static final long serialVersionUID = 829907884555472415L;

    public HomePageException() {
    }

    public HomePageException(String message) {
        super(message);
    }

    public HomePageException(String message, Exception cause) {
        super(message, cause);
    }


}
