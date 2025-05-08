package com.kaankarakas.librarymanagement.unit.service.book;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.dto.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.dto.request.book.UpdateBookRequest;
import com.kaankarakas.librarymanagement.dto.response.book.BookDTO;
import com.kaankarakas.librarymanagement.service.book.BookCommandService;
import com.kaankarakas.librarymanagement.service.book.Impl.BookCommandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Optional;

import static com.kaankarakas.librarymanagement.validator.book.BookServiceValidationRule.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookCommandServiceTest extends BookServiceTestBase {

    private static final LocalDate FUTURE_DATE = LocalDate.now().plusDays(10);
    private static final String UPDATED_BOOK_TITLE = "Updated Book";

    @Mock
    private BookCommandService bookCommandService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        bookCommandService = new BookCommandServiceImpl(bookRepository, bookMapper);
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

    @Test
    void addBook_ShouldReturnBookDTO_WhenBookIsSuccessfullyAdded() {
        // Arrange
        CreateBookRequest createBookRequest = prepareCreateBookRequest();

        Book book = prepareBook();
        BookDTO bookDTO = prepareBookDTO();

        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        // Act
        BookDTO response = bookCommandService.addBook(createBookRequest);

        // Assert
        assertNotNull(response);
        assertEquals(BOOK_TITLE, response.getTitle());
        verify(bookRepository).save(any(Book.class));
        verify(bookMapper).toDTO(any(Book.class));
    }

    @Test
    void addBook_ShouldThrowLibraryException_WhenIsbnAlreadyExists() {
        // Arrange
        CreateBookRequest createBookRequest = prepareCreateBookRequest();

        when(bookRepository.existsByIsbnAndBookStatusNot(anyString(), any())).thenReturn(true);

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> bookCommandService.addBook(createBookRequest));

        // Assert
        assertEquals(ERR_BOOK_ALREADY_EXISTS.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void addBook_ShouldThrowLibraryException_WhenPublicationDateInFuture() {
        // Arrange
        CreateBookRequest createBookRequest = prepareCreateBookRequest();
        createBookRequest.setPublicationDate(FUTURE_DATE);

        when(bookRepository.existsByIsbnAndBookStatusNot(anyString(), any())).thenReturn(false);

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> bookCommandService.addBook(createBookRequest));

        // Assert
        assertEquals(ERR_PUBLICATION_DATE_FUTURE.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void updateBook_ShouldReturnBookDTO_WhenBookIsUpdated() {
        // Arrange
        UpdateBookRequest updateBookRequest = prepareUpdateBookRequest();

        Book existingBook = prepareBook();

        Book updatedBook = prepareBook();
        updatedBook.setTitle(UPDATED_BOOK_TITLE);

        BookDTO updatedBookDTO = prepareBookDTO();
        updatedBookDTO.setTitle(UPDATED_BOOK_TITLE);

        when(bookRepository.findById(BOOK_ID)).thenReturn(java.util.Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        when(bookMapper.toDTO(updatedBook)).thenReturn(updatedBookDTO);

        // Act
        BookDTO result = bookCommandService.updateBook(BOOK_ID, updateBookRequest);

        // Assert
        assertNotNull(result);
        assertEquals(UPDATED_BOOK_TITLE, result.getTitle());
        verify(bookRepository).findById(BOOK_ID);
        verify(bookRepository).save(any(Book.class));
        verify(bookMapper).toDTO(any(Book.class));
    }

    @Test
    void updateBook_ShouldReturnBookDTO_WhenIsbnIsNull() {
        // Arrange
        UpdateBookRequest updateBookRequest = prepareUpdateBookRequest();
        updateBookRequest.setIsbn(null);

        Book existingBook = prepareBook();

        Book updatedBook = prepareBook();
        updatedBook.setTitle(UPDATED_BOOK_TITLE);

        BookDTO updatedBookDTO = prepareBookDTO();
        updatedBookDTO.setTitle(UPDATED_BOOK_TITLE);

        when(bookRepository.findById(BOOK_ID)).thenReturn(java.util.Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        when(bookMapper.toDTO(updatedBook)).thenReturn(updatedBookDTO);

        // Act
        BookDTO result = bookCommandService.updateBook(BOOK_ID, updateBookRequest);

        // Assert
        assertNotNull(result);
        assertEquals(UPDATED_BOOK_TITLE, result.getTitle());
        verify(bookRepository).findById(BOOK_ID);
        verify(bookRepository).save(any(Book.class));
        verify(bookMapper).toDTO(any(Book.class));
    }

    @Test
    void updateBook_ShouldThrowLibraryException_WhenBookNotFound() {
        // Arrange
        UpdateBookRequest updateBookRequest = prepareUpdateBookRequest();

        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> bookCommandService.updateBook(BOOK_ID, updateBookRequest));

        // Assert
        assertEquals(ERR_BOOK_NOT_FOUND.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void updateBook_ShouldThrowLibraryException_WhenIsbnAlreadyExists() {
        // Arrange
        UpdateBookRequest updateBookRequest = prepareUpdateBookRequest();
        updateBookRequest.setIsbn(UPDATED_BOOK_ISBN);

        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.ofNullable(prepareBook()));
        when(bookRepository.existsByIsbnAndBookStatusNot(anyString(), any())).thenReturn(true);

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> bookCommandService.updateBook(BOOK_ID, updateBookRequest));

        // Assert
        assertEquals(ERR_BOOK_ALREADY_EXISTS.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void deleteBook_ShouldReturnBookDTO_WhenBookIsDeleted() {
        // Arrange
        Book book = prepareBook();
        BookDTO bookDTO = prepareBookDTO();
        bookDTO.setBookStatus(DELETED_BOOK_STATUS.name());

        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        // Act
        BookDTO result = bookCommandService.deleteBook(BOOK_ID);

        // Assert
        assertNotNull(result);
        assertEquals(BOOK_TITLE, result.getTitle());
        assertEquals(DELETED_BOOK_STATUS.name(), result.getBookStatus());
        verify(bookRepository).save(any(Book.class));
        verify(bookMapper).toDTO(any(Book.class));
    }

    @Test
    void deleteBook_ShouldThrowLibraryException_WhenBookAlreadyDeleted() {
        // Arrange
        Book book = prepareBook();
        book.setBookStatus(DELETED_BOOK_STATUS);

        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(book));

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> bookCommandService.deleteBook(BOOK_ID));

        // Assert
        assertEquals(ERR_BOOK_ALREADY_DELETED.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void deleteBook_ShouldThrowLibraryException_WhenBookNotFound() {
        // Arrange

        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());

        // Act
        LibraryException exception = assertThrows(LibraryException.class, () -> bookCommandService.deleteBook(BOOK_ID));

        // Assert
        assertEquals(ERR_BOOK_NOT_FOUND.getDescription(), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
