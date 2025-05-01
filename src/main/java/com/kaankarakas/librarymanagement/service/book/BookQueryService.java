package com.kaankarakas.librarymanagement.service.book;

import com.kaankarakas.librarymanagement.dto.request.book.SearchBookRequest;
import com.kaankarakas.librarymanagement.dto.response.book.BookDTO;
import com.kaankarakas.librarymanagement.dto.response.book.BookPageResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface BookQueryService {

     BookDTO findBookById(Long id);

     BookPageResponseDTO searchBooks(@NotNull @Valid SearchBookRequest searchBookRequest);
}
