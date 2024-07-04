package com.github.GuilhermeBauer16.FitnessTracking.controller;

import com.github.GuilhermeBauer16.FitnessTracking.controller.contract.CrudControllerContract;
import com.github.GuilhermeBauer16.FitnessTracking.service.WorkoutExerciseService;
import com.github.GuilhermeBauer16.FitnessTracking.vo.WorkoutExerciseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workoutExercise")
public class WorkoutExerciseController implements CrudControllerContract<WorkoutExerciseVO, String> {

    @Autowired
    private WorkoutExerciseService service;


    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(summary = "Register a new Workout exercise", description = "Returns a created Workout exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = WorkoutExerciseVO.class))),
            @ApiResponse(responseCode = "404", description = "Will throw a custom exception: Workout Exercise Not Found Exception" +
                    " or Field Not Found Exception, depending of the situation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    public ResponseEntity<WorkoutExerciseVO> create(@RequestBody WorkoutExerciseVO workoutExerciseVO) {
        WorkoutExerciseVO createdWorkoutExercise = service.create(workoutExerciseVO);
        return new ResponseEntity<>(createdWorkoutExercise, HttpStatus.CREATED);
    }

    @Override
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(summary = "Update a Workout exercise", description = "Returns a updated Workout exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = WorkoutExerciseVO.class))),
            @ApiResponse(responseCode = "404", description = "Will throw a custom exception: Workout Exercise Not Found Exception" +
                    " or Field Not Found Exception, depending of the situation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    public ResponseEntity<WorkoutExerciseVO> update(@RequestBody WorkoutExerciseVO workoutExerciseVO) {
        WorkoutExerciseVO update = service.update(workoutExerciseVO);
        return ResponseEntity.ok(update);
    }

    @Override
    @GetMapping(value = "/{id}")

    @Operation(summary = "Get a single Workout exercise", description = "Returns a Workout exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = WorkoutExerciseVO.class))),
            @ApiResponse(responseCode = "404", description = "Will throw a custom exception: Workout Exercise Not Found Exception"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    public ResponseEntity<WorkoutExerciseVO> findById(@PathVariable(value = "id") String id) {

        WorkoutExerciseVO workoutExerciseVO = service.findById(id);
        return ResponseEntity.ok(workoutExerciseVO);
    }

    @Override
    @GetMapping
    @Operation(summary = "Get a Workout exercise list", description = "Returns a list of Workout exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = WorkoutExerciseVO.class))),
            @ApiResponse(responseCode = "404", description = "Will throw a custom exception: Workout Exercise Not Found Exception"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    public ResponseEntity<Page<WorkoutExerciseVO>> findAll(@PageableDefault(sort = "name", size = 20) final Pageable pageable) {
        Page<WorkoutExerciseVO> workoutExerciseVOList = service.findAll(pageable);
        return ResponseEntity.ok(workoutExerciseVOList);
    }

    @Override
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a Workout exercise list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation, the Workout exercise was deleted",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Will throw a custom exception: Workout Exercise Not Found Exception"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    public ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
