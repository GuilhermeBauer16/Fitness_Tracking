package com.github.GuilhermeBauer16.FitnessTracking.controller;

import com.github.GuilhermeBauer16.FitnessTracking.controller.contract.WorkoutExerciseControllerContract;
import com.github.GuilhermeBauer16.FitnessTracking.service.WorkoutExerciseService;
import com.github.GuilhermeBauer16.FitnessTracking.vo.WorkoutExerciseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workoutExercise")
public class WorkoutExerciseController implements WorkoutExerciseControllerContract<WorkoutExerciseVO, String> {

    @Autowired
    private WorkoutExerciseService service;


    @Override
    public ResponseEntity<WorkoutExerciseVO> create(@RequestBody WorkoutExerciseVO workoutExerciseVO) {
        WorkoutExerciseVO createdWorkoutExercise = service.create(workoutExerciseVO);
        return new ResponseEntity<>(createdWorkoutExercise, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<WorkoutExerciseVO> update(@RequestBody WorkoutExerciseVO workoutExerciseVO) {
        WorkoutExerciseVO update = service.update(workoutExerciseVO);
        return ResponseEntity.ok(update);
    }

    @Override

    public ResponseEntity<WorkoutExerciseVO> findById(@PathVariable(value = "id") String id) {

        WorkoutExerciseVO workoutExerciseVO = service.findById(id);
        return ResponseEntity.ok(workoutExerciseVO);
    }

    @Override

    public ResponseEntity<List<WorkoutExerciseVO>> findByMuscleGroup(@RequestBody WorkoutExerciseVO workoutExerciseVO) {
        List<WorkoutExerciseVO> byMuscleGroup = service.findByMuscleGroup(workoutExerciseVO);
        return ResponseEntity.ok(byMuscleGroup);
    }

    @Override

    public ResponseEntity<List<WorkoutExerciseVO>> findByDifficultLevel(@RequestBody WorkoutExerciseVO workoutExerciseVO) {
        List<WorkoutExerciseVO> byDifficultLevel = service.findByDifficultLevel(workoutExerciseVO);
        return ResponseEntity.ok(byDifficultLevel);
    }

    @Override

    public ResponseEntity<Page<WorkoutExerciseVO>> findAll(@PageableDefault(sort = "name", size = 20) final Pageable pageable) {
        Page<WorkoutExerciseVO> workoutExerciseVOList = service.findAll(pageable);
        return ResponseEntity.ok(workoutExerciseVOList);
    }

    @Override

    public ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
