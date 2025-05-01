package com.kaankarakas.librarymanagement.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Size(max = 50)
    @Schema(description = "Full name of the user", example = "John Doe")
    private String name;

    @Email
    @Size(max = 100)
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @Size(max = 20)
    @Schema(description = "Unique username for login", example = "johndoe")
    private String username;

    @Schema(description = "Role of the user (e.g., LIBRARIAN, PATRON)", example = "PATRON")
    private String role;

    @Schema(description = "Status of the user (e.g., ACTIVE, DELETED)", example = "ACTIVE")
    private String userStatus;
}
