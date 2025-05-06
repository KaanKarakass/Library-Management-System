package com.kaankarakas.librarymanagement.service.borrowhistory;

import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.domain.borrowhistory.BorrowHistory;
import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.response.borrowhistory.BorrowHistoryDTO;
import com.kaankarakas.librarymanagement.enums.BookStatus;
import com.kaankarakas.librarymanagement.enums.Genre;
import com.kaankarakas.librarymanagement.enums.UserRole;
import com.kaankarakas.librarymanagement.enums.UserStatus;
import com.kaankarakas.librarymanagement.mapper.borrowhistory.BorrowHistoryMapper;
import com.kaankarakas.librarymanagement.repository.barrowhistory.BorrowHistoryRepository;
import com.kaankarakas.librarymanagement.repository.book.BookRepository;
import com.kaankarakas.librarymanagement.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

public abstract class BorrowHistoryServiceTestBase {

    protected static final String USERNAME = "username";
    protected static final long USER_ID = 1L;
    protected static final long SECOND_USER_ID = 2L;
    protected static final long BORROW_ID = 1L;
    protected static final String NAME = "testName";
    protected static final String EMAIL = "test@test.com";
    protected static final String PASSWORD = "password";
    protected static final UserRole ROLE = UserRole.LIBRARIAN;
    protected static final UserStatus STATUS = UserStatus.ACTIVE;
    protected static final UserStatus DELETED_STATUS = UserStatus.DELETED;
    protected static final long BOOK_ID = 1L;
    protected static final String BOOK_TITLE = "Test Book";
    protected static final String BOOK_AUTHOR = "Test Author";
    protected static final String BOOK_ISBN = "1231234123";
    protected static final BookStatus BOOK_STATUS = BookStatus.ACTIVE;
    protected static final LocalDate PUBLICATION_DATE = LocalDate.of(2025, 5, 5);
    protected static final Genre GENRE = Genre.BIOGRAPHY;
    protected static final LocalDate BORROW_DATE = LocalDate.of(2020, 2, 2);
    protected static final LocalDate DUE_DATE = LocalDate.of(2020, 2, 20);
    protected static final LocalDate RETURN_DATE = LocalDate.of(2020, 2, 20);

    @Mock
    protected BorrowHistoryRepository historyRepository;

    @Mock
    protected UserRepository userRepository;

    @Mock
    protected BorrowHistoryMapper borrowHistoryMapper;


    @BeforeEach
    void setUp() {
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

    protected Book prepareBook() {
        Book book = new Book();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setAuthor(BOOK_AUTHOR);
        book.setIsbn(BOOK_ISBN);
        book.setPublicationDate(PUBLICATION_DATE);
        book.setGenre(GENRE);
        return book;
    }

    protected BorrowHistory prepareBorrowHistory() {
        BorrowHistory borrowHistory = new BorrowHistory();
        borrowHistory.setUser(prepareUser());
        borrowHistory.setBook(prepareBook());
        borrowHistory.setBorrowDate(BORROW_DATE);
        borrowHistory.setDueDate(DUE_DATE);
        return borrowHistory;
    }

    protected BorrowHistoryDTO prepareBorrowHistoryDTO() {
        BorrowHistoryDTO borrowHistory = new BorrowHistoryDTO();
        borrowHistory.setId(BORROW_ID);
        borrowHistory.setUserId(USER_ID);
        borrowHistory.setBookId(BOOK_ID);
        borrowHistory.setUserName(USERNAME);
        borrowHistory.setBookTitle(BOOK_TITLE);
        borrowHistory.setBorrowDate(BORROW_DATE);
        borrowHistory.setDueDate(DUE_DATE);
        return borrowHistory;
    }
}
