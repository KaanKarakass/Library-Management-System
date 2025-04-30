package com.kaankarakas.librarymanagement.service.book;

import com.kaankarakas.librarymanagement.api.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.domain.book.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface BookCommandService {
    Book addBook(@NotNull @Valid CreateBookRequest createBookRequest);
}
