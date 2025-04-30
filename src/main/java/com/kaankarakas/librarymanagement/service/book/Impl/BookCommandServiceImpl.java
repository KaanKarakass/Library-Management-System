package com.kaankarakas.librarymanagement.service.book.Impl;

import com.kaankarakas.librarymanagement.api.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.repository.book.BookRepository;
import com.kaankarakas.librarymanagement.service.book.BookCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.kaankarakas.librarymanagement.validator.book.BookServiceValidationRule.ERR_BOOK_ALREADY_EXISTS;
import static com.kaankarakas.librarymanagement.validator.book.BookServiceValidationRule.ERR_PUBLICATION_DATE_FUTURE;

@Service
@RequiredArgsConstructor
public class BookCommandServiceImpl implements BookCommandService {

    private final BookRepository bookRepository;

    @Override
    public Book addBook(CreateBookRequest createBookRequest) {
        return bookRepository.save(checkAndPrepareAddBook(createBookRequest));
    }

    private Book checkAndPrepareAddBook(CreateBookRequest createBookRequest) {
        if (bookRepository.existsByIsbn(createBookRequest.getISBN())) {
            throw new IllegalArgumentException(ERR_BOOK_ALREADY_EXISTS.getDescription());
        }
        if (createBookRequest.getPublicationDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(ERR_PUBLICATION_DATE_FUTURE.getDescription());
        }
        Book book = new Book();
        book.setTitle(createBookRequest.getTitle());
        book.setAuthor(createBookRequest.getAuthor());
        book.setIsbn(createBookRequest.getISBN());
        book.setPublicationDate(createBookRequest.getPublicationDate());
        book.setGenre(createBookRequest.getGenre());
        return book;
    }
}
