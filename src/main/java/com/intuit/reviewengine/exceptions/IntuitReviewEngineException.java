package com.intuit.reviewengine.exceptions;

import org.springframework.http.HttpStatus;

/**
 * A run time exception.
 * @author kchopperla
 *
 */
public class IntuitReviewEngineException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final HttpStatus httpStatus;
    private final String messageCode;
    private final String[] args;

    public IntuitReviewEngineException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.messageCode = null;
        this.args = null;
    }

    public IntuitReviewEngineException(HttpStatus httpStatus, String messageCode) {
        this.httpStatus = httpStatus;
        this.messageCode = messageCode;
        this.args = null;
    }

    public IntuitReviewEngineException(HttpStatus httpStatus, String messageCode, String[] args) {
        this.httpStatus = httpStatus;
        this.messageCode = messageCode;
        this.args = args;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public String[] getArgs() {
        return args;
    }

    @Override
    public String getMessage() {
        return getMessageCode();
    }
}
