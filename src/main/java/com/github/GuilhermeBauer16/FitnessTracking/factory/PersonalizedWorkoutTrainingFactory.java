package com.github.GuilhermeBauer16.FitnessTracking.factory;

import com.github.GuilhermeBauer16.FitnessTracking.model.PersonalizedWorkoutTrainingEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.WorkoutExerciseEntity;
import com.github.GuilhermeBauer16.FitnessTracking.utils.UuidUtils;

/**
 * That factory create an instance of the class {@link  PersonalizedWorkoutTrainingEntity}
 * <p>
 * That factory hide the business rules to create a Personalized Workout Training,
 * include the creation of a unique UUID for each Personalized Workout Training
 * </p>
 */


public class PersonalizedWorkoutTrainingFactory {

    public PersonalizedWorkoutTrainingFactory() {
    }


    public static PersonalizedWorkoutTrainingEntity create(WorkoutExerciseEntity workoutExerciseEntity, Integer repetitions,
                                                           Integer series, Double weight) {
        return new PersonalizedWorkoutTrainingEntity(UuidUtils.generateUuid(), workoutExerciseEntity, repetitions, series, weight);
    }


}
