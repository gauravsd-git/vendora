package com.gaurav.vendora.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Custom Business Errors
    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> handleUserException(UserException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Validation Errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return buildResponse(message, HttpStatus.BAD_REQUEST);
    }

    // Constraint Errors
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintException(ConstraintViolationException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Forbidden Access
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return buildResponse("Access Denied", HttpStatus.FORBIDDEN);
    }

    // Not Found
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {

        if (ex.getMessage() != null &&
                ex.getMessage().toLowerCase().contains("not found")) {
            return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
        }

        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Any Unknown Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        return buildResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Common Response Builder
    private ResponseEntity<Map<String, Object>> buildResponse(
            String message,
            HttpStatus status
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, status);
    }
}