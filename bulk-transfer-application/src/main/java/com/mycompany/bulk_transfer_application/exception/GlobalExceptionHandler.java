package com.mycompany.bulk_transfer_application.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mycompany.bulk_transfer_application.pojo.Response;

// Provides handling for exceptions throughout this service.
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Response> handleException(Exception e) {

        logger.error("Exception exception ", e);

        Response response = new Response();
        response.setCode(-99);
        response.setDescription(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoBankAccountFoundException.class)
    public final ResponseEntity<Response> handleException(NoBankAccountFoundException e) {

        logger.error("NoBankAccountFoundException exception ", e);

        Response response = new Response();
        response.setCode(404);
        response.setDescription("No items found in the DB that matches the query");

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BadRequestException.class, MethodArgumentNotValidException.class})
    public final ResponseEntity<Response> handleException(MethodArgumentNotValidException e) {

        logger.error("BadRequestException exception ", e);

        Response response = new Response();
        response.setCode(400);
        response.setDescription("Bad Request: elements missing or type not correct");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
}
