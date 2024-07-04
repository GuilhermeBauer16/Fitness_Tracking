package com.github.GuilhermeBauer16.FitnessTracking.repository;

import com.github.GuilhermeBauer16.FitnessTracking.model.WorkoutExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExerciseEntity, String> {
}
