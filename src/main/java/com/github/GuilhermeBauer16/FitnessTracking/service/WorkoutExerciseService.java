package com.github.GuilhermeBauer16.FitnessTracking.service;

import com.github.GuilhermeBauer16.FitnessTracking.mapper.Mapper;
import com.github.GuilhermeBauer16.FitnessTracking.model.WorkoutExerciseEntity;
import com.github.GuilhermeBauer16.FitnessTracking.repository.WorkoutExerciseRepository;
import com.github.GuilhermeBauer16.FitnessTracking.service.contract.CrudServiceContract;
import com.github.GuilhermeBauer16.FitnessTracking.utils.CheckIfNotNull;
import com.github.GuilhermeBauer16.FitnessTracking.utils.UuidUtils;
import com.github.GuilhermeBauer16.FitnessTracking.utils.ValidatorUtils;
import com.github.GuilhermeBauer16.FitnessTracking.vo.WorkoutExerciseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class WorkoutExerciseService implements CrudServiceContract<WorkoutExerciseVO, String> {

    private static final Mapper<WorkoutExerciseEntity, WorkoutExerciseVO> workoutExerciseVOMapper =
            new Mapper<>(WorkoutExerciseEntity.class, WorkoutExerciseVO.class);

    private static final Mapper<WorkoutExerciseVO, WorkoutExerciseEntity> workoutExerciseEntityMapper =
            new Mapper<>(WorkoutExerciseVO.class, WorkoutExerciseEntity.class);


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
        workoutExerciseVOMapper.parseObject(repository.save(entity));

        return workoutExerciseVOMapper.parseObject(repository.save(entity));


    }

    @Override
    @Transactional
    public WorkoutExerciseVO update(WorkoutExerciseVO workoutExerciseVO) {

        UuidUtils.isValidUuid(workoutExerciseVO.getId());
        WorkoutExerciseEntity workoutExerciseEntity = repository.findById(workoutExerciseVO.getId())
                .orElseThrow(() -> new RuntimeException("Don't do that!"));
        ValidatorUtils.checkNotNullAndNotEmptyOrThrowException(workoutExerciseEntity);
        WorkoutExerciseEntity updatedEntity = CheckIfNotNull.updateIfNotNull(workoutExerciseEntity, workoutExerciseVO);

        return workoutExerciseVOMapper.parseObject(repository.save(updatedEntity));
    }

    @Override
    public WorkoutExerciseVO findById(String id) {

        UuidUtils.isValidUuid(id);
        WorkoutExerciseEntity workoutExerciseEntity = repository.findById(id).orElseThrow(() -> new RuntimeException("Don't do that!"));
        ValidatorUtils.checkNotNullAndNotEmptyOrThrowException(workoutExerciseEntity);
        return workoutExerciseVOMapper.parseObject(workoutExerciseEntity);

    }

    @Override
    public List<WorkoutExerciseVO> findAll() {

        return workoutExerciseVOMapper.parseObjectList(repository.findAll());
    }

    @Override
    public void delete(String id) {

        UuidUtils.isValidUuid(id);
        WorkoutExerciseEntity workoutExerciseEntity = repository.findById(id).orElseThrow(() -> new RuntimeException("Have an error!"));
        ValidatorUtils.checkNotNullAndNotEmptyOrThrowException(workoutExerciseEntity);
        repository.delete(workoutExerciseEntity);

    }
}
