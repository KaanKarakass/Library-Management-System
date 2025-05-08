package com.kaankarakas.librarymanagement.integration.user;

import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.request.user.RegisterUserRequest;
import com.kaankarakas.librarymanagement.dto.request.user.UpdateUserRequest;
import com.kaankarakas.librarymanagement.dto.response.user.UserDTO;
import com.kaankarakas.librarymanagement.enums.UserRole;
import com.kaankarakas.librarymanagement.enums.UserStatus;
import com.kaankarakas.librarymanagement.integration.base.BaseIntegrationTest;
import com.kaankarakas.librarymanagement.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiIntegrationTest extends BaseIntegrationTest {

    private static final String USER_URL = "/api/users";
    private static final String REGISTER_URL = USER_URL + "/register";
    private static final String UPDATE_URL = USER_URL + "/update";
    private static final String DELETE_URL = USER_URL + "/delete";

    private static final String USERNAME = "integrationUser";
    private static final String DEFINED_USERNAME = "alice";
    private static final int USER_ID = 1;
    private static final int INVALID_USER_ID = 999999;
    private static final String NAME = "integrationUser";
    private static final String UPDATED_USERNAME = "updatedUser";
    private static final String EMAIL = "user@example.com";
    private static final String UPDATED_EMAIL = "updated@example.com";
    private static final String PASSWORD = "password123";
    private static final UserRole USER_ROLE = UserRole.PATRON;
    private static final UserStatus DELETED_USER_STATUS = UserStatus.DELETED;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private RegisterUserRequest prepareRegisterUserRequest() {
        return RegisterUserRequest.builder()
                .name(NAME)
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .role(USER_ROLE.name())
                .build();
    }

    @Test
    void registerUser_Success() {
        // Arrange
        RegisterUserRequest request = prepareRegisterUserRequest();
        HttpEntity<RegisterUserRequest> httpEntity = new HttpEntity<>(request, new HttpHeaders());

        // Act
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                URI.create(url(REGISTER_URL)), HttpMethod.POST, httpEntity, UserDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(USERNAME);

        // DB Assert
        List<User> all = userRepository.findAll();
        Assertions.assertThat(all).anyMatch(user -> USERNAME.equals(user.getUsername()));
    }

    @Test
    void registerUser_AlreadyExist() {
        // Arrange
        RegisterUserRequest request = prepareRegisterUserRequest();
        request.setUsername(DEFINED_USERNAME);
        HttpEntity<RegisterUserRequest> httpEntity = new HttpEntity<>(request, new HttpHeaders());

        // Act
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                URI.create(url(REGISTER_URL)), HttpMethod.POST, httpEntity, UserDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private UpdateUserRequest prepareUpdateUserRequest() {
        return UpdateUserRequest.builder().username(UPDATED_USERNAME).email(UPDATED_EMAIL).build();
    }

    @Test
    @DirtiesContext
    void updateUser_Success() {
        // Arrange
        UpdateUserRequest request = prepareUpdateUserRequest();
        HttpEntity<UpdateUserRequest> httpEntity = new HttpEntity<>(request, jsonHeaders(librarianToken));

        // Act
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                URI.create(url(UPDATE_URL + "/" + USER_ID)), HttpMethod.PUT, httpEntity, UserDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(response.getBody().getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    void updateUser_NotFound() {
        // Arrange
        UpdateUserRequest request = prepareUpdateUserRequest();
        HttpEntity<UpdateUserRequest> httpEntity = new HttpEntity<>(request, jsonHeaders(librarianToken));

        // Act
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                URI.create(url(UPDATE_URL + "/" + INVALID_USER_ID)), HttpMethod.PUT, httpEntity, UserDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    @Test
    @DirtiesContext
    void deleteUser_Success() {
        // Arrange
        HttpEntity<Object> request = new HttpEntity<>(jsonHeaders(librarianToken));

        // Act
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                URI.create(url(DELETE_URL + "/" + USER_ID)), HttpMethod.DELETE, request, UserDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getUserStatus()).isEqualTo(DELETED_USER_STATUS);
    }

    @Test
    void deleteUser_NotFound() {
        // Arrange
        HttpEntity<Object> request = new HttpEntity<>(jsonHeaders(librarianToken));

        // Act
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                URI.create(url(DELETE_URL + "/" + INVALID_USER_ID)), HttpMethod.DELETE, request, UserDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getUserById_Success() {
        // Arrange
        HttpEntity<Object> request = new HttpEntity<>(jsonHeaders(librarianToken));

        // Act
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                URI.create(url(USER_URL + "/" + USER_ID)), HttpMethod.GET, request, UserDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(DEFINED_USERNAME);
    }

    @Test
    void getUserById_NotFound() {
        // Arrange
        HttpEntity<Object> request = new HttpEntity<>(jsonHeaders(librarianToken));

        // Act
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                URI.create(url(USER_URL + "/" + INVALID_USER_ID)), HttpMethod.GET, request, UserDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
