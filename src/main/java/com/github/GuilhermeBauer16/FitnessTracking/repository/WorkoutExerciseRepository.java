package com.github.GuilhermeBauer16.FitnessTracking.repository;

import com.github.GuilhermeBauer16.FitnessTracking.enums.DifficultyLevel;
import com.github.GuilhermeBauer16.FitnessTracking.enums.MuscleGroup;
import com.github.GuilhermeBauer16.FitnessTracking.model.WorkoutExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExerciseEntity, String> {

    @Query("SELECT w FROM WorkoutExerciseEntity w JOIN w.muscleGroups mg WHERE mg IN :muscleGroups")
    List<WorkoutExerciseEntity> findByMuscleGroups(
            @Param("muscleGroups") Set<MuscleGroup> muscleGroups);


    @Query("SELECT w FROM WorkoutExerciseEntity w WHERE w.difficultyLevel = :difficultyLevel")
    List<WorkoutExerciseEntity> findByDifficultLevel(
            @Param("difficultyLevel") DifficultyLevel difficultyLevel);
}
