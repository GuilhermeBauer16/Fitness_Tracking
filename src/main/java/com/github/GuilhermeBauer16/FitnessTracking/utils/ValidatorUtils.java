package com.github.GuilhermeBauer16.FitnessTracking.utils;

import com.github.GuilhermeBauer16.FitnessTracking.exception.ValidationUtilsException;

import java.lang.reflect.Constructor;

public class ValidatorUtils {


    public static <T> void checkNotNullAndNotEmptyOrThrowException(T object, String errorMessage, Class<? extends RuntimeException> customException ){
        if(object == null){
            throwException(errorMessage,customException);
        }

        if(object instanceof String && ((String) object).trim().isEmpty()){
            throwException(errorMessage,customException);
        }
    }

    public static void throwException(String message, Class<? extends RuntimeException> runtimeExceptionClass){
        try {
            Constructor<? extends RuntimeException> exceptionConstruction = runtimeExceptionClass.getConstructor(String.class);
            throw exceptionConstruction.newInstance(message);
        } catch (ReflectiveOperationException e) {

            throw new ValidationUtilsException(runtimeExceptionClass.getSimpleName(),e);


        }
    }
}
