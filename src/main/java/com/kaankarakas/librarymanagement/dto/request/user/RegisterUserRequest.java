package com.kaankarakas.librarymanagement.dto.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import static com.kaankarakas.librarymanagement.constants.LibraryManagementDefinitionConstants.*;

@Data
@Builder
public class RegisterUserRequest {
    @Schema(description = "Full name of the user", example = "John Doe", maxLength = NAME_MAX_LENGTH)
    @Size(max = NAME_MAX_LENGTH)
    @NotNull
    private String name;

    @Schema(description = "Username to be used for login", example = "johndoe", maxLength = SHORT_NAME_MAX_LENGTH)
    @Size(max = SHORT_NAME_MAX_LENGTH)
    @NotNull
    private String username;

    @Schema(description = "Email address of the user", example = "john.doe@example.com", maxLength = EMAIL_MAX_LENGTH)
    @Size(max = EMAIL_MAX_LENGTH)
    @NotNull
    @Email
    private String email;

    @Schema(description = "Password of the user", example = "securePassword123", maxLength = CODE_MAX_LENGTH)
    @Size(max = CODE_MAX_LENGTH)
    @NotNull
    private String password;

    @Schema(description = "Role of the user", example = "PATRON", allowableValues = {"PATRON", "LIBRARIAN"})
    @NotNull
    private String role;
}
