package com.mycompany.bulk_transfer_application.exception;

public class BadRequestException extends Exception {

    public BadRequestException(String errorMsg) {
        super(errorMsg);
    }
    
}
