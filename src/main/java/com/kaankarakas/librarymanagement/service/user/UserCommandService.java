package com.kaankarakas.librarymanagement.service.user;

import com.kaankarakas.librarymanagement.dto.request.user.RegisterUserRequest;
import com.kaankarakas.librarymanagement.dto.request.user.UpdateUserRequest;
import com.kaankarakas.librarymanagement.dto.response.user.UserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserCommandService {
    UserDTO registerUser(@NotNull @Valid RegisterUserRequest registerUserRequest);

    UserDTO updateUser(@NotNull Long id, @NotNull @Valid UpdateUserRequest updateUserRequest);

    UserDTO deleteUser(@NotNull Long id);
}
