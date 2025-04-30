package com.kaankarakas.librarymanagement.service.book;

import com.kaankarakas.librarymanagement.api.request.book.SearchBookRequest;
import com.kaankarakas.librarymanagement.domain.book.Book;
import com.kaankarakas.librarymanagement.dto.BookDTO;
import com.kaankarakas.librarymanagement.dto.BookPageResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.query.Page;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface BookQueryService {

    public Book findBookById(UUID id);

    public BookPageResponseDTO searchBooks(@NotNull @Valid SearchBookRequest searchBookRequest);
}
