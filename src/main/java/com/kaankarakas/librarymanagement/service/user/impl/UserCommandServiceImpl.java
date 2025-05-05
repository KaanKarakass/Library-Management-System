package com.kaankarakas.librarymanagement.service.user.impl;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.request.user.RegisterUserRequest;
import com.kaankarakas.librarymanagement.dto.request.user.UpdateUserRequest;
import com.kaankarakas.librarymanagement.dto.response.user.UserDTO;
import com.kaankarakas.librarymanagement.enums.UserRole;
import com.kaankarakas.librarymanagement.enums.UserStatus;
import com.kaankarakas.librarymanagement.mapper.user.UserMapper;
import com.kaankarakas.librarymanagement.repository.user.UserRepository;
import com.kaankarakas.librarymanagement.service.user.UserCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.kaankarakas.librarymanagement.validator.user.UserServiceValidationRule.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private static final int MIN_PASSWORD_LENGTH = 8;

    @Override
    public UserDTO registerUser(RegisterUserRequest registerUserRequest) {
        log.info("Registering new user with email: {}", registerUserRequest.getEmail());
        checkEmail(registerUserRequest.getEmail());
        checkUsername(registerUserRequest.getUsername());
        checkPassword(registerUserRequest.getPassword());
        checkUserRole(registerUserRequest.getRole());
        return userMapper.toDTO(userRepository.save(prepareUser(registerUserRequest)));
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserRequest updateUserRequest) {
        log.info("Updating user with id: {}", id);
        User user = checkUserId(id);

        if (!user.getUsername().equals(updateUserRequest.getUsername())) {
            checkUsername(updateUserRequest.getUsername());
        }

        if (!user.getEmail().equals(updateUserRequest.getEmail())) {
            checkEmail(updateUserRequest.getEmail());
        }

        userMapper.updateUserFromDto(updateUserRequest, user);

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        User user = checkUserId(id);
        if (user.getUserStatus() == UserStatus.DELETED) {
            log.error("User with id: {} is already deleted", id);
            throw new LibraryException(ERR_USER_ALREADY_DELETED.getDescription(), HttpStatus.BAD_REQUEST);
        }
        user.setUserStatus(UserStatus.DELETED);
        return userMapper.toDTO(userRepository.save(user));
    }

    private User checkUserId(Long id){
        log.debug("Checking if user with id: {} exists.", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new LibraryException(ERR_USER_NOT_FOUND.getDescription(), HttpStatus.NOT_FOUND));
    }

    private User prepareUser(RegisterUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(hashPassword(request.getPassword()));
        user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        return user;
    }

    private String hashPassword(String password) {
        log.debug("Hashing password for user");
        return passwordEncoder.encode(password);
    }

    private void checkEmail(String email) {
        log.debug("Checking if email already exists: {}", email);
        if (userRepository.existsByEmail(email)) {
            log.error("Email already exists: {}", email);
            throw new LibraryException(ERR_EMAIL_ALREADY_EXISTS.getDescription(), HttpStatus.BAD_REQUEST);
        }
    }

    private void checkUsername(String username) {
        log.debug("Checking if username already exists: {}", username);
        if (userRepository.existsByUsername(username)) {
            log.error("Username already exists: {}", username);
            throw new LibraryException(ERR_USERNAME_ALREADY_EXISTS.getDescription(), HttpStatus.BAD_REQUEST);
        }
    }

    private void checkPassword(String password) {
        log.debug("Checking password validity");
        if (password.length() < MIN_PASSWORD_LENGTH) {
            log.error("Password is too short: {} characters", password.length());
            throw new LibraryException(ERR_INVALID_PASSWORD.getDescription(), HttpStatus.BAD_REQUEST);
        }
    }

    private void checkUserRole(String userRole) {
        log.debug("Checking if user role is valid: {}", userRole);
        try {
            UserRole.valueOf(userRole.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid user role: {}", userRole);
            throw new LibraryException(ERR_INVALID_ROLE.getDescription(), HttpStatus.BAD_REQUEST);
        }
    }


}
