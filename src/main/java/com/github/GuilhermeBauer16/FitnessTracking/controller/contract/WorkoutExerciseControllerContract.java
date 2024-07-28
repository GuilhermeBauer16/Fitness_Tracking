package com.github.GuilhermeBauer16.FitnessTracking.controller.contract;

import com.github.GuilhermeBauer16.FitnessTracking.model.values.WorkoutExerciseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface WorkoutExerciseControllerContract<T, I> {

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
    ResponseEntity<T> create(@RequestBody T target);

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
    ResponseEntity<T> update(@RequestBody T target);

    @GetMapping(value = "/{id}")

    @Operation(summary = "Get a single Workout exercise", description = "Returns a Workout exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = WorkoutExerciseVO.class))),
            @ApiResponse(responseCode = "404", description = "Will throw a custom exception: Workout Exercise Not Found Exception"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    ResponseEntity<T> findById(I id);

    @GetMapping("/muscleGroup")
    ResponseEntity<List<T>> findByMuscleGroup(@RequestBody T t);

    @GetMapping("/difficultLevel")
    ResponseEntity<List<T>> findByDifficultLevel(@RequestBody T t);

    @GetMapping
    @Operation(summary = "Get a Workout exercise list", description = "Returns a list of Workout exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = WorkoutExerciseVO.class))),
            @ApiResponse(responseCode = "404", description = "Will throw a custom exception: Workout Exercise Not Found Exception"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    ResponseEntity<Page<T>> findAll(final Pageable pageable);


    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a Workout exercise list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation, the Workout exercise was deleted",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Will throw a custom exception: Workout Exercise Not Found Exception"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    ResponseEntity<?> delete(I id);
}
