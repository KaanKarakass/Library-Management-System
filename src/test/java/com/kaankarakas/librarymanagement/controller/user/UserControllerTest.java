package com.kaankarakas.librarymanagement.controller.user;

import com.kaankarakas.librarymanagement.api.controller.user.UserController;
import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.request.user.RegisterUserRequest;
import com.kaankarakas.librarymanagement.dto.request.user.UpdateUserRequest;
import com.kaankarakas.librarymanagement.dto.response.user.UserDTO;
import com.kaankarakas.librarymanagement.enums.UserRole;
import com.kaankarakas.librarymanagement.enums.UserStatus;
import com.kaankarakas.librarymanagement.service.user.UserCommandService;
import com.kaankarakas.librarymanagement.service.user.UserQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    protected static final long USER_ID = 1L;
    protected static final String USERNAME = "testUsername";
    protected static final String NAME = "testName";
    protected static final String EMAIL = "test@test.com";
    protected static final String PASSWORD = "password";
    protected static final UserRole ROLE = UserRole.LIBRARIAN;
    protected static final UserStatus STATUS = UserStatus.ACTIVE;

    @Mock
    private UserCommandService userCommandService;

    @Mock
    private UserQueryService userQueryService;

    private UserController userController;


    private UserDTO prepareUserDTO() {
        UserDTO user = new UserDTO();
        user.setId(USER_ID);
        user.setUsername(USERNAME);
        user.setName(NAME);
        user.setEmail(EMAIL);
        user.setRole(ROLE);
        user.setUserStatus(STATUS);
        return user;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userCommandService, userQueryService);
    }

    @Test
    void registerUser_ShouldCallServiceAndReturnDto() {
        // Arrange
        RegisterUserRequest request = prepareRegisterUserRequest();
        UserDTO mockResponse = prepareUserDTO();
        when(userCommandService.registerUser(request)).thenReturn(mockResponse);

        // Act
        UserDTO response = userController.registerUser(request);

        // Assert
        verify(userCommandService).registerUser(request);
        assertEquals(mockResponse, response);
    }

    @Test
    void getUserById_ShouldCallServiceAndReturnDto() {
        // Arrange
        UserDTO mockResponse = prepareUserDTO();
        when(userQueryService.findUserById(USER_ID)).thenReturn(mockResponse);

        // Act
        UserDTO response = userController.getUserById(USER_ID);

        // Assert
        verify(userQueryService).findUserById(USER_ID);
        assertEquals(mockResponse, response);
    }

    @Test
    void updateUser_ShouldCallServiceAndReturnDto() {
        // Arrange
        UpdateUserRequest request = prepareUpdateUserRequest();
        UserDTO mockResponse = prepareUserDTO();
        when(userCommandService.updateUser(USER_ID, request)).thenReturn(mockResponse);

        // Act
        UserDTO response = userController.updateUser(USER_ID, request);

        // Assert
        verify(userCommandService).updateUser(USER_ID, request);
        assertEquals(mockResponse, response);
    }

    @Test
    void deleteUser_ShouldCallServiceAndReturnDto() {
        // Arrange
        UserDTO mockResponse = prepareUserDTO();
        when(userCommandService.deleteUser(USER_ID)).thenReturn(mockResponse);

        // Act
        UserDTO response = userController.deleteUser(USER_ID);

        // Assert
        verify(userCommandService).deleteUser(USER_ID);
        assertEquals(mockResponse, response);
    }
}
