package com.github.GuilhermeBauer16.FitnessTracking.exception;

import com.github.GuilhermeBauer16.FitnessTracking.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FieldNotFound extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.FIELD_NOT_FOUND_MESSAGE;

    public FieldNotFound(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
