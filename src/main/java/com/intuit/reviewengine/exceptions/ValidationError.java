package com.intuit.reviewengine.exceptions;

public class ValidationError {

    private String message;
    private final String[] codes;

    public ValidationError(String message, String[] codes) {
        this.message = message;
        this.codes = codes;
    }

    public String getMessage() {
        return message;
    }

    public String[] getCodes() {
        return codes;
    }
}