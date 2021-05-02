package com.epam.esm.service.exception;

public class InvalidEntityException extends RuntimeException {
    public InvalidEntityException() {
    }

    public InvalidEntityException(String message) {
        super(message);
    }
}
