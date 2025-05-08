package com.kaankarakas.librarymanagement.unit.service.borrowhistory;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.domain.borrowhistory.BorrowHistory;
import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.response.borrowhistory.BorrowHistoryDTO;
import com.kaankarakas.librarymanagement.security.SecurityUtil;
import com.kaankarakas.librarymanagement.service.borrowhistory.BorrowHistoryQueryService;
import com.kaankarakas.librarymanagement.service.borrowhistory.impl.BorrowHistoryQueryServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.kaankarakas.librarymanagement.validator.borrowhistory.BorrowHistoryServiceValidationRule.ERR_USER_NOT_ELIGIBLE;
import static com.kaankarakas.librarymanagement.validator.user.UserServiceValidationRule.ERR_USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class BorrowHistoryQueryServiceTest extends BorrowHistoryServiceTestBase {
    private static final int BORROW_HISTORY_SIZE = 1;
    private static final String REPORT_HEADER = "OVERDUE BOOKS REPORT";
    private static final String TOTAL_OVERDUE = "Total Overdue Records: 1";
    private static final String USER_OVERDUE = "Users with Overdues: 1";
    private static final String BOOK_OVERDUE = "did not return Book 1 (\"Test Book\")";

    @Mock
    private BorrowHistoryQueryService service;

    private MockedStatic<SecurityUtil> mockedStatic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentUsername).thenReturn(USERNAME);
        service = new BorrowHistoryQueryServiceImpl(historyRepository, userRepository, borrowHistoryMapper);
    }

    @AfterEach
    void tearDown() {
        mockedStatic.close();
    }

    @Test
    void getUserHistory_ShouldReturnHistoryList() {
        // Arrange
        User activeUser = prepareUser();
        BorrowHistory history = prepareBorrowHistory();
        BorrowHistoryDTO historyDTO = prepareBorrowHistoryDTO();
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(activeUser));
        when(historyRepository.findByUser(activeUser)).thenReturn(List.of(history));
        when(borrowHistoryMapper.toDTO(history)).thenReturn(historyDTO);

        // Act
        List<BorrowHistoryDTO> result = service.getUserHistory();

        // Assert
        assertEquals(BORROW_HISTORY_SIZE, result.size());
        assertEquals(historyDTO, result.get(0));
    }

    @Test
    void getUserHistory_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> service.getUserHistory());

        // Assert
        assertEquals(ERR_USER_NOT_FOUND.getDescription(), exception.getMessage());
    }

    @Test
    void getUserHistory_ShouldThrowException_WhenUserNotEligible() {
        // Arrange
        User inactiveUser = prepareUser();
        inactiveUser.setUserStatus(DELETED_STATUS);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(inactiveUser));

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> service.getUserHistory());

        // Assert
        assertEquals(ERR_USER_NOT_ELIGIBLE.getDescription(), exception.getMessage());
    }

    @Test
    void getAllHistories_ShouldReturnList() {
        // Arrange
        BorrowHistory history = prepareBorrowHistory();
        BorrowHistoryDTO historyDTO = prepareBorrowHistoryDTO();
        when(historyRepository.findAll()).thenReturn(List.of(history));
        when(borrowHistoryMapper.toDTO(history)).thenReturn(historyDTO);

        // Act
        List<BorrowHistoryDTO> result = service.getAllHistories();

        // Assert
        assertEquals(1, result.size());
        assertEquals(historyDTO, result.get(0));
    }

    @Test
    void generateOverdueReport_ShouldReturnFormattedReport() {
        // Arrange
        BorrowHistory history = prepareBorrowHistory();
        BorrowHistoryDTO historyDTO = prepareBorrowHistoryDTO();
        when(historyRepository.findByIsReturnedFalseAndDueDateBefore(any(LocalDate.class))).thenReturn(List.of(history));
        when(borrowHistoryMapper.toDTO(history)).thenReturn(historyDTO);

        // Act
        String report = service.generateOverdueReport();

        // Assert
        assertTrue(report.contains(REPORT_HEADER));
        assertTrue(report.contains(TOTAL_OVERDUE));
        assertTrue(report.contains(USER_OVERDUE));
        assertTrue(report.contains(BOOK_OVERDUE));
    }
}
