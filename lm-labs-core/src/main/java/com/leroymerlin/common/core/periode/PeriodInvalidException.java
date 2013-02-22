package com.leroymerlin.common.core.periode;

public class PeriodInvalidException extends Exception {

    private static final long serialVersionUID = 1L;

    private final String message;

    public PeriodInvalidException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
