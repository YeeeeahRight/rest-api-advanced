package com.epam.esm.service.exception;

public class InvalidParametersException extends RuntimeException {

    public InvalidParametersException() {
    }

    public InvalidParametersException(String message) {
        super(message);
    }
}
