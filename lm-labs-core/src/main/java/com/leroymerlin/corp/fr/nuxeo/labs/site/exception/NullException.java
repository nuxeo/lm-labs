package com.leroymerlin.corp.fr.nuxeo.labs.site.exception;

public class NullException extends Exception {

    private static final long serialVersionUID = 1L;

    public NullException() {
        super();
    }

    public NullException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public NullException(String arg0) {
        super(arg0);
    }

    public NullException(Throwable arg0) {
        super(arg0);
    }

}
