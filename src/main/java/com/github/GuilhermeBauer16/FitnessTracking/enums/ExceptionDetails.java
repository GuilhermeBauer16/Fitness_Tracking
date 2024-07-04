package com.github.GuilhermeBauer16.FitnessTracking.enums;

import org.springframework.http.HttpStatus;


public enum ExceptionDetails {

    INVALID_UUID_FORMAT_MESSAGE("The ID %s needs to be in a UUID format",HttpStatus.INTERNAL_SERVER_ERROR),
    WORKOUT_EXERCISE_NOT_FOUND_MESSAGE("Occur an error in workout exercise in reason of: %s",HttpStatus.NOT_FOUND),
    FIELD_NOT_FOUND_MESSAGE("The field %s can not be null or empty!",HttpStatus.NOT_FOUND),
    EXCEPTION_TYPE_NOT_THROWN("Can not thrown the exception of the type: %s", HttpStatus.NOT_FOUND);

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
