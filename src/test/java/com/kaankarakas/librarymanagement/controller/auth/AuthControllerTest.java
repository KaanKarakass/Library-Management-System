package com.kaankarakas.librarymanagement.controller.auth;

import com.kaankarakas.librarymanagement.api.controller.auth.AuthController;
import com.kaankarakas.librarymanagement.dto.request.auth.AuthRequest;
import com.kaankarakas.librarymanagement.dto.response.auth.AuthResponse;
import com.kaankarakas.librarymanagement.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    private static final String USERNAME = "username";
    protected static final String PASSWORD = "password";
    protected static final String TOKEN = "token";

    @Mock
    private AuthService authService;

    AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authService);
    }

    private AuthRequest prepareAuthRequest() {
        return new AuthRequest(USERNAME, PASSWORD);
    }

    private AuthResponse prepareAuthResponse() {
        return new AuthResponse(TOKEN);
    }

    @Test
    void login_ShouldReturnOk_WhenRequestIsValid() {
        // Arrange
        AuthRequest request = prepareAuthRequest();

        when(authService.login(any(AuthRequest.class))).thenReturn(prepareAuthResponse());

        // Act
        ResponseEntity<?> response = authController.login(request);

        // Assert
        verify(authService).login(request);
        assertNotNull(response.getBody());
        assertInstanceOf(AuthResponse.class, response.getBody());
        AuthResponse authResponse = (AuthResponse) response.getBody();
        assertEquals(TOKEN, authResponse.token());
    }

}
