package com.github.GuilhermeBauer16.FitnessTracking.exception;

import com.github.GuilhermeBauer16.FitnessTracking.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailAlreadyRegisterException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.EMAIL_ALREADY_REGISTER_MESSAGE;

    public EmailAlreadyRegisterException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
