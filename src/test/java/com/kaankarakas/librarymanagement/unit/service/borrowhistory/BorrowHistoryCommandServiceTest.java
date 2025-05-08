package com.kaankarakas.librarymanagement.unit.service.borrowhistory;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.domain.borrowhistory.BorrowHistory;
import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.request.borrowhistory.BorrowRequestDTO;
import com.kaankarakas.librarymanagement.dto.request.borrowhistory.ReturnRequestDTO;
import com.kaankarakas.librarymanagement.dto.response.borrowhistory.BorrowHistoryDTO;
import com.kaankarakas.librarymanagement.repository.book.BookRepository;
import com.kaankarakas.librarymanagement.security.SecurityUtil;
import com.kaankarakas.librarymanagement.service.borrowhistory.BorrowHistoryCommandService;
import com.kaankarakas.librarymanagement.service.borrowhistory.impl.BorrowHistoryCommandServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static com.kaankarakas.librarymanagement.validator.borrowhistory.BorrowHistoryServiceValidationRule.*;
import static com.kaankarakas.librarymanagement.validator.user.UserServiceValidationRule.ERR_USER_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BorrowHistoryCommandServiceTest extends BorrowHistoryServiceTestBase {
    @Mock
    private BorrowHistoryCommandService service;

    @Mock
    protected BookRepository bookRepository;

    private MockedStatic<SecurityUtil> mockedStatic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentUsername).thenReturn(USERNAME);
        service = new BorrowHistoryCommandServiceImpl(historyRepository, bookRepository, userRepository, borrowHistoryMapper);
    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }

    private BorrowRequestDTO prepareBorrowRequestDTO() {
        BorrowRequestDTO borrowRequestDTO = new BorrowRequestDTO();
        borrowRequestDTO.setBookId(BOOK_ID);
        borrowRequestDTO.setBorrowDate(BORROW_DATE);
        borrowRequestDTO.setDueDate(DUE_DATE);
        return borrowRequestDTO;
    }

    private ReturnRequestDTO prepareReturnRequestDTO() {
        ReturnRequestDTO returnRequestDTO = new ReturnRequestDTO();
        returnRequestDTO.setReturnDate(RETURN_DATE);
        return returnRequestDTO;
    }

    @Test
    void borrowBook_ShouldSaveBorrowHistory_WhenValidRequest() {
        // Arrange
        User user = prepareUser();
        Book book = prepareBook();
        BorrowRequestDTO request = prepareBorrowRequestDTO();

        BorrowHistory history = prepareBorrowHistory();
        BorrowHistoryDTO dto = prepareBorrowHistoryDTO();

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(bookRepository.findByIdAndBookStatus(BOOK_ID, BOOK_STATUS)).thenReturn(Optional.of(book));
        when(historyRepository.save(any())).thenReturn(history);
        when(borrowHistoryMapper.toDTO(history)).thenReturn(dto);

        // Act
        BorrowHistoryDTO result = service.borrowBook(request);

        // Assert
        assertThat(result).isEqualTo(dto);
        verify(bookRepository).save(book);
        verify(historyRepository).save(any());
    }

    @Test
    void borrowBook_shouldThrow_whenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Act
        LibraryException exception = assertThrows(LibraryException.class,
                () -> service.borrowBook(prepareBorrowRequestDTO()));

        // Assert
        assertEquals(ERR_USER_NOT_FOUND.getDescription(), exception.getMessage());
    }

    @Test
    void borrowBook_shouldThrow_whenUserIsNotActive() {
        // Arrange
        User user = prepareUser();
        user.setUserStatus(DELETED_STATUS);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        // Act
        LibraryException ex = assertThrows(LibraryException.class,
                () -> service.borrowBook(prepareBorrowRequestDTO()));

        // Assert
        assertEquals(ERR_USER_NOT_ELIGIBLE.getDescription(), ex.getMessage());
    }

    @Test
    void borrowBook_shouldThrow_whenBookNotAvailable() {
        // Arrange
        User user = prepareUser();

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(bookRepository.findByIdAndBookStatus(BOOK_ID, BOOK_STATUS)).thenReturn(Optional.empty());

        // Act
        LibraryException exception = assertThrows(LibraryException.class,
                () -> service.borrowBook(prepareBorrowRequestDTO()));

        // Assert
        assertEquals(ERR_BOOK_NOT_AVAILABLE.getDescription(), exception.getMessage());
    }

    @Test
    void returnBook_shouldReturnDTO_whenSuccess() {
        // Arrange
        User user = prepareUser();
        BorrowHistory history = prepareBorrowHistory();
        ReturnRequestDTO request = prepareReturnRequestDTO();
        BorrowHistoryDTO dto = prepareBorrowHistoryDTO();
        dto.setIsReturned(true);
        dto.setReturnDate(RETURN_DATE);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(historyRepository.findById(BORROW_ID)).thenReturn(Optional.of(history));
        when(historyRepository.save(any(BorrowHistory.class))).thenReturn(history);
        when(borrowHistoryMapper.toDTO(history)).thenReturn(dto);

        // Act
        BorrowHistoryDTO result = service.returnBook(BORROW_ID, request);

        assertNotNull(result);
        assertTrue(history.getIsReturned());
        assertEquals(RETURN_DATE, result.getReturnDate());
        verify(bookRepository).save(any());
        verify(historyRepository).save(history);
    }

    @Test
    void returnBook_shouldReturnDTO_whenRequestNull() {
        // Arrange
        User user = prepareUser();
        BorrowHistory history = prepareBorrowHistory();
        BorrowHistoryDTO dto = prepareBorrowHistoryDTO();
        ReturnRequestDTO request = prepareReturnRequestDTO();
        request.setReturnDate(null);
        dto.setIsReturned(true);
        dto.setReturnDate(LocalDate.now());

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(historyRepository.findById(BORROW_ID)).thenReturn(Optional.of(history));
        when(historyRepository.save(any(BorrowHistory.class))).thenReturn(history);
        when(borrowHistoryMapper.toDTO(history)).thenReturn(dto);

        // Act
        BorrowHistoryDTO result = service.returnBook(BORROW_ID, request);

        assertNotNull(result);
        assertTrue(history.getIsReturned());
        assertEquals(LocalDate.now(), result.getReturnDate());
        verify(bookRepository).save(any());
        verify(historyRepository).save(history);
    }

    @Test
    void returnBook_shouldThrow_whenUserNotFound() {
        // Arrange
        ReturnRequestDTO returnRequestDTO = prepareReturnRequestDTO();
        returnRequestDTO.setReturnDate(null);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Act
        LibraryException ex = assertThrows(LibraryException.class,
                () -> service.returnBook(BORROW_ID, returnRequestDTO));

        // Assert
        assertEquals(ERR_USER_NOT_FOUND.getDescription(), ex.getMessage());
    }

    @Test
    void returnBook_shouldThrow_whenUserIsNotActive() {
        // Arrange
        ReturnRequestDTO returnRequestDTO = prepareReturnRequestDTO();
        returnRequestDTO.setReturnDate(null);
        User user = prepareUser();
        user.setUserStatus(DELETED_STATUS);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        // Act
        LibraryException ex = assertThrows(LibraryException.class,
                () -> service.returnBook(BORROW_ID, returnRequestDTO));

        assertEquals(ERR_USER_NOT_ELIGIBLE.getDescription(), ex.getMessage());
    }

    @Test
    void returnBook_shouldThrow_whenBorrowHistoryNotFound() {
        // Arrange
        ReturnRequestDTO returnRequestDTO = prepareReturnRequestDTO();
        returnRequestDTO.setReturnDate(null);
        User user = prepareUser();
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(historyRepository.findById(BORROW_ID)).thenReturn(Optional.empty());

        // Act
        LibraryException ex = assertThrows(LibraryException.class,
                () -> service.returnBook(BORROW_ID, returnRequestDTO));

        // Assert
        assertEquals(ERR_BORROW_RECORD_NOT_FOUND.getDescription(), ex.getMessage());
    }

    @Test
    void returnBook_shouldThrow_whenAlreadyReturned() {
        // Arrange
        ReturnRequestDTO returnRequestDTO = prepareReturnRequestDTO();
        returnRequestDTO.setReturnDate(null);
        User user = prepareUser();

        BorrowHistory history = prepareBorrowHistory();
        history.setIsReturned(true);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(historyRepository.findById(BORROW_ID)).thenReturn(Optional.of(history));

        // Act
        LibraryException ex = assertThrows(LibraryException.class,
                () -> service.returnBook(BORROW_ID, returnRequestDTO));

        // Assert
        assertEquals(ERR_BOOK_ALREADY_RETURNED.getDescription(), ex.getMessage());
    }

    @Test
    void returnBook_shouldThrow_whenUserIsNotBorrower() {
        // Assert
        ReturnRequestDTO returnRequestDTO = prepareReturnRequestDTO();
        returnRequestDTO.setReturnDate(null);
        User loggedInUser = prepareUser();
        loggedInUser.setId(SECOND_USER_ID);

        BorrowHistory history = prepareBorrowHistory();
        history.setIsReturned(false);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(loggedInUser));
        when(historyRepository.findById(BORROW_ID)).thenReturn(Optional.of(history));

        // Act
        LibraryException ex = assertThrows(LibraryException.class,
                () -> service.returnBook(BORROW_ID, returnRequestDTO));

        // Assert
        assertEquals(ERR_INVALID_USER.getDescription(), ex.getMessage());
    }
}
