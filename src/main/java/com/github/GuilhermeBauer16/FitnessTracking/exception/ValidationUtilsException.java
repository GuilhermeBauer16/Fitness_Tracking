package com.github.GuilhermeBauer16.FitnessTracking.exception;

import com.github.GuilhermeBauer16.FitnessTracking.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ValidationUtilsException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.EXCEPTION_TYPE_NOT_THROWN;

    public ValidationUtilsException(String message, Throwable cause) {
        super(ERROR.formatErrorMessage(message), cause);
    }
}
