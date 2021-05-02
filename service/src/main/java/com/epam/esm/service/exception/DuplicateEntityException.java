package com.epam.esm.service.exception;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException() {
    }

    public DuplicateEntityException(String message) {
        super(message);
    }
}
