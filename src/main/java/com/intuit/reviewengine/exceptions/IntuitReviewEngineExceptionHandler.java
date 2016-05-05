package com.intuit.reviewengine.exceptions;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class IntuitReviewEngineExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    

    @ExceptionHandler(IntuitReviewEngineException.class)
    public ResponseEntity<ValidationErrors> processError(IntuitReviewEngineException ex) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.addError(resolveLocalizedErrorMessage(ex));
        validationErrors.setTimestamp(System.currentTimeMillis());
        validationErrors.setStatus(ex.getHttpStatus().value());
        ResponseEntity<ValidationErrors> response = new ResponseEntity<ValidationErrors>(validationErrors, ex.getHttpStatus());
        return response;
    }

    private ValidationError resolveLocalizedErrorMessage(IntuitReviewEngineException ex) {
        String messageCode = ex.getMessageCode();
        String[] args = ex.getArgs();

        if (messageCode == null) {
            messageCode = "http.status." + ex.getHttpStatus();
        }

        Locale currentLocale =  LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(messageCode, args, messageCode, currentLocale);

        ValidationError validationError = new ValidationError(localizedErrorMessage, new String[] {messageCode});
        return validationError;
    }

}