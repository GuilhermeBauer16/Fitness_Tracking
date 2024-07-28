package com.github.GuilhermeBauer16.FitnessTracking.service.contract;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface WorkoutExerciseServiceContract<T, I> {

    T create(T target);

    T update(T target);

    T findById(I id);

    Page<T> findAll(final Pageable pageable);

    void delete(I id);
}
