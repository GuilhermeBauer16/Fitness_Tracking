package com.github.GuilhermeBauer16.FitnessTracking.repository;

import com.github.GuilhermeBauer16.FitnessTracking.enums.DifficultyLevel;
import com.github.GuilhermeBauer16.FitnessTracking.enums.ExerciseType;
import com.github.GuilhermeBauer16.FitnessTracking.enums.MuscleGroup;
import com.github.GuilhermeBauer16.FitnessTracking.model.PersonalizedWorkoutTrainingEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.WorkoutExerciseEntity;
import integrationtests.testContainers.AbstractionIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonalizedWorkoutTrainingRepositoryTest extends AbstractionIntegrationTest {

    @Autowired
    private PersonalizedWorkoutTrainingRepository repository;

    @Autowired
            private WorkoutExerciseRepository workoutExerciseRepository;

    PersonalizedWorkoutTrainingEntity personalizedWorkoutTrainingEntity;
    WorkoutExerciseEntity workoutExerciseEntity;

    PersonalizedWorkoutTrainingEntity savedPersonalizedWorkoutEntity;

    private final String ID = "d8e7df81-2cd4-41a2-a005-62e6d8079716";

    private final Integer REPETITIONS = 10;
    private final Integer SERIES = 4;
    private final Double WEIGHT = 40d;

    private final String NAME = "push-up";
    private final String DESCRIPTION = "A push-up is a common calisthenics exercise beginning from the prone position.";
    private final int CALORIES_BURNED = 50;
    private final ExerciseType EXERCISE_TYPE = ExerciseType.STRENGTH;
    private final String EQUIPMENT_NEEDED = "None";
    private final DifficultyLevel DIFFICULTY_LEVEL = DifficultyLevel.BEGINNER;
    private final Set<MuscleGroup> MUSCLE_GROUPS = new HashSet<>(Arrays.asList(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS));


    @BeforeEach
    void setUp(){

        workoutExerciseEntity = new WorkoutExerciseEntity(ID, NAME, DESCRIPTION, CALORIES_BURNED, EXERCISE_TYPE, EQUIPMENT_NEEDED, DIFFICULTY_LEVEL, MUSCLE_GROUPS);
        personalizedWorkoutTrainingEntity = new PersonalizedWorkoutTrainingEntity(ID, workoutExerciseEntity, REPETITIONS, SERIES, WEIGHT);
        WorkoutExerciseEntity savedWorkoutExercise = workoutExerciseRepository.save(workoutExerciseEntity);
        personalizedWorkoutTrainingEntity.setWorkoutExerciseEntity(savedWorkoutExercise);
        savedPersonalizedWorkoutEntity = repository.save(personalizedWorkoutTrainingEntity);
    }

    @Test
    void testGivenMuscleGroup_whenFindByMuscleGroup_ThenReturnListOfPersonalizedWorkoutTrainingEntity(){


        List<PersonalizedWorkoutTrainingEntity> byMuscleGroup =
                repository.findByMuscleGroup(savedPersonalizedWorkoutEntity.getWorkoutExerciseEntity().getMuscleGroups());

        assertEquals(1, byMuscleGroup.size());

        PersonalizedWorkoutTrainingEntity muscleGroup = byMuscleGroup.getFirst();
        assertNotNull(muscleGroup);
        assertNotNull(muscleGroup.getWorkoutExerciseEntity());

        assertEquals(REPETITIONS, muscleGroup.getRepetitions());
        assertEquals(WEIGHT, muscleGroup.getWeight());
        assertEquals(SERIES, muscleGroup.getSeries());

        assertEquals(NAME, muscleGroup.getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, muscleGroup.getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, muscleGroup.getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, muscleGroup.getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, muscleGroup.getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, muscleGroup.getWorkoutExerciseEntity().getMuscleGroups());

    }

    @Test
    void testGivenFindByName_whenFindByName_ThenReturnListOfPersonalizedWorkoutTrainingEntity(){


        List<PersonalizedWorkoutTrainingEntity> byName =
                repository.findByName(savedPersonalizedWorkoutEntity.getWorkoutExerciseEntity().getName());


        assertEquals(1, byName.size());

        PersonalizedWorkoutTrainingEntity muscleGroup = byName.getFirst();
        assertNotNull(muscleGroup);
        assertNotNull(muscleGroup.getWorkoutExerciseEntity());

        assertEquals(REPETITIONS, muscleGroup.getRepetitions());
        assertEquals(WEIGHT, muscleGroup.getWeight());
        assertEquals(SERIES, muscleGroup.getSeries());

        assertEquals(NAME, muscleGroup.getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, muscleGroup.getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, muscleGroup.getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, muscleGroup.getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, muscleGroup.getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, muscleGroup.getWorkoutExerciseEntity().getMuscleGroups());

    }

    @Test
    void testGivenFindByDifficultyLevel_whenFindByDifficultyLevel_ThenReturnListOfPersonalizedWorkoutTrainingEntity(){


        List<PersonalizedWorkoutTrainingEntity> byDifficultyLevel =
                repository.findByDifficultyLevel(savedPersonalizedWorkoutEntity.getWorkoutExerciseEntity().getDifficultyLevel());


        assertEquals(1, byDifficultyLevel.size());

        PersonalizedWorkoutTrainingEntity muscleGroup = byDifficultyLevel.getFirst();
        assertNotNull(muscleGroup);
        assertNotNull(muscleGroup.getWorkoutExerciseEntity());

        assertEquals(REPETITIONS, muscleGroup.getRepetitions());
        assertEquals(WEIGHT, muscleGroup.getWeight());
        assertEquals(SERIES, muscleGroup.getSeries());

        assertEquals(NAME, muscleGroup.getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, muscleGroup.getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, muscleGroup.getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, muscleGroup.getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, muscleGroup.getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, muscleGroup.getWorkoutExerciseEntity().getMuscleGroups());

    }


}
