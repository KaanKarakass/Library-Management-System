package com.kaankarakas.librarymanagement.service.user;

import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.response.user.UserDTO;
import com.kaankarakas.librarymanagement.enums.UserRole;
import com.kaankarakas.librarymanagement.enums.UserStatus;
import com.kaankarakas.librarymanagement.mapper.user.UserMapper;
import com.kaankarakas.librarymanagement.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public abstract class UserServiceTestBase {

    protected static final long USER_ID = 1L;
    protected static final long SECOND_USER_ID = 2L;
    protected static final String USERNAME = "testUsername";
    protected static final String NAME = "testName";
    protected static final String EMAIL = "test@test.com";
    protected static final String PASSWORD = "password";
    protected static final UserRole ROLE = UserRole.LIBRARIAN;
    protected static final UserStatus STATUS = UserStatus.ACTIVE;
    protected static final UserStatus DELETED_STATUS = UserStatus.DELETED;

    @Mock
    protected UserRepository userRepository;

    @Mock
    protected UserMapper userMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    protected User prepareUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setUsername(USERNAME);
        user.setName(NAME);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        user.setRole(ROLE);
        user.setUserStatus(STATUS);
        return user;
    }

    protected UserDTO prepareUserDTO() {
        UserDTO user = new UserDTO();
        user.setId(USER_ID);
        user.setUsername(USERNAME);
        user.setName(NAME);
        user.setEmail(EMAIL);
        user.setRole(ROLE);
        user.setUserStatus(STATUS);
        return user;
    }
}
