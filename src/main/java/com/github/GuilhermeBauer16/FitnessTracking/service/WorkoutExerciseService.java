package com.github.GuilhermeBauer16.FitnessTracking.service;

import com.github.GuilhermeBauer16.FitnessTracking.exception.FieldNotFound;
import com.github.GuilhermeBauer16.FitnessTracking.exception.WorkoutExerciseNotFound;
import com.github.GuilhermeBauer16.FitnessTracking.mapper.Mapper;
import com.github.GuilhermeBauer16.FitnessTracking.model.WorkoutExerciseEntity;
import com.github.GuilhermeBauer16.FitnessTracking.repository.WorkoutExerciseRepository;
import com.github.GuilhermeBauer16.FitnessTracking.service.contract.CrudServiceContract;
import com.github.GuilhermeBauer16.FitnessTracking.utils.UuidUtils;
import com.github.GuilhermeBauer16.FitnessTracking.utils.ValidatorUtils;
import com.github.GuilhermeBauer16.FitnessTracking.vo.WorkoutExerciseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class WorkoutExerciseService implements CrudServiceContract<WorkoutExerciseVO, String> {

    private static final Mapper<WorkoutExerciseEntity, WorkoutExerciseVO> workoutExerciseVOMapper =
            new Mapper<>(WorkoutExerciseEntity.class, WorkoutExerciseVO.class);

    private static final Mapper<WorkoutExerciseVO, WorkoutExerciseEntity> workoutExerciseEntityMapper =
            new Mapper<>(WorkoutExerciseVO.class, WorkoutExerciseEntity.class);

    private static final String WORKOUT_EXERCISE_NOT_FOUND_MESSAGE = "Can not be find that Workout exercise!";
    private static final String WORKOUT_EXERCISE_LIST_NOT_FOUND_MESSAGE = "Can not be find any Workout exercise!";


    private final WorkoutExerciseRepository repository;

    @Autowired
    public WorkoutExerciseService(WorkoutExerciseRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public WorkoutExerciseVO create(WorkoutExerciseVO workoutExerciseVO) {

        workoutExerciseVO.setId(UuidUtils.generateUuid());
        UuidUtils.isValidUuid(workoutExerciseVO.getId());
        WorkoutExerciseEntity entity = workoutExerciseEntityMapper.parseObject(workoutExerciseVO);
        ValidatorUtils.checkNotNullAndNotEmptyOrThrowException(entity, WORKOUT_EXERCISE_NOT_FOUND_MESSAGE, FieldNotFound.class);
        workoutExerciseVOMapper.parseObject(repository.save(entity));

        return workoutExerciseVOMapper.parseObject(repository.save(entity));


    }

    @Override
    @Transactional
    public WorkoutExerciseVO update(WorkoutExerciseVO workoutExerciseVO) {

        UuidUtils.isValidUuid(workoutExerciseVO.getId());
        WorkoutExerciseEntity workoutExerciseEntity = repository.findById(workoutExerciseVO.getId())
                .orElseThrow(() -> new WorkoutExerciseNotFound(WORKOUT_EXERCISE_NOT_FOUND_MESSAGE));

        WorkoutExerciseEntity updatedEntity = ValidatorUtils.updateFieldIfNotNull(workoutExerciseEntity, workoutExerciseVO,
                WORKOUT_EXERCISE_NOT_FOUND_MESSAGE, WorkoutExerciseNotFound.class);

        ValidatorUtils.checkNotNullAndNotEmptyOrThrowException(updatedEntity,
                WORKOUT_EXERCISE_NOT_FOUND_MESSAGE, FieldNotFound.class);


        return workoutExerciseVOMapper.parseObject(repository.save(updatedEntity));
    }

    @Override
    public WorkoutExerciseVO findById(String id) {

        UuidUtils.isValidUuid(id);
        WorkoutExerciseEntity workoutExerciseEntity = repository.findById(id)
                .orElseThrow(() -> new WorkoutExerciseNotFound(WORKOUT_EXERCISE_NOT_FOUND_MESSAGE));
        ValidatorUtils.checkNotNullAndNotEmptyOrThrowException(workoutExerciseEntity,
                WORKOUT_EXERCISE_NOT_FOUND_MESSAGE, FieldNotFound.class);
        return workoutExerciseVOMapper.parseObject(workoutExerciseEntity);

    }

    @Override
    public Page<WorkoutExerciseVO> findAll(final Pageable pageable) {

        Page<WorkoutExerciseEntity> all = repository.findAll(pageable);
        List<WorkoutExerciseVO> workoutExerciseVOList = workoutExerciseVOMapper.parseObjectList(all.getContent());
        if( workoutExerciseVOList.isEmpty()){
            throw new WorkoutExerciseNotFound(WORKOUT_EXERCISE_LIST_NOT_FOUND_MESSAGE);
        }
        return new PageImpl<>( workoutExerciseVOList,pageable, all.getTotalElements());

    }


    @Override
    public void delete(String id) {

        UuidUtils.isValidUuid(id);
        WorkoutExerciseEntity workoutExerciseEntity = repository.findById(id)
                .orElseThrow(() -> new WorkoutExerciseNotFound(WORKOUT_EXERCISE_NOT_FOUND_MESSAGE));

        repository.delete(workoutExerciseEntity);

    }
}
