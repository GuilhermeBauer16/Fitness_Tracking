package com.github.GuilhermeBauer16.FitnessTracking.repository;

import com.github.GuilhermeBauer16.FitnessTracking.model.PersonalizedWorkoutTrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalizedWorkoutTrainingRepository extends JpaRepository<PersonalizedWorkoutTrainingEntity,String> {
}
