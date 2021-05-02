package com.epam.esm.web.exception;

public class ExceptionResponse {
    private final String errorMessage;
    private final int errorCode;


    public ExceptionResponse(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
