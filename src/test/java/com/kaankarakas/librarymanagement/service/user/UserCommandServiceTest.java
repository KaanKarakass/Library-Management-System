package com.kaankarakas.librarymanagement.service.user;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.request.user.RegisterUserRequest;
import com.kaankarakas.librarymanagement.dto.request.user.UpdateUserRequest;
import com.kaankarakas.librarymanagement.dto.response.user.UserDTO;
import com.kaankarakas.librarymanagement.service.user.impl.UserCommandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.kaankarakas.librarymanagement.validator.user.UserServiceValidationRule.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserCommandServiceTest extends UserServiceTestBase {

    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String INVALID_PASSWORD = "123";
    private static final String INVALID_ROLE = "Role";
    private static final String UPDATED_USERNAME = "updatedUsername";
    private static final String UPDATED_EMAIL = "updatedEmail";

    @Mock
    private UserCommandService userCommandService;

    @Mock
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userCommandService = new UserCommandServiceImpl(userRepository, passwordEncoder, userMapper);
    }

    private RegisterUserRequest prepareRegisterUserRequest() {
        return RegisterUserRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .name(NAME)
                .role(ROLE.name())
                .build();
    }

    private UpdateUserRequest prepareUpdateUserRequest() {
        return UpdateUserRequest.builder()
                .email(EMAIL)
                .username(USERNAME)
                .name(NAME)
                .role(ROLE.name())
                .build();
    }

    @Test
    void registerUser_ShouldSucceed_WhenRequestIsValid() {
        // Arrange
        User user = prepareUser();
        UserDTO userDTO = prepareUserDTO();
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // Act
        UserDTO result = userCommandService.registerUser(prepareRegisterUserRequest());

        // Assert
        assertNotNull(result);
        assertEquals(EMAIL, result.getEmail());
        assertEquals(USERNAME, result.getUsername());
        assertEquals(NAME, result.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowLibraryException_WhenEmailExists() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> userCommandService.registerUser(prepareRegisterUserRequest()));

        // Assert
        assertEquals(ERR_EMAIL_ALREADY_EXISTS.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void registerUser_ShouldThrowLibraryException_WhenUsernameExists() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> userCommandService.registerUser(prepareRegisterUserRequest()));

        // Assert
        assertEquals(ERR_USERNAME_ALREADY_EXISTS.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void registerUser_ShouldThrowLibraryException_WhenPasswordTooShort() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        RegisterUserRequest request = prepareRegisterUserRequest();
        request.setPassword(INVALID_PASSWORD);
        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> userCommandService.registerUser(request));

        // Assert
        assertEquals(ERR_INVALID_PASSWORD.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void registerUser_ShouldThrowLibraryException_WhenUserRoleInvalid() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        RegisterUserRequest request = prepareRegisterUserRequest();
        request.setRole(INVALID_ROLE);
        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> userCommandService.registerUser(request));

        // Assert
        assertEquals(ERR_INVALID_ROLE.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void updateUser_ShouldSucceed_WhenDataIsValid() {
        // Arrange
        User user = prepareUser();
        UserDTO userDTO = prepareUserDTO();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // Act
        UserDTO result = userCommandService.updateUser(USER_ID, prepareUpdateUserRequest());

        // Assert
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowLibraryException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> userCommandService.updateUser(USER_ID, prepareUpdateUserRequest()));

        // Assert
        assertEquals(ERR_USER_NOT_FOUND.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void updateUser_ShouldThrowLibraryException_WhenUsernameAlreadyExists() {
        // Arrange
        User user = prepareUser();
        UpdateUserRequest updateUserRequest = prepareUpdateUserRequest();
        updateUserRequest.setUsername(UPDATED_USERNAME);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(UPDATED_USERNAME)).thenReturn(true);

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> userCommandService.updateUser(USER_ID, updateUserRequest));

        // Assert
        assertEquals(ERR_USERNAME_ALREADY_EXISTS.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void updateUser_ShouldThrowLibraryException_WhenEmailAlreadyExists() {
        // Arrange
        User existingUser = prepareUser();
        UpdateUserRequest updateUserRequest = prepareUpdateUserRequest();
        updateUserRequest.setEmail(UPDATED_EMAIL);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(UPDATED_EMAIL)).thenReturn(true);

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> userCommandService.updateUser(USER_ID, updateUserRequest));

        // Assert
        assertEquals(ERR_EMAIL_ALREADY_EXISTS.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void deleteUser_ShouldSucceed_WhenUserExists() {
        // Arrange
        User user = prepareUser();
        UserDTO userDTO = prepareUserDTO();
        userDTO.setUserStatus(DELETED_STATUS);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // Act
        UserDTO result = userCommandService.deleteUser(USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(DELETED_STATUS, result.getUserStatus());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldThrowLibraryException_WhenUserAlreadyDeleted() {
        // Arrange
        User user = prepareUser();
        user.setUserStatus(DELETED_STATUS);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> userCommandService.deleteUser(USER_ID));

        // Assert
        assertEquals(ERR_USER_ALREADY_DELETED.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void deleteUser_ShouldThrowLibraryException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> userCommandService.deleteUser(USER_ID));

        // Assert
        assertEquals(ERR_USER_NOT_FOUND.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }


}
