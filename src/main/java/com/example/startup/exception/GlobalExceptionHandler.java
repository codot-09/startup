package com.example.startup.exception;

import com.example.startup.payload.ApiResponse;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.security.sasl.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        logger.warn("Validation failed: {}", errors);
        return ResponseEntity.badRequest().body(ApiResponse.error("Validation failed", errors));
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<ApiResponse<?>> handleRestException(RestException ex) {
        logger.warn("RestException caught: Message='{}', Status={}, Details={}",
                ex.getMessage(), ex.getStatus(), ex.getDetails(), ex);

        return ResponseEntity.status(ex.getStatus())
                .body(ApiResponse.error(ex.getMessage(), ex.getDetails()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleAll(Exception ex) {
        logger.error("An unexpected internal server error occurred:", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error"));
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleAll(NotFoundException ex) {
        logger.error("Element not found", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Not Found"));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<?> handleException(AccessDeniedException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(
                ApiResponse.error("You not access for use this way"),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<?> handleException(org.springframework.security.core.AuthenticationException ex){
        ex.printStackTrace();
        return new ResponseEntity<>(
                ApiResponse.error("You don't authenticated"),
                HttpStatus.UNAUTHORIZED);
    }
}
