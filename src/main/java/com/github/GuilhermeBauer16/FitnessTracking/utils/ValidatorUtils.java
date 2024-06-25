package com.github.GuilhermeBauer16.FitnessTracking.utils;

public class ValidatorUtils {


    public static <T> void checkNotNullAndNotEmptyOrThrowException(T object){
        if(object == null){
            throw new RuntimeException("error");
        }

        if(object instanceof String && ((String) object).trim().isEmpty()){
            throw new RuntimeException("error!");
        }
    }
}
