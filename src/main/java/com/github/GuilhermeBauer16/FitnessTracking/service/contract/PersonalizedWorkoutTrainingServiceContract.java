package com.github.GuilhermeBauer16.FitnessTracking.service.contract;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PersonalizedWorkoutTrainingServiceContract<T, I> {

    T create(T target);

    T update(T target);

    T findById(I id);

    List<T> workoutExercisesByMuscleGroup(T t);

    List<T> workoutExercisesByDifficultLevel(T t);

    Page<T> findAll(final Pageable pageable);

    void delete(I id);
}
