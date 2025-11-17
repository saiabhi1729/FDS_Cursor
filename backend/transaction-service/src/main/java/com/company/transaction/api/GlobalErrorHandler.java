package com.company.transaction.api;

import java.net.URI;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public ProblemDetail handleDuplicate(DuplicateKeyException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        detail.setType(URI.create("https://docs.company.com/errors/idempotency"));
        return detail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        detail.setType(URI.create("https://docs.company.com/errors/validation"));
        return detail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        detail.setType(URI.create("https://docs.company.com/errors/not-found"));
        return detail;
    }
}
