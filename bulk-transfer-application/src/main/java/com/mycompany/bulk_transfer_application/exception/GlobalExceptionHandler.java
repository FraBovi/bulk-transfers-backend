package com.mycompany.bulk_transfer_application.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// Provides handling for exceptions throughout this service.
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception e) {

        logger.error("Exception exception ", e);
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e));
    
    }

    @ExceptionHandler(NoBankAccountFoundException.class)
    public final ResponseEntity<Object> handleException(NoBankAccountFoundException e) {

        logger.error("NoBankAccountFoundException exception ", e);
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, "No items found in the DB that matches the query", e));
    
    }

    @ExceptionHandler(value = {BadRequestException.class, MethodArgumentNotValidException.class})
    public final ResponseEntity<Object> handleException(MethodArgumentNotValidException e) {

        logger.error("BadRequestException exception ", e);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), e));
    
    }

    @ExceptionHandler(value = {CreditNotSufficientException.class})
    public final ResponseEntity<Object> handleException(CreditNotSufficientException e) {

        logger.error("CreditNotSufficientException exception ", e);
        return buildResponseEntity(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e));
    
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
