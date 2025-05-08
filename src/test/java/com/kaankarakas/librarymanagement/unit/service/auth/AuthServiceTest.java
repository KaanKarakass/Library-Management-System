package com.kaankarakas.librarymanagement.unit.service.auth;

import com.kaankarakas.librarymanagement.dto.request.auth.AuthRequest;
import com.kaankarakas.librarymanagement.dto.response.auth.AuthResponse;
import com.kaankarakas.librarymanagement.security.JwtUtil;
import com.kaankarakas.librarymanagement.service.auth.AuthService;
import com.kaankarakas.librarymanagement.service.auth.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private static final String USERNAME = "username";
    protected static final String PASSWORD = "password";
    protected static final String TOKEN = "token";
    protected static final String INVALID_CREDENTIALS = "Invalid credentials";

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthService authService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthServiceImpl(authenticationManager, jwtUtil);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn(TOKEN);
    }

    private AuthRequest prepareAuthRequest() {
        return new AuthRequest(USERNAME, PASSWORD);
    }

    private AuthResponse prepareAuthResponse() {
        return new AuthResponse(TOKEN);
    }

    @Test
    void testLogin_ShouldReturnAuthResponse_WhenSuccess() {
        // Arrange
        AuthResponse authResponse = prepareAuthResponse();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Act
        AuthResponse actualAuthResponse = authService.login(prepareAuthRequest());

        // Assert
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(any(UserDetails.class));

        assertEquals(authResponse.token(), actualAuthResponse.token());
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException(INVALID_CREDENTIALS));
        // Act
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(prepareAuthRequest()));

        //Assert
        assertEquals(INVALID_CREDENTIALS, exception.getMessage());
    }

}
