package com.github.GuilhermeBauer16.FitnessTracking.exception;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public record ExceptionResponse(String message, String details, Date timeStamp) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

}
