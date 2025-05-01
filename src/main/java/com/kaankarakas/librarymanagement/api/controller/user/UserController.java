package com.kaankarakas.librarymanagement.api.controller.user;

import com.kaankarakas.librarymanagement.api.constants.ApiEndpointConstants;
import com.kaankarakas.librarymanagement.dto.request.user.RegisterUserRequest;
import com.kaankarakas.librarymanagement.dto.request.user.UpdateUserRequest;
import com.kaankarakas.librarymanagement.dto.response.user.UserDTO;
import com.kaankarakas.librarymanagement.service.user.UserCommandService;
import com.kaankarakas.librarymanagement.service.user.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller", description = "User management operations")
@RestController
@RequestMapping(value = ApiEndpointConstants.USER_API, produces = {ApiEndpointConstants.RESPONSE_CONTENT_TYPE}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@RequiredArgsConstructor
public class UserController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user with provided information like username, email, password, and role.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User successfully registered", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))), @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Invalid data\"}")))})
    public UserDTO registerUser(@Valid @RequestBody RegisterUserRequest request) {
        return userCommandService.registerUser(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves detailed information of a user by their ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))), @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"User not found\"}")))})
    public UserDTO getUserById(@PathVariable Long id) {
        return userQueryService.findUserById(id);
    }

    @PutMapping("update/{id}")
    @Operation(summary = "Update user by ID", description = "Updates user details for a given ID. Checks for uniqueness of username and email before updating.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User successfully updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))), @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Invalid data\"}"))), @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"User not found\"}")))})
    public UserDTO updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return userCommandService.updateUser(id, request);
    }

    @DeleteMapping("delete/{id}")
    @Operation(summary = "Soft delete user by ID", description = "Performs a soft delete (marks user as deleted) instead of physically removing them from the database.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User successfully deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))), @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"User not found\"}")))})
    public UserDTO deleteUser(@PathVariable Long id) {
        return userCommandService.deleteUser(id);
    }
}
