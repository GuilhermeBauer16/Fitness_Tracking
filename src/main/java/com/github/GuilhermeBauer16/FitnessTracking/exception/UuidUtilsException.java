package com.github.GuilhermeBauer16.FitnessTracking.exception;

import com.github.GuilhermeBauer16.FitnessTracking.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class UuidUtilsException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.INVALID_UUID_FORMAT_MESSAGE;

    public UuidUtilsException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
