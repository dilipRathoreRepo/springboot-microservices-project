package com.diliprathore.employeeservice.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    private String message;

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
