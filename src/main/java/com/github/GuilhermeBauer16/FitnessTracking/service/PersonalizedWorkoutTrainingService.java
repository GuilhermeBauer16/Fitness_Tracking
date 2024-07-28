package com.github.GuilhermeBauer16.FitnessTracking.service;

import com.github.GuilhermeBauer16.FitnessTracking.exception.FieldNotFound;
import com.github.GuilhermeBauer16.FitnessTracking.exception.PersonalizedWorkoutTrainingNotFound;
import com.github.GuilhermeBauer16.FitnessTracking.factory.PersonalizedWorkoutTrainingFactory;
import com.github.GuilhermeBauer16.FitnessTracking.mapper.Mapper;
import com.github.GuilhermeBauer16.FitnessTracking.model.PersonalizedWorkoutTrainingEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.WorkoutExerciseEntity;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.PersonalizedWorkoutTrainingVO;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.WorkoutExerciseVO;
import com.github.GuilhermeBauer16.FitnessTracking.repository.PersonalizedWorkoutTrainingRepository;
import com.github.GuilhermeBauer16.FitnessTracking.service.contract.PersonalizedWorkoutTrainingServiceContract;
import com.github.GuilhermeBauer16.FitnessTracking.utils.UuidUtils;
import com.github.GuilhermeBauer16.FitnessTracking.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonalizedWorkoutTrainingService implements PersonalizedWorkoutTrainingServiceContract<PersonalizedWorkoutTrainingVO, String> {

    @Autowired
    private WorkoutExerciseService workoutExerciseService;

    @Autowired
    private PersonalizedWorkoutTrainingRepository repository;


    private final Mapper<WorkoutExerciseVO, WorkoutExerciseEntity> workoutExerciseEntityMapper =
            new Mapper<>(WorkoutExerciseVO.class, WorkoutExerciseEntity.class);

    private final Mapper<PersonalizedWorkoutTrainingVO, PersonalizedWorkoutTrainingEntity> personalizedWorkoutTrainingEntityMapper =
            new Mapper<>(PersonalizedWorkoutTrainingVO.class, PersonalizedWorkoutTrainingEntity.class);

    private final Mapper<PersonalizedWorkoutTrainingEntity, PersonalizedWorkoutTrainingVO> personalizedWorkoutTrainingVOMapper =
            new Mapper<>(PersonalizedWorkoutTrainingEntity.class, PersonalizedWorkoutTrainingVO.class);


    private static final String PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE =
            "Can not be find any Workout exercise into your Personalized workout training!";

    public PersonalizedWorkoutTrainingService(WorkoutExerciseService workoutExerciseService, PersonalizedWorkoutTrainingRepository repository) {
        this.workoutExerciseService = workoutExerciseService;
        this.repository = repository;
    }

    @Override
    public PersonalizedWorkoutTrainingVO create(PersonalizedWorkoutTrainingVO personalizedWorkoutTrainingVO) {

        ValidatorUtils.checkObjectIsNullOrThrowException(personalizedWorkoutTrainingVO,
                PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE, PersonalizedWorkoutTrainingNotFound.class);

        WorkoutExerciseVO workoutExerciseVO = workoutExerciseService.findById(personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getId());
        WorkoutExerciseEntity workoutExerciseEntity = workoutExerciseEntityMapper.parseObject(workoutExerciseVO);
        PersonalizedWorkoutTrainingEntity personalizedWorkoutTrainingEntity = personalizedWorkoutTrainingEntityMapper.
                parseObject(personalizedWorkoutTrainingVO);


        personalizedWorkoutTrainingEntity = PersonalizedWorkoutTrainingFactory.create(workoutExerciseEntity, personalizedWorkoutTrainingVO.getRepetitions(),
                personalizedWorkoutTrainingEntity.getSeries(), personalizedWorkoutTrainingEntity.getWeight());

        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(personalizedWorkoutTrainingEntity, PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE,
                FieldNotFound.class);
        repository.save(personalizedWorkoutTrainingEntity);
        return personalizedWorkoutTrainingVOMapper.parseObject(personalizedWorkoutTrainingEntity);


    }

    @Override
    public PersonalizedWorkoutTrainingVO update(PersonalizedWorkoutTrainingVO personalizedWorkoutTrainingVO) {

        ValidatorUtils.checkObjectIsNullOrThrowException(personalizedWorkoutTrainingVO,
                PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE, PersonalizedWorkoutTrainingNotFound.class);

        UuidUtils.isValidUuid(personalizedWorkoutTrainingVO.getId());

        PersonalizedWorkoutTrainingEntity personalizedWorkoutTrainingEntity = repository.findById(personalizedWorkoutTrainingVO.getId()).orElseThrow(() -> new
                PersonalizedWorkoutTrainingNotFound(PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE));


        PersonalizedWorkoutTrainingEntity updatedpersonalizedWorkoutTrainingEntity = ValidatorUtils.
                updateFieldIfNotNull(personalizedWorkoutTrainingEntity, personalizedWorkoutTrainingVO,
                        PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE, PersonalizedWorkoutTrainingNotFound.class);

        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(updatedpersonalizedWorkoutTrainingEntity,
                PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE, FieldNotFound.class);

        repository.save(updatedpersonalizedWorkoutTrainingEntity);
        return personalizedWorkoutTrainingVOMapper.parseObject(updatedpersonalizedWorkoutTrainingEntity);


    }

    @Override
    public PersonalizedWorkoutTrainingVO findById(String id) {

        UuidUtils.isValidUuid(id);
        PersonalizedWorkoutTrainingEntity personalizedWorkoutTrainingEntity = repository.findById(id)
                .orElseThrow(() -> new PersonalizedWorkoutTrainingNotFound(PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE));

        return personalizedWorkoutTrainingVOMapper.parseObject(personalizedWorkoutTrainingEntity);
    }

    @Override
    public List<PersonalizedWorkoutTrainingVO> findPersonalizedWorkoutTrainingByMuscleGroup(PersonalizedWorkoutTrainingVO personalizedWorkoutTrainingVO) {

        List<PersonalizedWorkoutTrainingEntity> byMuscleGroup = repository.findByMuscleGroup(
                personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getMuscleGroups());


        return personalizedWorkoutTrainingVOMapper.parseObjectList(byMuscleGroup);
    }

    @Override
    public List<PersonalizedWorkoutTrainingVO> findPersonalizedWorkoutTrainingByDifficultLevel(PersonalizedWorkoutTrainingVO personalizedWorkoutTrainingVO) {

        List<PersonalizedWorkoutTrainingEntity> byMuscleGroup = repository.findByDifficultyLevel(
                personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getDifficultyLevel());

        return personalizedWorkoutTrainingVOMapper.parseObjectList(byMuscleGroup);
    }

    @Override
    public List<PersonalizedWorkoutTrainingVO> findPersonalizedWorkoutTrainingByName(PersonalizedWorkoutTrainingVO personalizedWorkoutTrainingVO) {

        List<PersonalizedWorkoutTrainingEntity> byMuscleGroup = repository.
                findByName(personalizedWorkoutTrainingVO.getWorkoutExerciseEntity().getName());

        return personalizedWorkoutTrainingVOMapper.parseObjectList(byMuscleGroup);
    }


    @Override
    public Page<PersonalizedWorkoutTrainingVO> findAll(Pageable pageable) {

        Page<PersonalizedWorkoutTrainingEntity> all = repository.findAll(pageable);
        List<PersonalizedWorkoutTrainingVO> personalizedWorkoutTrainingVOS = personalizedWorkoutTrainingVOMapper.parseObjectList(all.getContent());
        return new PageImpl<>(personalizedWorkoutTrainingVOS, pageable, all.getTotalElements());

    }

    @Override
    public void delete(String id) {

        UuidUtils.isValidUuid(id);
        PersonalizedWorkoutTrainingEntity personalizedWorkoutTrainingEntity = repository.findById(id)
                .orElseThrow(() -> new PersonalizedWorkoutTrainingNotFound(PERSONALIZED_WORKOUT_TRAINING_NOT_FOUND_MESSAGE));

        repository.delete(personalizedWorkoutTrainingEntity);

    }
}
