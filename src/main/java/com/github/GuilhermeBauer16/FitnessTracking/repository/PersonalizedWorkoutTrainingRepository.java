package com.github.GuilhermeBauer16.FitnessTracking.repository;

import com.github.GuilhermeBauer16.FitnessTracking.enums.MuscleGroup;
import com.github.GuilhermeBauer16.FitnessTracking.model.PersonalizedWorkoutTrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PersonalizedWorkoutTrainingRepository extends JpaRepository<PersonalizedWorkoutTrainingEntity,String> {

    @Query("SELECT p FROM PersonalizedWorkoutTrainingEntity p JOIN p.workoutExerciseEntity w JOIN w.muscleGroups mg WHERE mg IN :muscleGroups")
    List<PersonalizedWorkoutTrainingEntity> findByMuscleGroup(@Param("muscleGroups") Set<MuscleGroup> muscleGroup);
}
