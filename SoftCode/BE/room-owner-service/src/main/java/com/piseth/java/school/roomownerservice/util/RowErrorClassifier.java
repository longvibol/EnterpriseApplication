package com.piseth.java.school.roomownerservice.util;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.piseth.java.school.roomownerservice.domain.enumeration.Outcome;
import com.piseth.java.school.roomownerservice.exception.ClassifiableError;

import jakarta.validation.ConstraintViolationException;

@Component
public class RowErrorClassifier {
    
    public String safeMessage(final Throwable ex) {
        if (ex == null) {
            return "Unknown error";
        }
        if (ex.getMessage() != null) {
            return ex.getMessage();
        }
        return ex.getClass().getSimpleName();
    }

    public Outcome classify(final Throwable ex) {

        if (ex == null) {
            return Outcome.OTHER;
        }

        // Your custom classifiable errors
        if (ex instanceof ClassifiableError ce) {
            return ce.getOutcome();
        }

        if (ex instanceof WebExchangeBindException) {
            return Outcome.VALIDATION;
        }

        if (ex instanceof ConstraintViolationException) {
            return Outcome.VALIDATION;
        }

        return Outcome.OTHER;
    }
}