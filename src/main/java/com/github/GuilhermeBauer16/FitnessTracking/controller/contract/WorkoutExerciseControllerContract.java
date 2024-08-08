package com.github.GuilhermeBauer16.FitnessTracking.controller.contract;

import com.github.GuilhermeBauer16.FitnessTracking.exception.WorkoutExerciseNotFound;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface WorkoutExerciseControllerContract<T, I> {

    /**
     * Registers a new workout exercise.
     * <p>
     * This method processes a POST request to create a new workout exercise. It consumes a JSON payload
     * representing the workout exercise details and returns a response with the created workout exercise.
     * </p>
     *
     * @param target the workout exercise details to be registered
     * @return a {@link ResponseEntity} containing the created workout exercise of type {@code T} and HTTP status code
     * @throws WorkoutExerciseNotFound or FieldNotFoundException if required fields are not found

     */

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a new Workout exercise", description = "Returns the created Workout exercise.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation, workout exercise created.",
                    content = @Content(schema = @Schema(implementation = WorkoutExerciseVO.class))),
            @ApiResponse(responseCode = "404", description = "Workout exercise not found or field not found.",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<T> create(@RequestBody T target);

    /**
     * Updates an existing workout exercise.
     * <p>
     * This method processes a PUT request to update an existing workout exercise. It consumes a JSON payload
     * representing the updated workout exercise details and returns a response with the updated workout exercise.
     * </p>
     *
     * @param target the updated workout exercise details
     * @return a {@link ResponseEntity} containing the updated workout exercise of type {@code T} and HTTP status code
     * @throws WorkoutExerciseNotFound or FieldNotFoundException if required fields are not found

     */

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a Workout exercise", description = "Returns the updated Workout exercise.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation, workout exercise updated.",
                    content = @Content(schema = @Schema(implementation = WorkoutExerciseVO.class))),
            @ApiResponse(responseCode = "404", description = "Workout exercise not found or field not found.",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<T> update(@RequestBody T target);

    /**
     * Retrieves a workout exercise by its identifier.
     * <p>
     * This method processes a GET request to fetch a workout exercise based on the provided identifier.
     * </p>
     *
     * @param id the identifier of the workout exercise to retrieve
     * @return a {@link ResponseEntity} containing the workout exercise of type {@code T} and HTTP status code
     * @throws WorkoutExerciseNotFound if the workout exercise is not found

     */

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get a single Workout exercise", description = "Returns the Workout exercise for the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation, workout exercise retrieved.",
                    content = @Content(schema = @Schema(implementation = WorkoutExerciseVO.class))),
            @ApiResponse(responseCode = "404", description = "Workout exercise not found.",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<T> findById(I id);

    /**
     * Retrieves a list of workout exercises with pagination.
     * <p>
     * This method processes a GET request to fetch a paginated list of workout exercises.
     * </p>
     *
     * @param pageable pagination information
     * @return a {@link ResponseEntity} containing a {@link Page} of workout exercises of type {@code T} and HTTP status code
     * @throws WorkoutExerciseNotFound if no workout exercises are found

     */

    @GetMapping
    @Operation(summary = "Get a Workout exercise list", description = "Returns a paginated list of Workout exercises.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation, list of workout exercises retrieved.",
                    content = @Content(schema = @Schema(implementation = WorkoutExerciseVO.class))),
            @ApiResponse(responseCode = "404", description = "Workout exercises not found.",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<Page<T>> findAll(final Pageable pageable);

    /**
     * Deletes a workout exercise by its identifier.
     * <p>
     * This method processes a DELETE request to remove a workout exercise based on the provided identifier.
     * </p>
     *
     * @param id the identifier of the workout exercise to delete
     * @return a {@link ResponseEntity} with HTTP status code 204 if successful
     * @throws WorkoutExerciseNotFound if the workout exercise is not found

     */

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a Workout exercise", description = "Deletes the Workout exercise for the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation, workout exercise deleted.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "404", description = "Workout exercise not found.",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<Void> delete(I id);
}
