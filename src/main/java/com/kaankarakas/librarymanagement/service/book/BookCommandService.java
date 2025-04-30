package com.kaankarakas.librarymanagement.service.book;

import com.kaankarakas.librarymanagement.api.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.api.request.book.UpdateBookRequest;
import com.kaankarakas.librarymanagement.domain.book.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface BookCommandService {

    Book addBook(@NotNull @Valid CreateBookRequest createBookRequest);

    Book updateBook(@NotNull @Valid UpdateBookRequest updateBookRequest);

    Book deleteBook(@NotNull Long bookId);
}
