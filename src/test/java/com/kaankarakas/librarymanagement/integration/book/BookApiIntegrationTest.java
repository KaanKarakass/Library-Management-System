package com.kaankarakas.librarymanagement.integration.book;

import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.dto.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.dto.response.book.BookDTO;
import com.kaankarakas.librarymanagement.enums.BookStatus;
import com.kaankarakas.librarymanagement.enums.Genre;
import com.kaankarakas.librarymanagement.repository.book.BookRepository;
import com.kaankarakas.librarymanagement.repository.user.UserRepository;
import com.kaankarakas.librarymanagement.security.JwtAuthenticationFilter;
import com.kaankarakas.librarymanagement.service.auth.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class BookApiIntegrationTest {

    private static final String API_BASE_URL = "http://localhost:";
    private static final String BOOK_URL = "/api/books";
    private static final String ADD_BOOK_URL = BOOK_URL + "/add";


    private static final long BOOK_ID = 1L;
    private static final String BOOK_TITLE = "Integration Book";
    private static final String BOOK_AUTHOR = "Author Int";
    private static final String BOOK_ISBN = "000111222";
    private static final String UPDATED_BOOK_ISBN = "128593849";
    private static final LocalDate PUBLICATION_DATE = LocalDate.of(2025, 5, 1);
    private static final Genre GENRE = Genre.FANTASY;
    private static final BookStatus DELETED_BOOK_STATUS = BookStatus.DELETED;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        this.restTemplate = new TestRestTemplate();
    }

    private String url(String path) {
        return API_BASE_URL + port + path;
    }

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
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

        HttpEntity<CreateBookRequest> request = new HttpEntity<>(createBookRequest, jsonHeaders());

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
}
