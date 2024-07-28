package com.github.GuilhermeBauer16.FitnessTracking.service.contract;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface PersonalizedWorkoutTrainingServiceContract<T, I> {

    T create(T target);

    T update(T target);

    T findById(I id);

    List<T> findPersonalizedWorkoutTrainingByMuscleGroup(T t);

    List<T> findPersonalizedWorkoutTrainingByDifficultLevel(T t);

    List<T> findPersonalizedWorkoutTrainingByName(T t);

    Page<T> findAll(final Pageable pageable);

    void delete(I id);
}
