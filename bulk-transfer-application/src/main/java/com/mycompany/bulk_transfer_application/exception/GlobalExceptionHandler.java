package com.mycompany.bulk_transfer_application.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mycompany.bulk_transfer_application.BulkConstants;

// Provides handling for exceptions throughout this service.
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception e) {

        logger.error("Exception exception ", e);
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e));
    
    }

    @ExceptionHandler(NoBankAccountFoundException.class)
    public final ResponseEntity<Object> handleException(NoBankAccountFoundException e) {

        logger.error("NoBankAccountFoundException exception ", e);
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, BulkConstants.BANK_ACCOUNT_NOT_FOUND, e));
    
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<Object> handleException(MethodArgumentNotValidException e) {

        logger.error("BadRequestException exception ", e);

        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, badRequestMsgBuilder(e), e));
    
    }

    @ExceptionHandler(CreditNotSufficientException.class)
    public final ResponseEntity<Object> handleException(CreditNotSufficientException e) {

        logger.error("CreditNotSufficientException exception ", e);
        return buildResponseEntity(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, BulkConstants.CREDIT_NOT_SUFFICIENT, e));
    
    }

    @ExceptionHandler(AmountFormatException.class)
    public final ResponseEntity<Object> handleException(AmountFormatException e) {

        logger.error("AmountFormatException exception ", e);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, BulkConstants.AMOUNT_FORMAT_EXCEPTION, e));
    
    }

    private String badRequestMsgBuilder(MethodArgumentNotValidException e) {

        StringBuilder errorMsg = new StringBuilder("Error in fields: ");
        
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMsgField = error.getDefaultMessage();
            errors.put(fieldName, errorMsgField);
        });
        errorMsg.append(errors.toString());

        return errorMsg.toString();

    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getCode());
    }
}
