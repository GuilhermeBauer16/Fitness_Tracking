package com.github.GuilhermeBauer16.FitnessTracking.enums;

import org.springframework.http.HttpStatus;

public enum ExceptionDetails {

    INVALID_UUID_FORMAT_MESSAGE("The ID %s needs to be in a UUID format",HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;

    ExceptionDetails(String message, HttpStatus httpStatus){
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String formatErrorMessage(String message){
        return String.format(this.message,message);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
