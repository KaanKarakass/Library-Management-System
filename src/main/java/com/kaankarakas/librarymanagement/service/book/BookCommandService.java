package com.kaankarakas.librarymanagement.service.book;

import com.kaankarakas.librarymanagement.dto.request.book.CreateBookRequest;
import com.kaankarakas.librarymanagement.dto.request.book.UpdateBookRequest;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.dto.response.book.BookDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface BookCommandService {

    BookDTO addBook(@NotNull @Valid CreateBookRequest createBookRequest);

    BookDTO updateBook(@NotNull Long id, @NotNull @Valid UpdateBookRequest updateBookRequest);

    BookDTO deleteBook(@NotNull Long bookId);
}
