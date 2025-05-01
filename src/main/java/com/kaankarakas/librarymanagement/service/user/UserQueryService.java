package com.kaankarakas.librarymanagement.service.user;

import com.kaankarakas.librarymanagement.dto.response.user.UserDTO;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserQueryService {
    UserDTO findUserById(Long id);
}
