package com.kaankarakas.librarymanagement.integration.borrowhistory;

import com.kaankarakas.librarymanagement.dto.request.borrowhistory.BorrowRequestDTO;
import com.kaankarakas.librarymanagement.dto.request.borrowhistory.ReturnRequestDTO;
import com.kaankarakas.librarymanagement.dto.response.borrowhistory.BorrowHistoryDTO;
import com.kaankarakas.librarymanagement.integration.base.BaseIntegrationTest;
import com.kaankarakas.librarymanagement.repository.barrowhistory.BorrowHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BorrowHistoryIntegrationTest extends BaseIntegrationTest {

    private static final String BORROW_HISTORY_URL = "/api/borrow-history";
    private static final String BORROW_BOOK_URL = BORROW_HISTORY_URL + "/borrow";
    private static final String GET_BORROW_HISTORY = BORROW_HISTORY_URL + "/user";
    private static final String GENERATE_REPORT_URL = BORROW_HISTORY_URL + "/report/overdue";
    private static final String RETURN_BOOK_URL = "/return";

    private static final long BOOK_ID = 1L;
    private static final long INVALID_ID = 99999L;
    private static final long BORROW_HISTORY_ID = 1L;
    private static final long INVALID_BORROW_HISTORY_ID = 999999L;
    private static final LocalDate BORROW_DATE = LocalDate.of(2025, 5, 9);
    private static final LocalDate DUE_DATE = LocalDate.of(2025, 6, 9);

    @Autowired
    private BorrowHistoryRepository borrowHistoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private BorrowRequestDTO prepareBorrowRequestDTO() {
        BorrowRequestDTO borrowRequestDTO = new BorrowRequestDTO();
        borrowRequestDTO.setBookId(BOOK_ID);
        borrowRequestDTO.setBorrowDate(BORROW_DATE);
        borrowRequestDTO.setDueDate(DUE_DATE);
        return borrowRequestDTO;
    }

    @Test
    @DirtiesContext
    void borrowBook_Success() {
        // Arrange
        BorrowRequestDTO request = prepareBorrowRequestDTO();
        HttpEntity<BorrowRequestDTO> httpEntity = new HttpEntity<>(request, jsonHeaders(patronToken));

        // Act
        ResponseEntity<BorrowHistoryDTO> response = restTemplate.exchange(
                URI.create(url(BORROW_BOOK_URL)), HttpMethod.POST, httpEntity, BorrowHistoryDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void borrowBook_Fail_BookNotAvailable() {
        // Arrange
        BorrowRequestDTO request = prepareBorrowRequestDTO();
        request.setBookId(INVALID_ID);
        HttpEntity<BorrowRequestDTO> httpEntity = new HttpEntity<>(request, jsonHeaders(patronToken));

        // Act
        ResponseEntity<BorrowHistoryDTO> response = restTemplate.exchange(
                URI.create(url(BORROW_BOOK_URL)), HttpMethod.POST, httpEntity, BorrowHistoryDTO.class
        );
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    @DirtiesContext
    void returnBook_Success() {
        // Arrange
        ReturnRequestDTO request = new ReturnRequestDTO();
        HttpEntity<ReturnRequestDTO> httpEntity = new HttpEntity<>(request, jsonHeaders(patronToken));

        // Act
        ResponseEntity<BorrowHistoryDTO> response = restTemplate.exchange(
                URI.create(url(BORROW_HISTORY_URL + "/" + BORROW_HISTORY_ID + RETURN_BOOK_URL)), HttpMethod.POST, httpEntity, BorrowHistoryDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIsReturned()).isEqualTo(true);
    }

    @Test
    void returnBook_Fail_HistoryNotFound() {
        // Arrange
        ReturnRequestDTO request = new ReturnRequestDTO();
        HttpEntity<ReturnRequestDTO> httpEntity = new HttpEntity<>(request, jsonHeaders(patronToken));

        // Act
        ResponseEntity<BorrowHistoryDTO> response = restTemplate.exchange(
                URI.create(url(BORROW_HISTORY_URL + "/" + INVALID_BORROW_HISTORY_ID + RETURN_BOOK_URL)), HttpMethod.POST, httpEntity, BorrowHistoryDTO.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getUserBorrowHistory_Success() {
        // Arrange
        HttpEntity<Object> request = new HttpEntity<>(jsonHeaders(patronToken));

        // Act
        ResponseEntity<BorrowHistoryDTO[]> response = restTemplate.exchange(
                URI.create(url(GET_BORROW_HISTORY)), HttpMethod.GET, request, BorrowHistoryDTO[].class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getAllBorrowHistory_Success() {
        HttpEntity<Object> request = new HttpEntity<>(jsonHeaders(librarianToken));

        ResponseEntity<BorrowHistoryDTO[]> response = restTemplate.exchange(
                URI.create(url(BORROW_HISTORY_URL)), HttpMethod.GET, request, BorrowHistoryDTO[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void generateOverdueReport_Success() {
        HttpEntity<Object> request = new HttpEntity<>(jsonHeaders(librarianToken));

        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(url(GENERATE_REPORT_URL)), HttpMethod.GET, request, String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

}
