package com.kaankarakas.librarymanagement.unit.service.book;

import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.dto.response.book.BookDTO;
import com.kaankarakas.librarymanagement.enums.BookStatus;
import com.kaankarakas.librarymanagement.enums.Genre;
import com.kaankarakas.librarymanagement.mapper.book.BookMapper;
import com.kaankarakas.librarymanagement.repository.book.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

public abstract class BookServiceTestBase {
    protected static final long BOOK_ID = 1L;
    protected static final String BOOK_TITLE = "Test Book";
    protected static final String BOOK_AUTHOR = "Test Author";
    protected static final String BOOK_ISBN = "1231234123";
    protected static final String UPDATED_BOOK_ISBN = "128593849";
    protected static final LocalDate PUBLICATION_DATE = LocalDate.of(2025, 5, 5);
    protected static final Genre GENRE = Genre.BIOGRAPHY;
    protected static final BookStatus DELETED_BOOK_STATUS = BookStatus.DELETED;

    @Mock
    protected BookRepository bookRepository;

    @Mock
    protected BookMapper bookMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
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

    protected BookDTO prepareBookDTO() {
        BookDTO book = new BookDTO();
        book.setTitle(BOOK_TITLE);
        book.setAuthor(BOOK_AUTHOR);
        book.setIsbn(BOOK_ISBN);
        book.setPublicationDate(PUBLICATION_DATE.toString());
        book.setGenre(GENRE.name());
        return book;
    }
}
