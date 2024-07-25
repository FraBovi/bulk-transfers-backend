package com.mycompany.bulk_transfer_application.exception;

public class CreditNotSufficientException extends Exception {

    public CreditNotSufficientException(String errorMsg) {
        super(errorMsg);
    }
    
}
