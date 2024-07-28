package com.github.GuilhermeBauer16.FitnessTracking.service;

import com.github.GuilhermeBauer16.FitnessTracking.enums.DifficultyLevel;
import com.github.GuilhermeBauer16.FitnessTracking.enums.ExerciseType;
import com.github.GuilhermeBauer16.FitnessTracking.enums.MuscleGroup;
import com.github.GuilhermeBauer16.FitnessTracking.exception.PersonalizedWorkoutTrainingNotFound;
import com.github.GuilhermeBauer16.FitnessTracking.exception.UuidUtilsException;
import com.github.GuilhermeBauer16.FitnessTracking.model.PersonalizedWorkoutTrainingEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.WorkoutExerciseEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.PersonalizedWorkoutTrainingVO;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.WorkoutExerciseVO;
import com.github.GuilhermeBauer16.FitnessTracking.repository.PersonalizedWorkoutTrainingRepository;
import com.github.GuilhermeBauer16.FitnessTracking.utils.UuidUtils;
import com.github.GuilhermeBauer16.FitnessTracking.utils.ValidatorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PersonalizedWorkoutExerciseTest {

    private static final String PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE =
            "Can not be find any Workout exercise into your Personalized workout training!";

    public final String UUID_REQUIRED = "An UUID need to be informed!";

    @Mock
    private PersonalizedWorkoutTrainingRepository repository;


    @Mock
    private WorkoutExerciseService workoutExerciseService;

    @Mock
    private WorkoutExerciseVO workoutExerciseVO;

    @Mock
    WorkoutExerciseEntity workoutExerciseEntity;

    @Mock
    private PersonalizedWorkoutTrainingEntity personalizedWorkoutTrainingEntity;

    @Mock
    private PersonalizedWorkoutTrainingVO personalizedWorkoutTrainingVO;

    @InjectMocks
    private PersonalizedWorkoutTrainingService service;


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
    void setUp() {

        workoutExerciseEntity = new WorkoutExerciseEntity(ID, NAME, DESCRIPTION, CALORIES_BURNED, EXERCISE_TYPE, EQUIPMENT_NEEDED, DIFFICULTY_LEVEL, MUSCLE_GROUPS);
        workoutExerciseVO = new WorkoutExerciseVO(ID, NAME, DESCRIPTION, CALORIES_BURNED, EXERCISE_TYPE, EQUIPMENT_NEEDED, DIFFICULTY_LEVEL, MUSCLE_GROUPS);
        personalizedWorkoutTrainingEntity = new PersonalizedWorkoutTrainingEntity(ID, workoutExerciseEntity, REPETITIONS, SERIES, WEIGHT);
        personalizedWorkoutTrainingVO = new PersonalizedWorkoutTrainingVO(ID, workoutExerciseEntity, REPETITIONS, SERIES, WEIGHT);


    }

    @Test
    void testCreatePersonalizeWorkoutExercise_WhenSaveWorkoutExercise_ShouldReturnPersonalizeWorkoutExerciseObject() {

        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {
            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any(Class.class))).thenAnswer(invocation -> null);
            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any(Class.class))).thenAnswer(invocation -> null);

            when(workoutExerciseService.findById(ID)).thenReturn(workoutExerciseVO);
            when(repository.save(any(PersonalizedWorkoutTrainingEntity.class))).thenReturn(personalizedWorkoutTrainingEntity);

            PersonalizedWorkoutTrainingVO createdPersonalizedWorkoutTrainingVO = service.create(personalizedWorkoutTrainingVO);

            mockedValidatorUtils.verify(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any(Class.class)));
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any(Class.class)));
            verify(repository, times(1)).save(any(PersonalizedWorkoutTrainingEntity.class));
            verify(workoutExerciseService, times(1)).findById(anyString());

            assertNotNull(createdPersonalizedWorkoutTrainingVO);
            assertNotNull(createdPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity());

            assertEquals(REPETITIONS, createdPersonalizedWorkoutTrainingVO.getRepetitions());
            assertEquals(WEIGHT, createdPersonalizedWorkoutTrainingVO.getWeight());
            assertEquals(SERIES, createdPersonalizedWorkoutTrainingVO.getSeries());

            assertEquals(NAME, createdPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getName());
            assertEquals(DESCRIPTION, createdPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDescription());
            assertEquals(CALORIES_BURNED, createdPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getCaloriesBurned());
            assertEquals(EXERCISE_TYPE, createdPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getExerciseType());
            assertEquals(DIFFICULTY_LEVEL, createdPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDifficultyLevel());
            assertEquals(MUSCLE_GROUPS, createdPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getMuscleGroups());
        }
    }

    @Test
    void testCreatePersonalizeWorkoutExercise_WhenPersonalizedWorkoutExerciseIsNull_ShouldThrowPersonalizedWorkoutTrainingNotFoundException() {

        PersonalizedWorkoutTrainingNotFound exception = assertThrows(PersonalizedWorkoutTrainingNotFound.class, () ->
                service.create(null));

        assertNotNull(exception);
        assertEquals(PersonalizedWorkoutTrainingNotFound.ERROR.formatErrorMessage(PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE)
                , exception.getMessage());


    }


    @Test
    void testUpdatePersonalizedWorkoutExercise_WhenUpdatedPersonalizedWorkoutExercise_ShouldReturnPersonalizedWorkoutExerciseObject() {
        try (MockedStatic<UuidUtils> mockedUuidUtils = Mockito.mockStatic(UuidUtils.class);
             MockedStatic<ValidatorUtils> mockedValidatorUtils = Mockito.mockStatic(ValidatorUtils.class)) {

            personalizedWorkoutTrainingEntity.setWeight(90D);
            personalizedWorkoutTrainingEntity.setRepetitions(20);

            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any(Class.class)))
                    .thenAnswer(invocation -> null);
            mockedUuidUtils.when(() -> UuidUtils.isValidUuid(personalizedWorkoutTrainingVO.getId())).thenReturn(true);
            mockedValidatorUtils.when(() -> ValidatorUtils.updateFieldIfNotNull(any(PersonalizedWorkoutTrainingEntity.class),
                            any(PersonalizedWorkoutTrainingVO.class), anyString(), any()))
                    .thenAnswer(invocation -> personalizedWorkoutTrainingEntity);
            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()))
                    .thenAnswer(invocation -> null);

            when(repository.findById(anyString())).thenReturn(Optional.of(personalizedWorkoutTrainingEntity));
            when(repository.save(any(PersonalizedWorkoutTrainingEntity.class))).thenReturn(personalizedWorkoutTrainingEntity);

            PersonalizedWorkoutTrainingVO updatedPersonalizedWorkoutTrainingVO = service.update(personalizedWorkoutTrainingVO);

            verify(repository, times(1)).save(any());
            verify(repository, times(1)).findById(anyString());

            mockedValidatorUtils.verify(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any(Class.class)));
            mockedUuidUtils.verify(() -> UuidUtils.isValidUuid(anyString()));
            mockedValidatorUtils.verify(() -> ValidatorUtils.updateFieldIfNotNull(any(), any(), anyString(), any()));
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()));

            assertNotNull(updatedPersonalizedWorkoutTrainingVO);
            assertNotNull(updatedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity());
            assertEquals(20, updatedPersonalizedWorkoutTrainingVO.getRepetitions());
            assertEquals(90D, updatedPersonalizedWorkoutTrainingVO.getWeight());
            assertEquals(SERIES, updatedPersonalizedWorkoutTrainingVO.getSeries());
            assertEquals(NAME, updatedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getName());
            assertEquals(DESCRIPTION, updatedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDescription());
            assertEquals(CALORIES_BURNED, updatedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getCaloriesBurned());
            assertEquals(EXERCISE_TYPE, updatedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getExerciseType());
            assertEquals(DIFFICULTY_LEVEL, updatedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDifficultyLevel());
            assertEquals(MUSCLE_GROUPS, updatedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getMuscleGroups());
        }
    }

    @Test
    void testUpdatePersonalizeWorkoutExercise_WhenPersonalizedWorkoutExerciseIsNull_ShouldThrowPersonalizedWorkoutTrainingNotFoundException() {

        PersonalizedWorkoutTrainingNotFound exception = assertThrows(PersonalizedWorkoutTrainingNotFound.class, () ->
                service.update(null));

        assertNotNull(exception);
        assertEquals(PersonalizedWorkoutTrainingNotFound.ERROR.formatErrorMessage(PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE)
                , exception.getMessage());


    }

    @Test
    void testUpdatePersonalizeWorkoutExercise_WhenPersonalizedWorkoutExerciseNotFound_ShouldThrowPersonalizedWorkoutTrainingNotFoundException() {

        PersonalizedWorkoutTrainingNotFound exception = assertThrows(PersonalizedWorkoutTrainingNotFound.class, () ->
                service.update(personalizedWorkoutTrainingVO));

        assertNotNull(exception);
        assertEquals(PersonalizedWorkoutTrainingNotFound.ERROR.formatErrorMessage(PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE)
                , exception.getMessage());


    }

    @Test
    void testPersonalizedWorkoutExercises_WhenFindPersonalizedWorkoutExerciseById_ShouldReturnPersonalizedWorkoutExerciseObject() {

        try (MockedStatic<UuidUtils> uuidUtilsMockedStatic = Mockito.mockStatic(UuidUtils.class)) {

            uuidUtilsMockedStatic.when(() -> UuidUtils.isValidUuid(ID)).thenReturn(true);

            when(repository.findById(ID)).thenReturn(Optional.of(personalizedWorkoutTrainingEntity));

            PersonalizedWorkoutTrainingVO foundedPersonalizedWorkoutTrainingVO = service.findById(ID);

            uuidUtilsMockedStatic.verify(() -> UuidUtils.isValidUuid(anyString()));
            verify(repository, times(1)).findById(anyString());

            assertNotNull(foundedPersonalizedWorkoutTrainingVO);
            assertNotNull(foundedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity());

            assertEquals(REPETITIONS, foundedPersonalizedWorkoutTrainingVO.getRepetitions());
            assertEquals(WEIGHT, foundedPersonalizedWorkoutTrainingVO.getWeight());
            assertEquals(SERIES, foundedPersonalizedWorkoutTrainingVO.getSeries());

            assertEquals(NAME, foundedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getName());
            assertEquals(DESCRIPTION, foundedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDescription());
            assertEquals(CALORIES_BURNED, foundedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getCaloriesBurned());
            assertEquals(EXERCISE_TYPE, foundedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getExerciseType());
            assertEquals(DIFFICULTY_LEVEL, foundedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDifficultyLevel());
            assertEquals(MUSCLE_GROUPS, foundedPersonalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getMuscleGroups());


        }
    }

    @Test
    void testFindPersonalizeWorkoutExerciseById_WhenPersonalizedWorkoutExerciseIdIsNull_ShouldThrowUuidUtilsException() {

        UuidUtilsException exception = assertThrows(UuidUtilsException.class, () ->
                service.findById(null));

        assertNotNull(exception);
        assertEquals(UuidUtilsException.ERROR.formatErrorMessage(UUID_REQUIRED)
                , exception.getMessage());


    }

    @Test
    void testFindPersonalizeWorkoutExerciseById_WhenPersonalizedWorkoutExerciseIdIsNotRegisterIntoDatabase_ShouldThrowPersonalizedWorkoutTrainingNotFoundException() {
        {
            PersonalizedWorkoutTrainingNotFound exception = assertThrows(PersonalizedWorkoutTrainingNotFound.class, () ->
                    service.findById(ID));

            assertNotNull(exception);
            assertEquals(PersonalizedWorkoutTrainingNotFound.ERROR.formatErrorMessage(PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE)
                    , exception.getMessage());

        }


    }

    @Test
    void testPersonalizedWorkoutExercises_WhenFindPersonalizedWorkoutExerciseByMuscleGroup_ShouldReturnPersonalizedWorkoutExerciseObject() {

        List<PersonalizedWorkoutTrainingEntity> personalizedWorkoutTrainingEntities = List.of(personalizedWorkoutTrainingEntity);

        when(repository.findByMuscleGroup(personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getMuscleGroups())
        ).thenReturn(personalizedWorkoutTrainingEntities);

        List<PersonalizedWorkoutTrainingVO> foundedPersonalizedWorkoutTrainingVO = service.findPersonalizedWorkoutTrainingByMuscleGroup(personalizedWorkoutTrainingVO);

        verify(repository, times(1)).findByMuscleGroup(any());

        assertNotNull(foundedPersonalizedWorkoutTrainingVO);
        assertNotNull(foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity());

        assertEquals(1, foundedPersonalizedWorkoutTrainingVO.size());
        assertEquals(REPETITIONS, foundedPersonalizedWorkoutTrainingVO.getFirst().getRepetitions());
        assertEquals(WEIGHT, foundedPersonalizedWorkoutTrainingVO.getFirst().getWeight());
        assertEquals(SERIES, foundedPersonalizedWorkoutTrainingVO.getFirst().getSeries());

        assertEquals(NAME, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getMuscleGroups());

    }

    @Test
    void testPersonalizedWorkoutExercises_WhenFindPersonalizedWorkoutExerciseByDifficultLevel_ShouldReturnPersonalizedWorkoutExerciseObject() {

        List<PersonalizedWorkoutTrainingEntity> personalizedWorkoutTrainingEntities = List.of(personalizedWorkoutTrainingEntity);

        when(repository.findByDifficultyLevel(personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDifficultyLevel())
        ).thenReturn(personalizedWorkoutTrainingEntities);

        List<PersonalizedWorkoutTrainingVO> foundedPersonalizedWorkoutTrainingVO =
                service.findPersonalizedWorkoutTrainingByDifficultLevel(personalizedWorkoutTrainingVO);

        verify(repository, times(1)).findByDifficultyLevel(any());

        assertNotNull(foundedPersonalizedWorkoutTrainingVO);
        assertNotNull(foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity());

        assertEquals(1, foundedPersonalizedWorkoutTrainingVO.size());
        assertEquals(REPETITIONS, foundedPersonalizedWorkoutTrainingVO.getFirst().getRepetitions());
        assertEquals(WEIGHT, foundedPersonalizedWorkoutTrainingVO.getFirst().getWeight());
        assertEquals(SERIES, foundedPersonalizedWorkoutTrainingVO.getFirst().getSeries());

        assertEquals(NAME, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getMuscleGroups());

    }

    @Test
    void testPersonalizedWorkoutExercises_WhenFindPersonalizedWorkoutExerciseByName_ShouldReturnPersonalizedWorkoutExerciseObject() {

        List<PersonalizedWorkoutTrainingEntity> personalizedWorkoutTrainingEntities = List.of(personalizedWorkoutTrainingEntity);

        when(repository.findByName(anyString())
        ).thenReturn(personalizedWorkoutTrainingEntities);

        List<PersonalizedWorkoutTrainingVO> foundedPersonalizedWorkoutTrainingVO =
                service.findPersonalizedWorkoutTrainingByName(personalizedWorkoutTrainingVO);

        verify(repository, times(1)).findByName(any());

        assertNotNull(foundedPersonalizedWorkoutTrainingVO);
        assertNotNull(foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity());

        assertEquals(1, foundedPersonalizedWorkoutTrainingVO.size());
        assertEquals(REPETITIONS, foundedPersonalizedWorkoutTrainingVO.getFirst().getRepetitions());
        assertEquals(WEIGHT, foundedPersonalizedWorkoutTrainingVO.getFirst().getWeight());
        assertEquals(SERIES, foundedPersonalizedWorkoutTrainingVO.getFirst().getSeries());

        assertEquals(NAME, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, foundedPersonalizedWorkoutTrainingVO.getFirst().getWorkoutExerciseEntity().getMuscleGroups());


    }

    @Test
    void testPersonalizedWorkoutExercise_When_FindAll_ShouldReturnPersonalizedWorkoutExerciseList() {

        List<PersonalizedWorkoutTrainingEntity> personalizedWorkoutTrainingEntities = List.of(personalizedWorkoutTrainingEntity);

        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(personalizedWorkoutTrainingEntities));
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<PersonalizedWorkoutTrainingVO> foundedListOfPersonalizedWorkoutTrainingVO = service.findAll(pageRequest);

        verify(repository, times(1)).findAll(any(Pageable.class));

        PersonalizedWorkoutTrainingVO personalizedWorkoutTraining = foundedListOfPersonalizedWorkoutTrainingVO.getContent().getFirst();

        assertEquals(1, foundedListOfPersonalizedWorkoutTrainingVO.getTotalElements());

        assertNotNull(personalizedWorkoutTraining);
        assertNotNull(personalizedWorkoutTraining.getWorkoutExerciseEntity());


        assertEquals(REPETITIONS, personalizedWorkoutTraining.getRepetitions());
        assertEquals(WEIGHT, personalizedWorkoutTraining.getWeight());
        assertEquals(SERIES, personalizedWorkoutTraining.getSeries());

        assertEquals(NAME, personalizedWorkoutTraining.getWorkoutExerciseEntity().getName());
        assertEquals(DESCRIPTION, personalizedWorkoutTraining.getWorkoutExerciseEntity().getDescription());
        assertEquals(CALORIES_BURNED, personalizedWorkoutTraining.getWorkoutExerciseEntity().getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, personalizedWorkoutTraining.getWorkoutExerciseEntity().getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, personalizedWorkoutTraining.getWorkoutExerciseEntity().getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, personalizedWorkoutTraining.getWorkoutExerciseEntity().getMuscleGroups());

    }

    @Test
    void testDelete_WhenDeletingPersonalizedWorkoutExercise_thenDoNothing() {

        when(repository.findById(ID)).thenReturn(Optional.of(personalizedWorkoutTrainingEntity));
        doNothing().when(repository).delete(personalizedWorkoutTrainingEntity);
        service.delete(ID);
        verify(repository, times(1)).delete(personalizedWorkoutTrainingEntity);
    }

    @Test
    void testDelete_WhenIdPassedIsNull_ShouldThrowUuidUtilsException() {

        UuidUtilsException exception = assertThrows(UuidUtilsException.class, () ->
                service.delete(null));

        assertNotNull(exception);
        assertEquals(UuidUtilsException.ERROR.formatErrorMessage(UUID_REQUIRED)
                , exception.getMessage());
    }

    @Test
    void testDelete_WhenIdPassedWasNotFoundIntoDatabase_ShouldThrowPersonalizedWorkoutTrainingNotFoundException() {

        PersonalizedWorkoutTrainingNotFound exception = assertThrows(PersonalizedWorkoutTrainingNotFound.class, () ->
                service.delete(ID));

        assertNotNull(exception);
        assertEquals(PersonalizedWorkoutTrainingNotFound.ERROR.formatErrorMessage(PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE)
                , exception.getMessage());
    }
}











