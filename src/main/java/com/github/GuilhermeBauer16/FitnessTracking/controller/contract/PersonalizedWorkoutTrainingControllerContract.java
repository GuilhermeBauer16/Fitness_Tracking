package com.github.GuilhermeBauer16.FitnessTracking.controller.contract;

import com.github.GuilhermeBauer16.FitnessTracking.model.values.PersonalizedWorkoutTrainingVO;
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

public interface PersonalizedWorkoutTrainingControllerContract<T, I> {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(summary = "Register a new Personalized workout training", description = "That method will use a POST to " +
            "Returns a created Personalized workout training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = PersonalizedWorkoutTrainingVO.class))),
            @ApiResponse(responseCode = "404", description = "Will throw a custom exception: Personalized Workout Training Not Found" +
                    " or Field Not Found Exception, depending of the situation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    ResponseEntity<T> create(@RequestBody T target);

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @Operation(summary = "Update a Personalized workout training", description = "Returns a updated Workout exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = PersonalizedWorkoutTrainingVO.class))),
            @ApiResponse(responseCode = "404", description = "Will throw a custom exception: Personalized Workout Training Not Found" +
                    " or Field Not Found Exception, depending of the situation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    ResponseEntity<T> update(@RequestBody T target);

    @GetMapping(value = "/{id}")

    @Operation(summary = "Get a single Personalized workout training", description = "Returns a Workout exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = PersonalizedWorkoutTrainingVO.class))),
            @ApiResponse(responseCode = "404", description = "Will throw a custom exception: Personalized Workout Training Not Found" +
                    " or Field Not Found Exception, depending of the situation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    ResponseEntity<T> findById(@PathVariable(value = "id") I id);

    @GetMapping
    @Operation(summary = "Get a Personalized workout training list", description = "Returns a list of Workout exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = PersonalizedWorkoutTrainingVO.class))),
            @ApiResponse(responseCode = "404", description = "Will throw a custom exception: Personalized Workout Training Not Found" +
                    " or Field Not Found Exception, depending of the situation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    ResponseEntity<Page<T>> findAll(final Pageable pageable);


    @GetMapping("/findByMuscleGroup")
    @Operation(summary = "Get a Personalized workout training list filter by muscle group", description = "Returns a list of Personalized workout training" +
            "with the specific muscle group that the user choose to filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = PersonalizedWorkoutTrainingVO.class))),

    })
    ResponseEntity<List<T>> findPersonalizedWorkoutTrainingByMuscleGroup(@RequestBody T t);

    @GetMapping("/findByDifficultLevel")
    @Operation(summary = "Get a Personalized workout training list filter by difficult level", description = "Returns a list of Personalized workout training" +
            "with the specific difficult level of the user choose to filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = PersonalizedWorkoutTrainingVO.class))),

    })
    ResponseEntity<List<T>> findPersonalizedWorkoutTrainingByDifficultLevel(@RequestBody T t);

    @GetMapping("/findByName")

    @Operation(summary = "Get a Personalized workout training list filter by name", description = "Returns a list of Personalized workout training" +
            "with the specific name of the user choose to filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = PersonalizedWorkoutTrainingVO.class))),

    })
    ResponseEntity<List<T>> findPersonalizedWorkoutTrainingByName(@RequestBody T t);


    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a Personalized workout training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation, the Personalized workout training was deleted",
                    content = @Content(schema = @Schema(implementation = PersonalizedWorkoutTrainingVO.class))),
            @ApiResponse(responseCode = "404", description = "Will throw a custom exception: Personalized Workout Training Not Found" +
                    " or Field Not Found Exception, depending of the situation"),
            @ApiResponse(responseCode = "500", description = "Internal server error")

    })
    ResponseEntity<?> delete(@PathVariable(value = "id") I id);
}
