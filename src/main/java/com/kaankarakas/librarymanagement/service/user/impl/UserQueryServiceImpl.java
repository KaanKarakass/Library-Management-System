package com.kaankarakas.librarymanagement.service.user.impl;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.dto.response.user.UserDTO;
import com.kaankarakas.librarymanagement.mapper.user.UserMapper;
import com.kaankarakas.librarymanagement.repository.user.UserRepository;
import com.kaankarakas.librarymanagement.service.user.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.kaankarakas.librarymanagement.validator.user.UserServiceValidationRule.ERR_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO findUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> {
                    log.error("User with id: {} not found", id);
                    return new LibraryException(ERR_USER_NOT_FOUND.getDescription(), HttpStatus.NOT_FOUND);
                });
    }
}
