package com.github.GuilhermeBauer16.FitnessTracking.exception;

import com.github.GuilhermeBauer16.FitnessTracking.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonalizedWorkoutTrainingNotFound extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE;

    public PersonalizedWorkoutTrainingNotFound(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
