package com.kaankarakas.librarymanagement.service.auth;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.enums.UserRole;
import com.kaankarakas.librarymanagement.repository.user.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static com.kaankarakas.librarymanagement.validator.user.UserServiceValidationRule.ERR_USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class CustomUserDetailServiceTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ROLE_USER = "ROLE_PATRON";
    private static final UserRole USER_ROLE = UserRole.PATRON;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    private User prepareUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setRole(USER_ROLE);
        return user;
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // Arrange
        User user = prepareUser();
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(USERNAME);

        // Assert
        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ROLE_USER)));
    }

    @Test
    void loadUserByUsername_ShouldThrowLibraryException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> customUserDetailsService.loadUserByUsername(USERNAME));

        // Assert
        assertEquals(ERR_USER_NOT_FOUND.getDescription(), exception.getMessage());
    }
}
