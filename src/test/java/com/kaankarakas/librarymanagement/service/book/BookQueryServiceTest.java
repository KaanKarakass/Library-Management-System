package com.kaankarakas.librarymanagement.service.book;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.dto.request.book.SearchBookRequest;
import com.kaankarakas.librarymanagement.dto.response.book.BookDTO;
import com.kaankarakas.librarymanagement.dto.response.book.BookPageResponseDTO;
import com.kaankarakas.librarymanagement.enums.Genre;
import com.kaankarakas.librarymanagement.mapper.book.BookMapper;
import com.kaankarakas.librarymanagement.repository.book.BookRepository;
import com.kaankarakas.librarymanagement.service.book.Impl.BookQueryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.kaankarakas.librarymanagement.validator.book.BookServiceValidationRule.ERR_BOOK_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BookQueryServiceTest {

    private static final long BOOK_ID = 1L;
    private static final String BOOK_TITLE = "Test Book";
    private static final String BOOK_AUTHOR = "Test Author";
    private static final String BOOK_ISBN = "1231234123";
    private static final LocalDate PUBLICATION_DATE = LocalDate.of(2025, 5, 5);
    private static final Genre GENRE = Genre.BIOGRAPHY;
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookQueryServiceImpl bookQueryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
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

    private SearchBookRequest prepareSearchBookRequest(boolean isCompleteRequest) {
        SearchBookRequest.SearchBookRequestBuilder builder = SearchBookRequest.builder()
                .title(null)
                .author(null)
                .genre(null)
                .isbn(null)
                .page(PAGE)
                .size(SIZE);

        if (isCompleteRequest) {
            builder.title(BOOK_TITLE)
                    .author(BOOK_AUTHOR)
                    .genre(GENRE.name())
                    .isbn(BOOK_ISBN);
        }
        return builder.build();
    }

    @Test
    void findBookById_ShouldReturnBookDTO_WhenBookExists() {
        // Arrange
        Book book = prepareBook();
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(bookMapper.toDTO(book)).thenReturn(prepareBookDTO());

        //Act
        BookDTO response = bookQueryService.findBookById(BOOK_ID);

        //Assert
        assertNotNull(response);
        assertEquals(BOOK_TITLE, response.getTitle());
        assertEquals(BOOK_AUTHOR, response.getAuthor());
    }

    @Test
    void findBookById_ShouldThrowException_WhenBookNotFound() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> bookQueryService.findBookById(BOOK_ID));

        // Assert
        assertEquals(ERR_BOOK_NOT_FOUND.getDescription(), exception.getMessage());
    }

    @Test
    void searchBooks_ShouldReturnPageResponse() {
        // Arrange
        SearchBookRequest request = prepareSearchBookRequest(true);

        Book book = prepareBook();
        BookDTO bookDTO = prepareBookDTO();

        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);

        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(bookPage);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        // Act
        BookPageResponseDTO result = bookQueryService.searchBooks(request);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getBooks().size());
        assertEquals(BOOK_TITLE, result.getBooks().get(0).getTitle());
    }

    @Test
    void searchBooks_ShouldReturnAllBooks_WhenRequestIsEmpty() {
        // Arrange
        SearchBookRequest emptyRequest = prepareSearchBookRequest(false);
        Book book = prepareBook();
        BookDTO bookDTO = prepareBookDTO();

        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);

        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(bookPage);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        // Act
        BookPageResponseDTO result = bookQueryService.searchBooks(emptyRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getBooks().size());
        assertEquals(BOOK_TITLE, result.getBooks().get(0).getTitle());
    }

}
