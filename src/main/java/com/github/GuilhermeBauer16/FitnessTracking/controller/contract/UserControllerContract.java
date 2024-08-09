package com.github.GuilhermeBauer16.FitnessTracking.controller.contract;

import com.github.GuilhermeBauer16.FitnessTracking.exception.EmailAlreadyRegisterException;
import com.github.GuilhermeBauer16.FitnessTracking.exception.UserNotFoundException;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.TokenVO;
import com.github.GuilhermeBauer16.FitnessTracking.model.values.UserVO;
import com.github.GuilhermeBauer16.FitnessTracking.request.LoginRequest;
import com.github.GuilhermeBauer16.FitnessTracking.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserControllerContract {



    /**
     * Registers a new user.
     * <p>
     * This method processes a POST request to register a new user. It consumes a JSON payload
     * representing the user details and returns a response with the created user.
     * </p>
     *
     * @param userVO the user details to be registered
     * @return a {@link ResponseEntity} containing the created {@link UserResponse} and HTTP status code
     * @throws  EmailAlreadyRegisterException if the email is already registered
     * @throws UserNotFoundException or FieldNotFoundException if required fields are not found

     */

    @PostMapping(value="/signUp", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a new user", description = "Registers a new user and returns the created user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation, user created.",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, email already registered.",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "User not found or field not found.",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<UserResponse> create(@RequestBody UserVO userVO);

    /**
     * Logs in a user.
     * <p>
     * This method processes a POST request to authenticate a user. It consumes a JSON payload
     * containing login credentials and returns a response with the authentication token.
     * </p>
     *
     * @param loginRequest the login credentials
     * @return a {@link ResponseEntity} containing the {@link TokenVO} with the authentication token
     * @throws UserNotFoundException or FieldNotFoundException if required fields are not found

     */

    @PostMapping(value="/login", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Log in a user", description = "Authenticates a user and returns an authentication token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation, user authenticated.",
                    content = @Content(schema = @Schema(implementation = TokenVO.class))),
            @ApiResponse(responseCode = "404", description = "User not found or field not found.",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<TokenVO> login(@RequestBody LoginRequest loginRequest);

    /**
     * Retrieves a user by email.
     * <p>
     * This method processes a GET request to fetch user details based on the email address.
     * </p>
     *
     * @param email the email address of the user to retrieve
     * @return a {@link ResponseEntity} containing the {@link UserResponse} with the user details
     * @throws UserNotFoundException or FieldNotFoundException if the user is not found or field is missing

     */

    @GetMapping(value = "/{email}")
    @Operation(summary = "Get a single user", description = "Returns user details for the specified email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation, user retrieved.",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found or field not found.",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<UserResponse> findUserByEmail(@PathVariable("email") String email);

    /**
     * Deletes a user by email.
     * <p>
     * This method processes a DELETE request to remove a user based on the email address.
     * </p>
     *
     * @param email the email address of the user to delete
     * @return a {@link ResponseEntity} with HTTP status code 204 if successful
     * @throws UserNotFoundException if the user is not found

     */

    @DeleteMapping(value = "/{email}")
    @Operation(summary = "Delete a user", description = "Deletes the user for the specified email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation, user deleted.",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "User not found.",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal server error.",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<Void> delete(@PathVariable("email") String email);

}
