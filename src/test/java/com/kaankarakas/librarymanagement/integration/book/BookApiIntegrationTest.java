package com.kaankarakas.librarymanagement.integration.book;

import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.dto.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.dto.request.book.SearchBookRequest;
import com.kaankarakas.librarymanagement.dto.request.book.UpdateBookRequest;
import com.kaankarakas.librarymanagement.dto.response.book.BookDTO;
import com.kaankarakas.librarymanagement.dto.response.book.BookPageResponseDTO;
import com.kaankarakas.librarymanagement.enums.BookStatus;
import com.kaankarakas.librarymanagement.enums.Genre;
import com.kaankarakas.librarymanagement.integration.base.BaseIntegrationTest;
import com.kaankarakas.librarymanagement.repository.book.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookApiIntegrationTest extends BaseIntegrationTest {

    private static final String BOOK_URL = "/api/books";
    private static final String ADD_BOOK_URL = BOOK_URL + "/add";
    private static final String SEARCH_BOOK_URL = BOOK_URL + "/search";
    private static final String UPDATE_BOOK_URL = BOOK_URL + "/update";
    private static final String DELETE_BOOK_URL = BOOK_URL + "/delete";

    private static final long BOOK_ID = 1L;
    private static final long INVALID_BOOK_ID = 999999L;
    private static final long DELETED_BOOK_ID = 2L;
    private static final String BOOK_TITLE = "Integration Book";
    private static final String DEFINED_BOOK_TITLE = "The Hobbit";
    private static final String UPDATED_BOOK_TITLE = "Updated Book";
    private static final String BOOK_AUTHOR = "Author Int";
    private static final String BOOK_ISBN = "000111222";
    private static final String DEFINED_ISBN = "9780547928227";
    private static final LocalDate PUBLICATION_DATE = LocalDate.of(2025, 5, 1);
    private static final Genre GENRE = Genre.FANTASY;
    private static final BookStatus DELETED_BOOK_STATUS = BookStatus.DELETED;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private CreateBookRequest prepareCreateBookRequest() {
        return CreateBookRequest.builder()
                .isbn(BOOK_ISBN)
                .title(BOOK_TITLE)
                .author(BOOK_AUTHOR)
                .genre(GENRE)
                .publicationDate(PUBLICATION_DATE)
                .build();
    }

    @Test
    void addBook_Success() {
        // Arrange
        CreateBookRequest createBookRequest = prepareCreateBookRequest();

        HttpEntity<CreateBookRequest> request = new HttpEntity<>(createBookRequest, jsonHeaders(librarianToken));

        // Act
        ResponseEntity<BookDTO> response = restTemplate.exchange(
                URI.create(url(ADD_BOOK_URL)), HttpMethod.POST, request, BookDTO.class
        );
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        BookDTO body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getTitle()).isEqualTo(BOOK_TITLE);

        // DB Assert
        List<Book> all = bookRepository.findAll();
        assertThat(all).anyMatch(book -> BOOK_TITLE.equals(book.getTitle()));
    }

    @Test
    void addBook_InvalidData() {
        // Arrange
        CreateBookRequest request = prepareCreateBookRequest();
        request.setIsbn(DEFINED_ISBN);
        HttpEntity<CreateBookRequest> httpEntity = new HttpEntity<>(request, jsonHeaders(librarianToken));

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(url(ADD_BOOK_URL)), HttpMethod.POST, httpEntity, String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private SearchBookRequest prepareSearchBookRequest() {
        return SearchBookRequest.builder()
                .page(0)
                .size(10).build();
    }

    @Test
    void getBookById_Success() {
        // Arrange
        HttpEntity<Object> request = new HttpEntity<>(jsonHeaders(patronToken));

        // Act
        ResponseEntity<BookDTO> response = restTemplate.exchange(
                URI.create(url(BOOK_URL + "/" + BOOK_ID)),
                HttpMethod.GET, request, BookDTO.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo(DEFINED_BOOK_TITLE);
    }

    @Test
    void getBookById_NotFound() {
        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(url(BOOK_URL + "/" + INVALID_BOOK_ID)), HttpMethod.GET, new HttpEntity<>(jsonHeaders(patronToken)), String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void searchBooks_Success() {
        // Arrange
        HttpEntity<SearchBookRequest> request = new HttpEntity<>(prepareSearchBookRequest(), jsonHeaders(patronToken));

        // Act
        ResponseEntity<BookPageResponseDTO> response = restTemplate.exchange(
                URI.create(url(SEARCH_BOOK_URL)),
                HttpMethod.POST, request, BookPageResponseDTO.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBooks()).isNotNull();
        assertThat(response.getBody().getBooks().get(0).getTitle()).isEqualTo(DEFINED_BOOK_TITLE);
    }

    private UpdateBookRequest prepareUpdateBookRequest() {
        return UpdateBookRequest.builder()
                .isbn(BOOK_ISBN)
                .title(UPDATED_BOOK_TITLE)
                .build();
    }

    @Test
    @DirtiesContext
    void updateBook_Success() {
        // Arrange
        HttpEntity<UpdateBookRequest> request = new HttpEntity<>(prepareUpdateBookRequest(), jsonHeaders(librarianToken));

        // Act
        ResponseEntity<BookDTO> response = restTemplate.exchange(
                URI.create(url(UPDATE_BOOK_URL + "/" + BOOK_ID)),
                HttpMethod.PUT, request, BookDTO.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo(UPDATED_BOOK_TITLE);
    }

    @Test
    void updateBook_NotFound() {
        // Arrange
        HttpEntity<UpdateBookRequest> request = new HttpEntity<>(prepareUpdateBookRequest(), jsonHeaders(librarianToken));

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(url(UPDATE_BOOK_URL + "/" + INVALID_BOOK_ID)), HttpMethod.PUT, request, String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    void deleteBook_Success() {
        // Arrange
        HttpEntity<Object> request = new HttpEntity<>(jsonHeaders(librarianToken));

        // Act
        ResponseEntity<BookDTO> response = restTemplate.exchange(
                URI.create(url(DELETE_BOOK_URL + "/" + DELETED_BOOK_ID)),
                HttpMethod.DELETE, request, BookDTO.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getBookStatus()).isEqualTo(DELETED_BOOK_STATUS.toString());
    }

    @Test
    void deleteBook_NotFound() {
        // Act
        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(url(DELETE_BOOK_URL + "/" + INVALID_BOOK_ID)), HttpMethod.DELETE, new HttpEntity<>(jsonHeaders(librarianToken)), String.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
