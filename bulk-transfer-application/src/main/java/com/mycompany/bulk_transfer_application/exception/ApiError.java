package com.mycompany.bulk_transfer_application.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiError {

    private HttpStatus code;
    private String message;

    public ApiError() {
    }

    public ApiError(HttpStatus status) {
        this();
        this.code = status;
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this();
        this.code = status;
        this.message = "Unexpected error";
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.code = status;
        this.message = message;
    }
    
}
