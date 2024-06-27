package com.github.GuilhermeBauer16.FitnessTracking.handler;

import com.github.GuilhermeBauer16.FitnessTracking.exception.ExceptionResponse;
import com.github.GuilhermeBauer16.FitnessTracking.exception.UuidUtilsException;
import com.github.GuilhermeBauer16.FitnessTracking.exception.WorkoutExerciseNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler {

    @ExceptionHandler({WorkoutExerciseNotFound.class})
    public final ResponseEntity<ExceptionResponse> handlerNotFoundException(
            Exception ex,
            WebRequest webRequest
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(),
                webRequest.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(exceptionResponse,HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({UuidUtilsException.class})
    public final ResponseEntity<ExceptionResponse> handlerInternalServerErrorException(
            Exception ex,
            WebRequest webRequest
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(),
                webRequest.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(exceptionResponse,HttpStatus.INTERNAL_SERVER_ERROR);

    }
//
//    @ExceptionHandler(InvalidJwtAuthenticationException.class)
//    public final ResponseEntity<ExceptionResponse> handleInvalidJwtAuthenticationExceptions(
//            Exception ex,
//            WebRequest request
//    ) {
//        ExceptionResponse exceptionResponse = new ExceptionResponse(
//                ex.getMessage(),
//                request.getDescription(false),
//                new Date()
//        );
//
//        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
//
//    }
}
