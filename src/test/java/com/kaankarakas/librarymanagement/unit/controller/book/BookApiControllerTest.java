package com.kaankarakas.librarymanagement.unit.controller.book;

import com.kaankarakas.librarymanagement.api.controller.book.BookApiController;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.dto.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.dto.request.book.SearchBookRequest;
import com.kaankarakas.librarymanagement.dto.request.book.UpdateBookRequest;
import com.kaankarakas.librarymanagement.dto.response.auth.AuthResponse;
import com.kaankarakas.librarymanagement.dto.response.book.BookDTO;
import com.kaankarakas.librarymanagement.dto.response.book.BookPageResponseDTO;
import com.kaankarakas.librarymanagement.enums.BookStatus;
import com.kaankarakas.librarymanagement.enums.Genre;
import com.kaankarakas.librarymanagement.service.book.BookCommandService;
import com.kaankarakas.librarymanagement.service.book.BookQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookApiControllerTest {

    private static final LocalDate FUTURE_DATE = LocalDate.now().plusDays(10);
    private static final String UPDATED_BOOK_TITLE = "Updated Book";
    private static final long BOOK_ID = 1L;
    private static final String BOOK_TITLE = "Test Book";
    private static final String BOOK_AUTHOR = "Test Author";
    private static final String BOOK_ISBN = "1231234123";
    private static final String UPDATED_BOOK_ISBN = "128593849";
    private static final LocalDate PUBLICATION_DATE = LocalDate.of(2025, 5, 5);
    private static final Genre GENRE = Genre.BIOGRAPHY;
    private static final BookStatus DELETED_BOOK_STATUS = BookStatus.DELETED;
    private static final int PAGE = 0;
    private static final int TOTAL_PAGE = 1;
    private static final int SIZE = 10;
    private static final int TOTAL_ELEMENTS = 1;

    @Mock
    private BookCommandService bookCommandService;

    @Mock
    private BookQueryService bookQueryService;

    BookApiController bookApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookApiController = new BookApiController(bookCommandService, bookQueryService);
    }

    private Book prepareBook() {
        Book book = new Book();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setAuthor(BOOK_AUTHOR);
        book.setIsbn(BOOK_ISBN);
        book.setPublicationDate(PUBLICATION_DATE);
        book.setGenre(GENRE);
        return book;
    }

    private BookDTO prepareBookDTO() {
        BookDTO book = new BookDTO();
        book.setTitle(BOOK_TITLE);
        book.setAuthor(BOOK_AUTHOR);
        book.setIsbn(BOOK_ISBN);
        book.setPublicationDate(PUBLICATION_DATE.toString());
        book.setGenre(GENRE.name());
        return book;
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

    private UpdateBookRequest prepareUpdateBookRequest() {
        return UpdateBookRequest.builder()
                .isbn(BOOK_ISBN)
                .title(UPDATED_BOOK_TITLE)
                .build();
    }

    private SearchBookRequest prepareSearchBookRequest() {
        return SearchBookRequest.builder().title(BOOK_TITLE)
                .author(BOOK_AUTHOR)
                .genre(GENRE.name())
                .isbn(BOOK_ISBN)
                .page(PAGE)
                .size(SIZE)
                .build();
    }

    @Test
    void addBook_ShouldReturnCreated_WhenRequestIsValid() {
        // Arrange
        CreateBookRequest request = prepareCreateBookRequest();

        BookDTO mockResponse = prepareBookDTO();

        when(bookCommandService.addBook(any(CreateBookRequest.class))).thenReturn(mockResponse);

        // Act
        BookDTO response = bookApiController.addBook(request);

        // Assert
        verify(bookCommandService).addBook(request);
        assertEquals(mockResponse, response);
    }


    @Test
    void getBookById_ShouldReturnOk_WhenBookExists() {
        // Arrange
        BookDTO mockResponse = prepareBookDTO();

        when(bookQueryService.findBookById(BOOK_ID)).thenReturn(mockResponse);

        // Act
        BookDTO response = bookApiController.getBookById(BOOK_ID);

        // Assert
        verify(bookQueryService).findBookById(BOOK_ID);
        assertEquals(mockResponse, response);
    }

    private BookPageResponseDTO prepareBookPageResponseDTO() {
        return new BookPageResponseDTO(List.of(prepareBookDTO()), TOTAL_ELEMENTS, TOTAL_PAGE, PAGE, SIZE);
    }

    @Test
    void searchBooks_ShouldReturnOk_WhenRequestIsValid() {
        // Arrange
        SearchBookRequest request = prepareSearchBookRequest();

        BookPageResponseDTO mockResponse = prepareBookPageResponseDTO();

        when(bookQueryService.searchBooks(any(SearchBookRequest.class))).thenReturn(mockResponse);

        // Act
        BookPageResponseDTO response = bookApiController.searchBooks(request);

        // Assert
        verify(bookQueryService).searchBooks(request);
        assertEquals(mockResponse, response);
    }


    @Test
    void updateBook_ShouldReturnOk_WhenBookExists() {
        // Arrange
        UpdateBookRequest request = prepareUpdateBookRequest();
        BookDTO mockResponse = prepareBookDTO();

        when(bookCommandService.updateBook(BOOK_ID, request)).thenReturn(mockResponse);

        // Act
        BookDTO response = bookApiController.updateBook(BOOK_ID, request);

        // Assert
        verify(bookCommandService).updateBook(BOOK_ID, request);
        assertEquals(mockResponse, response);
    }

    @Test
    void deleteBook_ShouldReturnOk_WhenBookExists() {
        // Arrange
        BookDTO mockResponse = prepareBookDTO();

        when(bookCommandService.deleteBook(BOOK_ID)).thenReturn(mockResponse);

        // Act
        BookDTO response = bookApiController.deleteBook(BOOK_ID);

        // Assert
        verify(bookCommandService).deleteBook(BOOK_ID);
        assertEquals(mockResponse, response);
    }
}
