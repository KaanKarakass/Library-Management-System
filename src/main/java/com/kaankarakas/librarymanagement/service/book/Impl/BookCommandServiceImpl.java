package com.kaankarakas.librarymanagement.service.book.Impl;

import com.kaankarakas.librarymanagement.api.exception.LibraryException;
import com.kaankarakas.librarymanagement.dto.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.dto.request.book.UpdateBookRequest;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.dto.response.book.BookDTO;
import com.kaankarakas.librarymanagement.enums.BookStatus;
import com.kaankarakas.librarymanagement.mapper.book.BookMapper;
import com.kaankarakas.librarymanagement.repository.book.BookRepository;
import com.kaankarakas.librarymanagement.service.book.BookCommandService;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

import static com.kaankarakas.librarymanagement.validator.book.BookServiceValidationRule.*;

@Service
@RequiredArgsConstructor
public class BookCommandServiceImpl implements BookCommandService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDTO addBook(CreateBookRequest createBookRequest) {
        return bookMapper.toDTO(bookRepository.save(checkAndPrepareAddBook(createBookRequest)));
    }

    @Override
    public BookDTO updateBook(@NotNull Long id, UpdateBookRequest updateBookRequest) {

        Book book = checkBookId(id);

        if (updateBookRequest.getIsbn() != null && !Objects.equals(book.getIsbn(), updateBookRequest.getIsbn())) {
            checkIsbn(updateBookRequest.getIsbn());
        }

        bookMapper.updateBookFromDto(updateBookRequest, book);

        return bookMapper.toDTO(bookRepository.save(book));
    }

    @Override
    public BookDTO deleteBook(Long bookId) {
        Book book = checkBookId(bookId);

        if (BookStatus.DELETED.equals(book.getBookStatus())) {
            throw new LibraryException(ERR_BOOK_ALREADY_DELETED.getDescription(), HttpStatus.BAD_REQUEST);
        }
        book.setBookStatus(BookStatus.DELETED);
        return bookMapper.toDTO(bookRepository.save(book));
    }

    private Book checkAndPrepareAddBook(CreateBookRequest createBookRequest) {
        checkIsbn(createBookRequest.getIsbn());
        checkPublicationDate(createBookRequest.getPublicationDate());

        Book book = new Book();
        book.setTitle(createBookRequest.getTitle());
        book.setAuthor(createBookRequest.getAuthor());
        book.setIsbn(createBookRequest.getIsbn());
        book.setPublicationDate(createBookRequest.getPublicationDate());
        book.setGenre(createBookRequest.getGenre());
        return book;
    }
    private Book checkBookId(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new LibraryException(ERR_BOOK_NOT_FOUND.getDescription(), HttpStatus.NOT_FOUND));
    }
    private void checkIsbn(String ISBN) {
        if (bookRepository.existsByIsbnAndBookStatusNot(ISBN, BookStatus.DELETED)) {
            throw new LibraryException(ERR_BOOK_ALREADY_EXISTS.getDescription(), HttpStatus.BAD_REQUEST);
        }
    }
    private void checkPublicationDate(LocalDate publicationDate) {
        if (publicationDate.isAfter(LocalDate.now())) {
            throw new LibraryException(ERR_PUBLICATION_DATE_FUTURE.getDescription(), HttpStatus.BAD_REQUEST);
        }
    }
}
