package com.github.GuilhermeBauer16.FitnessTracking.controller;

import com.github.GuilhermeBauer16.FitnessTracking.controller.contract.CrudControllerContract;
import com.github.GuilhermeBauer16.FitnessTracking.service.WorkoutExerciseService;
import com.github.GuilhermeBauer16.FitnessTracking.vo.WorkoutExerciseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workoutExercise")
public class WorkoutExerciseController implements CrudControllerContract<WorkoutExerciseVO,String> {

    @Autowired
    private  WorkoutExerciseService service;


    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkoutExerciseVO> create(@RequestBody WorkoutExerciseVO workoutExerciseVO) {
        WorkoutExerciseVO createdWorkoutExercise = service.create(workoutExerciseVO);
        return new ResponseEntity<>(createdWorkoutExercise, HttpStatus.CREATED);
    }

    @Override
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkoutExerciseVO> update(@RequestBody WorkoutExerciseVO workoutExerciseVO) {
        WorkoutExerciseVO update = service.update(workoutExerciseVO);
        return ResponseEntity.ok(update);
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<WorkoutExerciseVO> findById(@PathVariable(value = "id") String id) {

        WorkoutExerciseVO workoutExerciseVO = service.findById(id);
        return ResponseEntity.ok(workoutExerciseVO);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<WorkoutExerciseVO>> findAll() {
        List<WorkoutExerciseVO> workoutExerciseVOList = service.findAll();
        return ResponseEntity.ok(workoutExerciseVOList);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
