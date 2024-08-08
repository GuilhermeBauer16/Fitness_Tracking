package com.github.GuilhermeBauer16.FitnessTracking.exception;

import com.github.GuilhermeBauer16.FitnessTracking.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.USER_NOT_FOUND_MESSAGE;

    public UserNotFoundException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
