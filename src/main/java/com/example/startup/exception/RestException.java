package com.example.startup.exception;

import org.springframework.http.HttpStatus;

public class RestException extends RuntimeException {

    private final HttpStatus status;
    private final String message;
    private final Object details;

    public RestException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
        this.details = null;
    }

    public RestException(String message, HttpStatus status, Object details) {
        super(message);
        this.message = message;
        this.status = status;
        this.details = details;
    }

    public RestException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.message = message;
        this.status = status;
        this.details = null;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object getDetails() {
        return details;
    }
}