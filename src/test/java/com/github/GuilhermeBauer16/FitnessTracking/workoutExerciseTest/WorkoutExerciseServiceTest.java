package com.github.GuilhermeBauer16.FitnessTracking.workoutExerciseTest;

import com.github.GuilhermeBauer16.FitnessTracking.enums.DifficultyLevel;
import com.github.GuilhermeBauer16.FitnessTracking.enums.ExerciseType;
import com.github.GuilhermeBauer16.FitnessTracking.enums.MuscleGroup;
import com.github.GuilhermeBauer16.FitnessTracking.exception.UuidUtilsException;
import com.github.GuilhermeBauer16.FitnessTracking.exception.WorkoutExerciseNotFound;
import com.github.GuilhermeBauer16.FitnessTracking.mapper.Mapper;
import com.github.GuilhermeBauer16.FitnessTracking.model.WorkoutExerciseEntity;
import com.github.GuilhermeBauer16.FitnessTracking.repository.WorkoutExerciseRepository;
import com.github.GuilhermeBauer16.FitnessTracking.service.WorkoutExerciseService;
import com.github.GuilhermeBauer16.FitnessTracking.utils.UuidUtils;
import com.github.GuilhermeBauer16.FitnessTracking.utils.ValidatorUtils;
import com.github.GuilhermeBauer16.FitnessTracking.vo.WorkoutExerciseVO;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutExerciseServiceTest {

    @Mock
    private WorkoutExerciseRepository repository;

    @Mock
    private Mapper<WorkoutExerciseEntity, WorkoutExerciseVO> workoutExerciseVOMapper;

    @Mock
    private Mapper<WorkoutExerciseVO, WorkoutExerciseEntity> workoutExerciseEntityMapper;

    @InjectMocks
    private WorkoutExerciseService service;

    private WorkoutExerciseVO workoutExerciseVO;
    private WorkoutExerciseEntity workoutExerciseEntity;

    public final String UUID_REQUIRED = "An UUID need to be informed!";
    private final String WORKOUT_EXERCISE_NOT_FOUND_MESSAGE = "Can not be find that Workout exercise!";
    private static final String WORKOUT_EXERCISE_LIST_NOT_FOUND_MESSAGE = "Can not be find any Workout exercise!";

    private final String ID = "d8e7df81-2cd4-41a2-a005-62e6d8079716";
    private final String INVALID_ID = "d8e";
    private final String NAME = "push-up";
    private final String DESCRIPTION = "A push-up is a common calisthenics exercise beginning from the prone position.";
    private final int CALORIES_BURNED = 50;
    private final ExerciseType EXERCISE_TYPE = ExerciseType.STRENGTH;
    private final String EQUIPMENT_NEEDED = "None";
    private final DifficultyLevel DIFFICULTY_LEVEL = DifficultyLevel.BEGINNER;
    private final Set<MuscleGroup> MUSCLE_GROUPS = new HashSet<>(Arrays.asList(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS));


    @BeforeEach
    void setUp() {
        workoutExerciseVO = new WorkoutExerciseVO(ID, NAME, DESCRIPTION, CALORIES_BURNED, EXERCISE_TYPE, EQUIPMENT_NEEDED, DIFFICULTY_LEVEL, MUSCLE_GROUPS);
        workoutExerciseEntity = new WorkoutExerciseEntity(ID, NAME, DESCRIPTION, CALORIES_BURNED, EXERCISE_TYPE, EQUIPMENT_NEEDED, DIFFICULTY_LEVEL, MUSCLE_GROUPS);
    }

    @Test
    void testCreateWorkoutExercises_WhenSaveWorkoutExercise_ShouldReturnWorkoutExerciseObject() {

        try (MockedStatic<UuidUtils> mockedUuidUtils = Mockito.mockStatic(UuidUtils.class);
             MockedStatic<ValidatorUtils> mockedValidatorUtils = Mockito.mockStatic(ValidatorUtils.class)) {

            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectOrThrowException(any(), anyString(), any(Class.class))).thenAnswer(invocation -> null);
            mockedUuidUtils.when(UuidUtils::generateUuid).thenReturn(ID);

            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any(Class.class))).thenAnswer(invocation -> null);
            when(repository.save(any(WorkoutExerciseEntity.class))).thenReturn(workoutExerciseEntity);


            WorkoutExerciseVO createdWorkoutExerciseVO = service.create(workoutExerciseVO);

            assertNotNull(createdWorkoutExerciseVO);
            assertEquals(ID, createdWorkoutExerciseVO.getId());
            assertEquals(NAME, createdWorkoutExerciseVO.getName());
            assertEquals(DESCRIPTION, createdWorkoutExerciseVO.getDescription());
            assertEquals(CALORIES_BURNED, createdWorkoutExerciseVO.getCaloriesBurned());
            assertEquals(EXERCISE_TYPE, createdWorkoutExerciseVO.getExerciseType());
            assertEquals(DIFFICULTY_LEVEL, createdWorkoutExerciseVO.getDifficultyLevel());
            assertEquals(MUSCLE_GROUPS, createdWorkoutExerciseVO.getMuscleGroups());

            mockedValidatorUtils.verify(() -> ValidatorUtils.checkObjectOrThrowException(any(), anyString(), any(Class.class)));
            mockedUuidUtils.verify(UuidUtils::generateUuid);
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any(Class.class)));

        }
    }

    @Test
    void testCreateWorkoutExercise_When_WorkoutExerciseIsNull_ShouldThrowWorkoutExerciseNotFound() {

        WorkoutExerciseNotFound exception = assertThrows(WorkoutExerciseNotFound.class, () ->
                service.create(null));

        assertNotNull(exception);
        assertEquals(WorkoutExerciseNotFound.ERROR.formatErrorMessage(WORKOUT_EXERCISE_NOT_FOUND_MESSAGE), exception.getMessage());

    }

    @Test
    void testUpdateWorkoutExercises_WhenUpdatedWorkoutExercise_ShouldReturnWorkoutExerciseObject() {

        try (MockedStatic<UuidUtils> mockedUuidUtils = Mockito.mockStatic(UuidUtils.class);
             MockedStatic<ValidatorUtils> mockedValidatorUtils = Mockito.mockStatic(ValidatorUtils.class)) {

            workoutExerciseEntity.setName("squat");
            workoutExerciseEntity.setCaloriesBurned(70);

            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectOrThrowException(any(), anyString(), any(Class.class)))
                    .thenAnswer(invocation -> null);
            mockedUuidUtils.when(() -> UuidUtils.isValidUuid(workoutExerciseEntity.getId()))
                    .thenReturn(true);
            mockedValidatorUtils.when(() -> ValidatorUtils.updateFieldIfNotNull(any(WorkoutExerciseEntity.class), any(WorkoutExerciseVO.class), anyString(), any()))
                    .thenAnswer(invocation -> workoutExerciseEntity); // Return updated entity
            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()))
                    .thenAnswer(invocation -> null);


            when(repository.findById(anyString())).thenReturn(Optional.of(workoutExerciseEntity));
            when(repository.save(any(WorkoutExerciseEntity.class))).thenReturn(workoutExerciseEntity);

            WorkoutExerciseVO updatedWorkoutExerciseVO = service.update(workoutExerciseVO);


            assertNotNull(updatedWorkoutExerciseVO);
            assertEquals(ID, updatedWorkoutExerciseVO.getId());
            assertEquals("squat", updatedWorkoutExerciseVO.getName());
            assertEquals(DESCRIPTION, updatedWorkoutExerciseVO.getDescription());
            assertEquals(70, updatedWorkoutExerciseVO.getCaloriesBurned());
            assertEquals(EXERCISE_TYPE, updatedWorkoutExerciseVO.getExerciseType());
            assertEquals(DIFFICULTY_LEVEL, updatedWorkoutExerciseVO.getDifficultyLevel());
            assertEquals(MUSCLE_GROUPS, updatedWorkoutExerciseVO.getMuscleGroups());

            mockedValidatorUtils.verify(() -> ValidatorUtils.checkObjectOrThrowException(any(), anyString(), any(Class.class)));
            mockedUuidUtils.verify(() -> UuidUtils.isValidUuid(anyString()));
            mockedValidatorUtils.verify(() -> ValidatorUtils.updateFieldIfNotNull(any(), any(), anyString(), any()));
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()));
        }
    }

    @Test
    void testUpdateWorkoutExercise_WhenUuidIsNull_ShouldThrowUuidUtilsException() {

        workoutExerciseVO.setId(null);
        UuidUtilsException exception = assertThrows(UuidUtilsException.class, () ->
                service.update(workoutExerciseVO));

        assertNotNull(exception);
        assertEquals(UuidUtilsException.ERROR.formatErrorMessage(UUID_REQUIRED), exception.getMessage());


    }

    @Test
    void testUpdateWorkoutExercise_WhenWorkoutExerciseNotExistsInTheDatabase_ShouldWorkoutExerciseNotFound() {

        when(repository.findById(workoutExerciseVO.getId())).thenReturn(Optional.empty());
        WorkoutExerciseNotFound exception = assertThrows(WorkoutExerciseNotFound.class, () ->
                service.update(workoutExerciseVO));

        assertNotNull(exception);
        assertEquals(WorkoutExerciseNotFound.ERROR.formatErrorMessage(WORKOUT_EXERCISE_NOT_FOUND_MESSAGE), exception.getMessage());


    }

    @Test
    void testUpdateWorkoutExercise_WhenUuidIsInvalid_ShouldThrowUuidUtilsException() {

        workoutExerciseVO.setId(INVALID_ID);

        UuidUtilsException exception = assertThrows(UuidUtilsException.class, () ->
                service.update(workoutExerciseVO));
        assertNotNull(exception);
        assertEquals(UuidUtilsException.ERROR.formatErrorMessage(INVALID_ID), exception.getMessage());


    }

    @Test
    void testUpdateWorkoutExercise_When_WorkoutExerciseIsNull_ShouldThrowWorkoutExerciseNotFound() {

        WorkoutExerciseNotFound exception = assertThrows(WorkoutExerciseNotFound.class, () ->
                service.create(null));

        assertNotNull(exception);
        assertEquals(WorkoutExerciseNotFound.ERROR.formatErrorMessage(WORKOUT_EXERCISE_NOT_FOUND_MESSAGE), exception.getMessage());

    }

    @Test
    void testWorkoutExercises_WhenFindWorkoutExerciseById_ShouldReturnWorkoutExerciseObject() {

        try (MockedStatic<ValidatorUtils> validatorUtilsMockedStatic = Mockito.mockStatic(ValidatorUtils.class);
             MockedStatic<UuidUtils> uuidUtilsMockedStatic = Mockito.mockStatic(UuidUtils.class);) {

            uuidUtilsMockedStatic.when(() -> UuidUtils.isValidUuid(ID)).thenReturn(true);
            validatorUtilsMockedStatic.when(
                    () -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(
                            any(), anyString(), any(Class.class))).thenAnswer(invocation -> null);
            ;
            when(repository.findById(ID)).thenReturn(Optional.of(workoutExerciseEntity));
            WorkoutExerciseVO findWorkoutExerciseVOByID = service.findById(ID);

            assertNotNull(findWorkoutExerciseVOByID);
            assertEquals(ID, findWorkoutExerciseVOByID.getId());
            assertEquals(NAME, findWorkoutExerciseVOByID.getName());
            assertEquals(DESCRIPTION, findWorkoutExerciseVOByID.getDescription());
            assertEquals(CALORIES_BURNED, findWorkoutExerciseVOByID.getCaloriesBurned());
            assertEquals(EXERCISE_TYPE, findWorkoutExerciseVOByID.getExerciseType());
            assertEquals(DIFFICULTY_LEVEL, findWorkoutExerciseVOByID.getDifficultyLevel());
            assertEquals(MUSCLE_GROUPS, findWorkoutExerciseVOByID.getMuscleGroups());

            uuidUtilsMockedStatic.verify(() -> UuidUtils.isValidUuid(ID));
            validatorUtilsMockedStatic.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any(Class.class)));


        }

    }

    @Test
    void testFindWorkoutExerciseById_When_UUIDIsNull_ShouldThrowUuidUtilsException() {

        UuidUtilsException exception = assertThrows(UuidUtilsException.class, () -> service.findById(null));
        assertNotNull(exception);
        assertEquals(UuidUtilsException.ERROR.formatErrorMessage(UUID_REQUIRED), exception.getMessage());
    }

    @Test
    void testFindWorkoutExerciseById_When_UUIDIsInvalid_ShouldThrowUuidUtilsException() {

        UuidUtilsException exception = assertThrows(UuidUtilsException.class, () -> service.findById(INVALID_ID));
        assertNotNull(exception);
        assertEquals(UuidUtilsException.ERROR.formatErrorMessage(INVALID_ID), exception.getMessage());
    }

    @Test
    void testFindWorkoutExerciseById_WhenWorkoutExerciseNotExistsInTheDatabase_ThrowWorkoutExerciseNotFound() {

        when(repository.findById(workoutExerciseVO.getId())).thenReturn(Optional.empty());
        WorkoutExerciseNotFound exception = assertThrows(WorkoutExerciseNotFound.class, () ->
                service.findById(workoutExerciseVO.getId()));

        assertNotNull(exception);
        assertEquals(WorkoutExerciseNotFound.ERROR.formatErrorMessage(WORKOUT_EXERCISE_NOT_FOUND_MESSAGE), exception.getMessage());

    }



    @Test
    void testWorkoutExercise_When_FindAll_ShouldReturnWorkoutExerciseList() {

        WorkoutExerciseEntity workoutExerciseEntity2 = workoutExerciseEntity;
        List<WorkoutExerciseEntity> workoutExerciseEntities = new ArrayList<WorkoutExerciseEntity>(Arrays.asList(workoutExerciseEntity, workoutExerciseEntity2));

        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(workoutExerciseEntities));
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<WorkoutExerciseVO> workoutExerciseVOPage = service.findAll(pageRequest);

        assertNotNull(workoutExerciseVOPage);

        WorkoutExerciseVO workoutExerciseVO1 = workoutExerciseVOPage.getContent().get(0);
        assertEquals(ID, workoutExerciseVO1.getId());
        assertEquals(NAME, workoutExerciseVO1.getName());
        assertEquals(DESCRIPTION, workoutExerciseVO1.getDescription());
        assertEquals(CALORIES_BURNED, workoutExerciseVO1.getCaloriesBurned());
        assertEquals(EXERCISE_TYPE, workoutExerciseVO1.getExerciseType());
        assertEquals(DIFFICULTY_LEVEL, workoutExerciseVO1.getDifficultyLevel());
        assertEquals(MUSCLE_GROUPS, workoutExerciseVO1.getMuscleGroups());

        verify(repository,times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testDelete_WhenDeletingWorkoutExercise_thenDoNothing(){

        when(repository.findById(workoutExerciseEntity.getId())).thenReturn(Optional.of(workoutExerciseEntity));
        doNothing().when(repository).delete(workoutExerciseEntity);
        service.delete(workoutExerciseEntity.getId());
        verify(repository, times(1)).delete(workoutExerciseEntity);
    }

    @Test
    void testDeleteWorkoutExercise_WhenUuidIsNull_ShouldThrowUuidUtilsException() {

        workoutExerciseVO.setId(null);
        UuidUtilsException exception = assertThrows(UuidUtilsException.class, () ->
                service.delete(workoutExerciseVO.getId()));

        assertNotNull(exception);
        assertEquals(UuidUtilsException.ERROR.formatErrorMessage(UUID_REQUIRED), exception.getMessage());


    }

    @Test
    void testDeleteWorkoutExercise_WhenUuidIsInvalid_ShouldThrowUuidUtilsException() {

        workoutExerciseVO.setId(INVALID_ID);
        UuidUtilsException exception = assertThrows(UuidUtilsException.class, () ->
                service.delete(workoutExerciseVO.getId()));

        assertNotNull(exception);
        assertEquals(UuidUtilsException.ERROR.formatErrorMessage(INVALID_ID), exception.getMessage());


    }

    @Test
    void testDeleteWorkoutExercise_WhenWorkoutExerciseNotExistsInTheDatabase_ThrowWorkoutExerciseNotFound() {

        when(repository.findById(workoutExerciseVO.getId())).thenReturn(Optional.empty());
        WorkoutExerciseNotFound exception = assertThrows(WorkoutExerciseNotFound.class, () ->
                service.delete(workoutExerciseVO.getId()));

        assertNotNull(exception);
        assertEquals(WorkoutExerciseNotFound.ERROR.formatErrorMessage(WORKOUT_EXERCISE_NOT_FOUND_MESSAGE), exception.getMessage());

    }




}

