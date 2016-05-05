package com.intuit.reviewengine.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationErrors {

    private Long timestamp;
    private int status;
    private List<ValidationError> errors;
    private Map<String, ValidationError> fieldErrors;

    public void addError(ValidationError error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }

    public void addFieldError(String path, String message, String[] codes) {
        if (fieldErrors == null) {
            fieldErrors = new HashMap<>();
        }
        ValidationError error = new ValidationError(message, codes);
        fieldErrors.put(path, error);
    }

    public boolean hasErrors() {
    	if ((fieldErrors != null && fieldErrors.size() >0) || (errors != null && errors.size() > 0))
    		return true;
    	return false;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public Map<String, ValidationError> getFieldErrors() {
        return fieldErrors;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}