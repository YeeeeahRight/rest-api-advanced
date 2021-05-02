package com.epam.esm.service.exception;

public class NoSuchEntityException extends RuntimeException {
    public NoSuchEntityException() {
    }

    public NoSuchEntityException(String message) {
        super(message);
    }
}
