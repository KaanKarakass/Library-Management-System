package com.kaankarakas.librarymanagement.unit.service.user;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.response.user.UserDTO;
import com.kaankarakas.librarymanagement.service.user.UserQueryService;
import com.kaankarakas.librarymanagement.service.user.impl.UserQueryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.kaankarakas.librarymanagement.validator.user.UserServiceValidationRule.ERR_USER_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserQueryServiceTest extends UserServiceTestBase {

    @Mock
    private UserQueryService userQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userQueryService = new UserQueryServiceImpl(userRepository, userMapper);
    }

    @Test
    void findUserById_ShouldReturnUserDTO_WhenUserExists() {
        // Arrange
        User user = prepareUser();
        UserDTO userDTO = prepareUserDTO();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // Act
        UserDTO result = userQueryService.findUserById(USER_ID);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(USER_ID);
        assertThat(result.getUsername()).isEqualTo(USERNAME);
        assertThat(result.getEmail()).isEqualTo(EMAIL);

        verify(userRepository).findById(USER_ID);
        verify(userMapper).toDTO(user);
    }

    @Test
    void findUserById_ShouldThrowLibraryException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(SECOND_USER_ID)).thenReturn(Optional.empty());

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> userQueryService.findUserById(SECOND_USER_ID));

        // Assert
        assertThat(exception.getMessage()).isEqualTo(ERR_USER_NOT_FOUND.getDescription());
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(userRepository).findById(SECOND_USER_ID);
        verifyNoInteractions(userMapper);
    }
}
