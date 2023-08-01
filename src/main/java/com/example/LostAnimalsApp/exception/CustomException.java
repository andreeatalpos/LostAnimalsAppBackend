package com.example.LostAnimalsApp.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class CustomException extends RuntimeException {
    private final String resource;
    private final HttpStatus status ;
    private final List<String> validationErrors;

    public CustomException(String message, HttpStatus status, String resource, List<String> errors) {
        super(message);
        this.resource = resource;
        this.validationErrors = errors;
        this.status = status;
    }


}
