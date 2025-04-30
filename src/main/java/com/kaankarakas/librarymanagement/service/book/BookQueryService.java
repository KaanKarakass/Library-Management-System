package com.kaankarakas.librarymanagement.service.book;

import com.kaankarakas.librarymanagement.dto.request.book.SearchBookRequest;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.dto.response.book.BookPageResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface BookQueryService {

    public Book findBookById(Long id);

    public BookPageResponseDTO searchBooks(@NotNull @Valid SearchBookRequest searchBookRequest);
}
