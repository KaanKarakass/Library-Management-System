package com.kaankarakas.librarymanagement.unit.controller.borrowhistory;

import com.kaankarakas.librarymanagement.api.controller.borrowhistory.BorrowHistoryApiController;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.domain.borrowhistory.BorrowHistory;
import com.kaankarakas.librarymanagement.domain.user.User;
import com.kaankarakas.librarymanagement.dto.request.borrowhistory.BorrowRequestDTO;
import com.kaankarakas.librarymanagement.dto.request.borrowhistory.ReturnRequestDTO;
import com.kaankarakas.librarymanagement.dto.response.borrowhistory.BorrowHistoryDTO;
import com.kaankarakas.librarymanagement.enums.Genre;
import com.kaankarakas.librarymanagement.enums.UserRole;
import com.kaankarakas.librarymanagement.enums.UserStatus;
import com.kaankarakas.librarymanagement.service.borrowhistory.BorrowHistoryCommandService;
import com.kaankarakas.librarymanagement.service.borrowhistory.BorrowHistoryQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BorrowHistoryApiControllerTest {
    private static final String USERNAME = "username";
    private static final long USER_ID = 1L;
    private static final long BORROW_ID = 1L;
    private static final long BOOK_ID = 1L;
    private static final String BOOK_TITLE = "Test Book";
    private static final LocalDate BORROW_DATE = LocalDate.of(2020, 2, 2);
    private static final LocalDate DUE_DATE = LocalDate.of(2020, 2, 20);
    private static final LocalDate RETURN_DATE = LocalDate.of(2020, 2, 20);
    private static final String OVERDUE_NONE = "Overdue: None";


    @Mock
    private BorrowHistoryCommandService commandService;

    @Mock
    private BorrowHistoryQueryService queryService;

    private BorrowHistoryApiController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new BorrowHistoryApiController(commandService, queryService);
    }


    private BorrowHistoryDTO prepareBorrowHistoryDTO() {
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
    void borrowBook_ShouldCallServiceAndReturnDto() {
        // Arrange
        BorrowRequestDTO request = prepareBorrowRequestDTO();
        BorrowHistoryDTO mockResponse = prepareBorrowHistoryDTO();
        when(commandService.borrowBook(request)).thenReturn(mockResponse);

        // Act
        BorrowHistoryDTO result = controller.borrowBook(request);

        // Assert
        verify(commandService).borrowBook(request);
        assertEquals(mockResponse, result);
    }

    @Test
    void returnBook_ShouldCallServiceAndReturnDto() {
        // Arrange
        ReturnRequestDTO request = prepareReturnRequestDTO();
        BorrowHistoryDTO mockResponse = prepareBorrowHistoryDTO();
        when(commandService.returnBook(BORROW_ID, request)).thenReturn(mockResponse);

        // Act
        BorrowHistoryDTO result = controller.returnBook(BORROW_ID, request);

        // Assert
        verify(commandService).returnBook(BORROW_ID, request);
        assertEquals(mockResponse, result);
    }

    @Test
    void getUserBorrowHistory_ShouldCallServiceAndReturnList() {
        // Arrange
        List<BorrowHistoryDTO> mockList = List.of(prepareBorrowHistoryDTO());
        when(queryService.getUserHistory()).thenReturn(mockList);

        // Act
        List<BorrowHistoryDTO> result = controller.getUserBorrowHistory();

        // Assert
        verify(queryService).getUserHistory();
        assertEquals(mockList, result);
    }

    @Test
    void getAllBorrowHistory_ShouldCallServiceAndReturnList() {
        // Arrange
        List<BorrowHistoryDTO> mockList = List.of(prepareBorrowHistoryDTO());
        when(queryService.getAllHistories()).thenReturn(mockList);

        // Act
        List<BorrowHistoryDTO> result = controller.getAllBorrowHistory();

        // Assert
        verify(queryService).getAllHistories();
        assertEquals(mockList, result);
    }

    @Test
    void generateOverdueReport_ShouldCallServiceAndReturnString() {
        // Arrange
        when(queryService.generateOverdueReport()).thenReturn(OVERDUE_NONE);

        // Act
        String result = controller.generateOverdueReport();

        // Assert
        verify(queryService).generateOverdueReport();
        assertEquals(OVERDUE_NONE, result);
    }

}
