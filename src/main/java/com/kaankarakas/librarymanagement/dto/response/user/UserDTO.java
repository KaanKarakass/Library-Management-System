package com.kaankarakas.librarymanagement.dto.response.user;

import com.kaankarakas.librarymanagement.enums.UserRole;
import com.kaankarakas.librarymanagement.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserDTO {
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Full name of the user", example = "John Doe")
    private String name;

    @Schema(description = "Username used for login", example = "johndoe")
    private String username;

    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Role of the user", example = "PATRON")
    private UserRole role;

    @Schema(description = "Status of the user", example = "ACTIVE")
    private UserStatus userStatus;
}
