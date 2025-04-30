package com.kaankarakas.librarymanagement.service.book.Impl;

import com.kaankarakas.librarymanagement.api.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.api.request.book.UpdateBookRequest;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.enums.Status;
import com.kaankarakas.librarymanagement.mapper.book.BookMapper;
import com.kaankarakas.librarymanagement.repository.book.BookRepository;
import com.kaankarakas.librarymanagement.service.book.BookCommandService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
    public Book addBook(CreateBookRequest createBookRequest) {
        return bookRepository.save(checkAndPrepareAddBook(createBookRequest));
    }

    @Override
    public Book updateBook(UpdateBookRequest updateBookRequest) {

        Book book = checkBookId(updateBookRequest.getBookId());

        if (updateBookRequest.getISBN() != null && !Objects.equals(book.getIsbn(), updateBookRequest.getISBN())) {
            checkIsbn(updateBookRequest.getISBN());
        }

        bookMapper.updateBookFromDto(updateBookRequest, book);

        return bookRepository.save(book);
    }

    @Override
    public Book deleteBook(Long bookId) {
        Book book = checkBookId(bookId);

        if (Status.DELETED.equals(book.getStatus())) {
            throw new IllegalArgumentException(ERR_BOOK_ALREADY_DELETED.getDescription());
        }
        book.setStatus(Status.DELETED);
        return bookRepository.save(book);
    }

    private Book checkAndPrepareAddBook(CreateBookRequest createBookRequest) {
        checkIsbn(createBookRequest.getISBN());
        checkPublicationDate(createBookRequest.getPublicationDate());

        Book book = new Book();
        book.setTitle(createBookRequest.getTitle());
        book.setAuthor(createBookRequest.getAuthor());
        book.setIsbn(createBookRequest.getISBN());
        book.setPublicationDate(createBookRequest.getPublicationDate());
        book.setGenre(createBookRequest.getGenre());
        return book;
    }
    private Book checkBookId(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ERR_BOOK_NOT_FOUND.getDescription()));
    }
    private void checkIsbn(String ISBN) {
        if (bookRepository.existsByIsbn(ISBN)) {
            throw new IllegalArgumentException(ERR_BOOK_ALREADY_EXISTS.getDescription());
        }
    }
    private void checkPublicationDate(LocalDate publicationDate) {
        if (publicationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(ERR_PUBLICATION_DATE_FUTURE.getDescription());
        }
    }
}
